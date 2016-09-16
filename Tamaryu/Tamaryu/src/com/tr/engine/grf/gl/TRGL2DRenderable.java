package com.tr.engine.grf.gl;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.packrect.Rect;
import com.tr.engine.grf.Color;
import com.tr.engine.grf.IRenderable;
import com.tr.engine.grf.TRRenderContext;
import com.tr.engine.grf.TRRenderPropertie;
import com.tr.engine.grf.TRScene;
import com.tr.gl.core.GLCamera;
import com.tr.gl.core.GLProgramm;
import com.tr.gl.core.GLTexture;
import com.tr.gl.core.Semantic;

public class TRGL2DRenderable extends TRGLRenderable {
	// constants for data format
	public static final int DATA_FORMAT_XYZ = 0x20; // X,Y,Z Coordinates
	public static final int DATA_FORMAT_XYZUV = 0x21; // X,Y,Z Coordinates, U, V
														// Texturecoordinates
	public static final int DATA_FORMAT_XYZWUV = 0x22; // X,Y,Z,W Coordinates,
														// U, V Texturecoord
	public static final int DATA_FORMAT_UVXYZ = 0x23; // U, V Texturecoord,
														// X,Y,Z Coordinates
	public static final int DATA_FORMAT_XYZ_UV_M = 0x24; // X,Y,Z , UV ,M
															// Material ID
	protected int dataFormat = DATA_FORMAT_XYZUV;
	protected volatile boolean useTexture = false;
	protected Color objectColor = Color.BLACK;
	protected volatile boolean dataUpdated = false;
	protected volatile boolean textureUpdated = false;
	
	protected Rect hitbox = new Rect();
	protected boolean ignore = false;
	protected boolean clip = false;
	
	//locations
	protected int modelMatLocation = 0;
	protected int viewMatLocation = 0;
	protected int projMatLocation  = 0;
	protected int colorLoc = 0;
	protected int settingsLoc = 0;
	
	//glsl settings as float array
	protected float[] glslSettings = new float[TRRenderPropertie.AMOUNT_OF_PROPERTIES];

	public void setData(float[] data, int dataFormat) {
		this.data = data;
		this.dataFormat = dataFormat;
		dataUpdated = true;
	}

	public void setTexture(GLTexture tex) {
		this.texture = tex;
		useTexture(true);
		this.textureUpdated = true;
		//System.out.println("Texture Update Flag set!");
	}

	public GLTexture getTexture() {
		return this.texture;
	}

	public void setColor(Color c) {
		this.objectColor = c;
		useTexture(false);
	}

	public Color getColor() {
		return this.objectColor;
	}

	public void useTexture(boolean b) {
		if (this.texture != null && b) {
			this.useTexture = b;
			glslSettings[TRRenderPropertie.USE_TEXTURE] = 1;
		} else {
			this.useTexture = false;
			glslSettings[TRRenderPropertie.USE_TEXTURE] = 0;
		}
	}
	
	public void setRenderPropertie(TRRenderPropertie p, float[] propArray){
		propArray[p.getID()] = p.getValue();
		switch(p.getID()){
		case TRRenderPropertie.USE_OUTLINE:
			propArray[TRRenderPropertie.OUTLINE_COLOR_RED] = p.getR();
			propArray[TRRenderPropertie.OUTLINE_COLOR_GREEN] = p.getG();
			propArray[TRRenderPropertie.OUTLINE_COLOR_BLUE] = p.getB();
			break;
		case TRRenderPropertie.USE_TEXTURE:
			if(p.getValue() == 0){
				propArray[TRRenderPropertie.COLOR_RED] = p.getR();
				propArray[TRRenderPropertie.COLOR_GREEN] = p.getG();
				propArray[TRRenderPropertie.COLOR_BLUE] = p.getB();
				propArray[TRRenderPropertie.COLOR_ALPHA] = p.getA();
			}
			break;
		case TRRenderPropertie.USE_COLOR_OVER_TEXTURE:
			propArray[TRRenderPropertie.OVERLAY_COLOR_RED] = p.getR();
			propArray[TRRenderPropertie.OVERLAY_COLOR_GREEN] = p.getG();
			propArray[TRRenderPropertie.OVERLAY_COLOR_BLUE] = p.getB();
			propArray[TRRenderPropertie.OVERLAY_COLOR_ALPHA] = p.getA();
			break;
		case TRRenderPropertie.USE_HSL_FILTER:
		case TRRenderPropertie.USE_RGB_FILTER:
			propArray[TRRenderPropertie.COLOR_FILTER_RED] = p.getR();
			propArray[TRRenderPropertie.COLOR_FILTER_GREEN] = p.getG();
			propArray[TRRenderPropertie.COLOR_FILTER_BLUE] = p.getB();
			break;
		case TRRenderPropertie.USE_DISTANCE_SCALE:
			propArray[TRRenderPropertie.DISTANCE_SCALE_VALUE] = p.getValue();
			break;
		case TRRenderPropertie.USE_PIXELATION:
			propArray[TRRenderPropertie.PIXELATION_VALUE] = p.getValue();
			break;
		default: break;
		}
	}
	
	public void setRenderPropertie(TRRenderPropertie p){
		this.setRenderPropertie(p, glslSettings);
		synchronized(lock){
			for(TRGLRenderable r : this.components){
				r.setRenderPropertie(p);
			}
		}
		for(TRGLRenderable r : this.inComponents){
			r.setRenderPropertie(p);
		}
	}

	public void setProgram(GLProgramm prog) {
		this.program = prog;
	}

	@Override
	public void init(TRRenderContext context) {
		GL2ES3 gl = (GL2ES3) ((TRGLRenderContext) context).getGL();
		GLCamera cam = (GLCamera) context.getScene().getCamera();
		
		//compile programm
		this.program = this.program.init(gl);
		
		//generate texture
		if(this.texture != null){
			this.texture = this.texture.init(gl);
		}
		
		//get location addr
		this.modelMatLocation = gl.glGetUniformLocation(this.program.getID(), "model_matrix");
		this.viewMatLocation = gl.glGetUniformLocation(this.program.getID(), "view_matrix");
		this.projMatLocation = gl.glGetUniformLocation(this.program.getID(), "proj_matrix");
		this.colorLoc = gl.glGetUniformLocation(this.program.getID(), "raw_color");
		this.settingsLoc = gl.glGetUniformLocation(this.program.getID(), "settings_array");

		// init vertex buffer object
		gl.glGenBuffers(1, objects, Semantic.Object.VBO);

		// init vertex array object
		gl.glGenVertexArrays(1, objects, Semantic.Object.VAO);

		// update vbo and vao data
		this.updateVBOData(gl, cam);
		
		//init components inside
		synchronized(lock){
			for(TRGLRenderable r : this.components){
				r.init(context);
			}
		}

		if(!normalized){
			this.updateModelMatrix(cam);
		}
		/*System.out.println("Model Matrix:");
		GLCamera.printFloatMatrix(this.getModelMatrix().getMatrix(), 4, 4);
		System.out.println("View Matrix:");
		GLCamera.printFloatMatrix(cam.getViewMatrix(), 4, 4);
		System.out.println("Proj Matrix:");
		GLCamera.printFloatMatrix(cam.getOrthoMatrix(), 4, 4);
		Matrix4 tmp = new Matrix4();
		tmp.multMatrix(model_matrix);
		tmp.multMatrix(cam.getViewMatrix());
		tmp.multMatrix(cam.getOrthoMatrix());
		System.out.println("MVP: ");
		GLCamera.printFloatMatrix(tmp.getMatrix(), 4, 4);*/
	}
	
	public Rect getClip(TRScene scene){
		if(this.clip){
			int x = (int) (this.getAbsolutPosition().x / ((GLCamera) scene.getCamera()).getAspectScale());
			int y = (int) (this.getAbsolutPosition().y / ((GLCamera) scene.getCamera()).getAspectScale());
			int w = (int) (this.getWidth() / ((GLCamera) scene.getCamera()).getAspectScale());
			int h = (int) (this.getHeight() / ((GLCamera) scene.getCamera()).getAspectScale());
		
			return new Rect(x,y,w,h,null);
		}
		if(this.parent != null){
			return parent.getClip(scene);
		}
		return null;
	}

	private void updateVBOData(GL2ES3 gl, GLCamera cam) {
		// bind VAO
		gl.glBindVertexArray(objects[Semantic.Object.VAO]);

		// bind VBO
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[Semantic.Object.VBO]);

		// load vertex data into the buffer
		gl.glBufferData(GL.GL_ARRAY_BUFFER, this.data.length * 4, FloatBuffer.wrap(this.normalizeData(cam)), GL.GL_STATIC_DRAW);

		// update attribute data according to data format
		switch (this.dataFormat) {
		case DATA_FORMAT_XYZ:
			gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 0, 0);
			gl.glEnableVertexAttribArray(1);
			break;
		case DATA_FORMAT_XYZWUV:
			gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 4 * 6, 0);
			gl.glEnableVertexAttribArray(1);
			gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, 4 * 6, 4 * 4);
			gl.glEnableVertexAttribArray(2);
			break;
		case DATA_FORMAT_UVXYZ:
			gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 4 * 5, 4 * 2);
			gl.glEnableVertexAttribArray(1);
			gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, 4 * 5, 0);
			gl.glEnableVertexAttribArray(2);
			break;
		case DATA_FORMAT_XYZ_UV_M:
			gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 4 * 6, 0);
			gl.glEnableVertexAttribArray(1);
			gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, 4 * 6, 4 * 3);
			gl.glEnableVertexAttribArray(2);
			gl.glVertexAttribPointer(3, 1, GL.GL_FLOAT, false, 4 * 6, 4 * 5);
			gl.glEnableVertexAttribArray(3);
			break;
		case DATA_FORMAT_XYZUV:
		default:
			gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 4 * 5, 0);
			gl.glEnableVertexAttribArray(1);
			gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, 4 * 5, 4 * 3);
			gl.glEnableVertexAttribArray(2);
			break;
		}

		// unbind vao
		gl.glBindVertexArray(0);
	}
	
	protected float[] normalizeData(GLCamera cam){
		if(this.normalized){
			return data;
		}
		
		float[] nd = new float[this.data.length];
		System.arraycopy(data, 0, nd, 0, data.length);
		int stX = 0, stY = 1, d = 3;
		
		switch (this.dataFormat) {
		case DATA_FORMAT_XYZ:
			break;
		case DATA_FORMAT_XYZWUV:
		case DATA_FORMAT_XYZ_UV_M:
			d = 6;
			break;
		case DATA_FORMAT_UVXYZ:
			stX = 2;
			stY = 3;
			d = 5;
			break;
		case DATA_FORMAT_XYZUV:
		default:
			d = 5;
			break;
		}
		
		for(int i = 0; (i+d-1) < data.length; i+=d){
			//nd[i+stX] = this.data[i+stX]*(this.width/cam.getRefWidth());
			//nd[i+stY] = this.data[i+stY]*(this.height/cam.getRefHeight());
			nd[i+stX] = (this.data[i+stX]*this.width + this.width)*(0.5f);
			nd[i+stY] = (this.data[i+stY]*this.height + this.height) * (0.5f);
		}
		
		//GLCamera.printFloatMatrix(nd, 5, 6, true);
		
		return nd;
	}
	
	private int getVertexDataSize(){
		switch (this.dataFormat) {
		case DATA_FORMAT_XYZ:
			return  data.length/3;
		case DATA_FORMAT_XYZWUV:
			return data.length/6;
		case DATA_FORMAT_UVXYZ:
			return data.length/5;
		case DATA_FORMAT_XYZ_UV_M:
			return data.length/6;
		case DATA_FORMAT_XYZUV:
		default:
			return data.length/5;
		}
	}

	@Override
	public void render(TRRenderContext context) {
		GL2ES3 gl = (GL2ES3) ((TRGLRenderContext) context).getGL();
		GLCamera cam = (GLCamera) context.getScene().getCamera();
		
		if(this.outComponents.size() > 0){
			synchronized(outLock){
				for(TRGLRenderable r : outComponents){
					r.parent = null;
					context.getScene().removeComponent(r);
					this.components.remove(r);
				}
				outComponents.clear();
			}
		}
		
		if(this.inComponents.size() > 0){
			synchronized(inLock){
				for(TRGLRenderable r : inComponents){
					context.getScene().addComponent(r);
					//System.out.println("Adding cc to RA");
					this.components.add(r);
					//System.out.println("Components: "+components.size());
				}
				inComponents.clear();
			}
		}
		
		if(this.callResize){
			this.resize(context, 0, 0);
		}
		
		if(this.updateMatrix){
			this.updateModelMatrix(cam);
		}
		
		if(this.textureUpdated){
			if(this.texture != null){
				//System.out.println("Updating texture!");
				this.texture = this.texture.init(gl);
				textureUpdated = false;
				//System.out.println("Cur Texture: "+texture.getTexture()+"; Object: "+texture);
			}
		}
		
		//render components inside (behind)
		/*for(TRGLRenderable r : this.components){
			if(r.getPosition().z < this.getPosition().z){
				r.render(context);
			}
			
		}*/

		// use program
		gl.glUseProgram(program.getID());

		// set settings for 2D textured/untextured object
		gl.glDisable(GL.GL_CULL_FACE);
		gl.glFrontFace(GL.GL_CW);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_TEXTURE_2D);

		// update data if changed (reload into VBO)
		if (this.dataUpdated) {
			this.updateVBOData(gl, cam);
			this.dataUpdated = false;
		}
		
		// use vao
		gl.glBindVertexArray(objects[Semantic.Object.VAO]);
		
		//update settings
		if(context.areGRPEnabled()){
			float[] tmp = new float[TRRenderPropertie.AMOUNT_OF_PROPERTIES];
			System.arraycopy(glslSettings, 0, tmp, 0, tmp.length);
			for(TRRenderPropertie p : context.getRenderProperties()){
				this.setRenderPropertie(p, tmp);
			}
			gl.glUniform1fv(settingsLoc, tmp.length, tmp, 0);
		}else{
			gl.glUniform1fv(settingsLoc, glslSettings.length, glslSettings, 0);
		}
		
		//update model, view and projection matrix
		gl.glUniformMatrix4fv(modelMatLocation, 1, false, this.getModelMatrix().getMatrix(), 0);
		/*System.out.println("Model Matrix:");
		GLCamera.printFloatMatrix(this.getModelMatrix().getMatrix(), 4, 4);
		System.out.println("View Matrix:");
		GLCamera.printFloatMatrix(cam.getViewMatrix(), 4, 4);
		System.out.println("Proj Matrix:");
		GLCamera.printFloatMatrix(cam.getOrthoMatrix(), 4, 4);*/
		gl.glUniformMatrix4fv(viewMatLocation, 1, false, cam.getViewMatrix(), 0);
		gl.glUniformMatrix4fv(projMatLocation, 1, false, cam.getOrthoMatrix(), 0);
		
		//use texture
		if(this.useTexture && !this.textureUpdated){
			//System.out.println("Selected texture: "+texture.getTexture()+"; Object: "+texture);
			gl.glActiveTexture(GL.GL_TEXTURE0);
        	texture.getTexture().enable(gl);
        	texture.getTexture().bind(gl);
        	gl.glUniform1i(gl.glGetUniformLocation(program.getID(), "tex_object"), 0); 
		}
		
		Rect clip = this.getClip(context.getScene());
		if(clip != null){
			gl.glScissor(clip.x(), clip.y(), clip.w(), clip.h());
			gl.glEnable(GL.GL_SCISSOR_TEST);
			//gl.glViewport(clip.x(), clip.y(), 800, 600);
			//gl.glViewport(clip.x(), clip.y(), clip.w(), clip.h());
			//System.out.println("Viewport: "+clip);
			//GLCamera.printFloatMatrix(this.getModelMatrix().getMatrix(), 4, 4);
		}

		//draw
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.getVertexDataSize());
		
		gl.glDisable(GL.GL_SCISSOR_TEST);
		//((TRGLScene) context.getScene()).resetViewport(gl);
		
		// unbind vao
		gl.glBindVertexArray(0);
		
		//render components inside (front)
		/*for(TRGLRenderable r : this.components){
			if(r.getPosition().z < this.getPosition().z){
				continue;
			}
			r.render(context);
		}*/
	}

	@Override
	public void resize(TRRenderContext context, int w, int h) {
		super.resize(context, w, h);
	}

	@Override
	public void destruct(TRRenderContext context) {
		GL2ES3 gl = (GL2ES3) ((TRGLRenderContext) context).getGL();
		gl.glDeleteVertexArrays(1, objects, Semantic.Object.VAO);
		gl.glDeleteBuffers(1, objects, Semantic.Object.VBO);
		this.texture = null;
		this.program = null;
	}
	
	public void setNormalized(boolean norm){
		this.normalized = false;
	}
	
	public boolean isNormalized(){
		return this.normalized;
	}

	@Override
	public void setSize(int w, int h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWidth() {
		return (int)this.width;
	}

	@Override
	public int getHeight() {
		return (int)this.height;
	}

	@Override
	public Rect getHitbox() {
		return this.hitbox;
	}

	@Override
	public void setHitbox(Rect r) {
		//System.out.println("New Hitbox: "+r);
		this.hitbox = r;
	}

	@Override
	public boolean isHit(float x, float y) {
		if(hitbox != null){
			if(x >= getAbsolutPosition().x+hitbox.x() && x <= getAbsolutPosition().x+hitbox.x()+(hitbox.maxX()*this.getScale())){
				if(y >= getAbsolutPosition().y+hitbox.y() && y <= getAbsolutPosition().y+hitbox.y()+(hitbox.maxY()*this.getScale())){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void propagateHit(String hitPath) {
		if(parent != null){
			String hp = (hitPath != null && !hitPath.equals(""))?(hitPath+"."+this.getName()):this.getName();
			parent.propagateHit(hp);
		}
	}

	@Override
	public boolean ignore() {
		return ignore;
	}

	@Override
	public void setIgnore(boolean b) {
		this.ignore = b;
	}

	@Override
	public IRenderable getParent() {
		return this.parent;
	}

	@Override
	public void setClipping(boolean b) {
		this.clip = b;
	}

}
