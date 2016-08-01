package com.tr.engine.grf.gl;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.math.Matrix4;
import com.tr.engine.grf.Color;
import com.tr.engine.grf.TRRenderContext;
import com.tr.engine.grf.TRRenderPropertie;
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
	protected boolean useTexture = false;
	protected Color objectColor = Color.BLACK;
	protected boolean dataUpdated = false;
	
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
		case TRRenderPropertie.USE_COLOR_OVER_TEXTURE:
			propArray[TRRenderPropertie.OVERLAY_COLOR_RED] = p.getR();
			propArray[TRRenderPropertie.OVERLAY_COLOR_GREEN] = p.getG();
			propArray[TRRenderPropertie.OVERLAY_COLOR_BLUE] = p.getB();
			propArray[TRRenderPropertie.OVERLAY_COLOR_ALPHA] = p.getA();
			break;
		case TRRenderPropertie.USE_HSL_FILTER:
		case TRRenderPropertie.USE_RGB_FILTER:
			propArray[TRRenderPropertie.COLOR_FILTER_RED] = p.getR();
			propArray[TRRenderPropertie.COLOR_FILTER_RED] = p.getG();
			propArray[TRRenderPropertie.COLOR_FILTER_RED] = p.getB();
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
	}

	public void setProgram(GLProgramm prog) {
		this.program = prog;
	}

	@Override
	public void init(TRRenderContext context) {
		GL2ES3 gl = (GL2ES3) ((TRGLRenderContext) context).getGL();
		
		//compile programm
		this.program = this.program.init(gl);
		
		//generate texture
		this.texture = this.texture.init(gl);
		
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
		this.updateVBOData(gl);
		
		//init components inside
		for(TRGLRenderable r : this.components){
			r.init(context);
		}

		GLCamera cam = (GLCamera) context.getScene().getCamera();
		if(!normalized){
			this.updateModelMatrix(cam);
		}
		System.out.println("Model Matrix:");
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
		GLCamera.printFloatMatrix(tmp.getMatrix(), 4, 4);
	}

	private void updateVBOData(GL2ES3 gl) {
		// bind VAO
		gl.glBindVertexArray(objects[Semantic.Object.VAO]);

		// bind VBO
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[Semantic.Object.VBO]);

		// load vertex data into the buffer
		gl.glBufferData(GL.GL_ARRAY_BUFFER, this.data.length * 4, FloatBuffer.wrap(this.data), GL.GL_STATIC_DRAW);

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
		
		if(this.updateMatrix){
			this.updateModelMatrix(cam);
		}

		// use program
		gl.glUseProgram(program.getID());

		// set settings for 2D textured/untextured object
		gl.glDisable(GL.GL_CULL_FACE);
		gl.glFrontFace(GL.GL_CW);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_TEXTURE_2D);

		// update data if changed (reload into VBO)
		if (this.dataUpdated) {
			this.updateVBOData(gl);
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
		if(this.useTexture){
			gl.glActiveTexture(GL.GL_TEXTURE0);
        	texture.getTexture().enable(gl);
        	texture.getTexture().bind(gl);
        	gl.glUniform1i(gl.glGetUniformLocation(program.getID(), "tex_object"), 0); 
		}

		//draw
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.getVertexDataSize());
		
		// unbind vao
		gl.glBindVertexArray(0);
		
		//render components inside
		for(TRGLRenderable r : this.components){
			r.render(context);
		}
	}

	@Override
	public void resize(TRRenderContext context, int w, int h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destruct(TRRenderContext context) {
		GL2ES3 gl = (GL2ES3) ((TRGLRenderContext) context).getGL();
		gl.glDeleteVertexArrays(1, objects, Semantic.Object.VAO);
		gl.glDeleteBuffers(1, objects, Semantic.Object.VBO);
		this.texture = null;
		this.program = null;
	}

}
