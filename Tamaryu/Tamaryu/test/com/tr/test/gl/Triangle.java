package com.tr.test.gl;

import java.io.IOException;
import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.texture.TextureIO;
import com.tr.engine.grf.TRRenderContext;
import com.tr.engine.grf.gl.TRGL2DRenderable;
import com.tr.engine.grf.gl.TRGLRenderContext;
import com.tr.gl.core.GLCamera;
import com.tr.gl.core.GLProgramManager;
import com.tr.gl.core.GLProgramm;
import com.tr.gl.core.GLTexture;
import com.tr.gl.core.Point3D;
import com.tr.gl.core.Semantic;

public class Triangle extends TRGL2DRenderable {
	private Matrix4 tmpmat = new Matrix4();
	private float[] proj_matrix;
	private int mv_location = 0;

	public Triangle() {
		this.proj_matrix = tmpmat.getMatrix();
		
			this.data = new float[]{
				//	   X	   Y	Z			U	V
			         0.25f,  0.25f, -1f, 	   1f, 1f,
			        -0.25f,  0.25f, -1f,		   0f, 1f,
			         0.25f, -0.25f, -1f,		   1f, 0f,
			         0.25f, -0.25f, -1f, 	   1f, 0f,
			        -0.25f,  0.25f, -1f,		   0f, 1f,
			        -0.25f, -0.25f, -1f,		   0f, 0f}; 
		this.dataFormat = TRGL2DRenderable.DATA_FORMAT_XYZUV;
		
		this.setProgram(new GLProgramm("/shader/", new String[]{"simple2d_f", "simple2d_v"},
				new int[]{GL2ES3.GL_FRAGMENT_SHADER, GL2ES3.GL_VERTEX_SHADER}, "default"));
	}

	public void update(float[] proj_matrix) {
		this.proj_matrix = tmpmat.getMatrix();
	}

	@Override
	public void init(TRRenderContext context) {
		System.out.println("Triangle Created!");
		this.pos = new Point3D(1.5f, 0.5f, 0.0f);
		this.setPosition(0f, 0, 0);
		TRGLRenderContext con = (TRGLRenderContext) context;
		GL2ES3 gl = (GL2ES3) con.getGL();
		
			//con.checkError(gl, "before compiling");
			this.program.init(gl);
			
			//con.checkError(gl, "on compiling");

			// init vertex array object
			gl.glGenVertexArrays(1, objects, Semantic.Object.VAO);
			gl.glBindVertexArray(objects[Semantic.Object.VAO]);

			// init vertex buffer object
			gl.glGenBuffers(1, objects, Semantic.Object.VBO);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[Semantic.Object.VBO]);
			
			//load texture
			this.texture = new GLTexture("dancing1.png", "dancing1");
			this.texture = this.texture.init(gl);

		// set the view
		mv_location = gl.glGetUniformLocation(program.getID(), "mv_matrix");
		projMatLocation = gl.glGetUniformLocation(program.getID(), "proj_matrix");
		this.colorLoc = gl.glGetUniformLocation(program.getID(), "color");

		gl.glDisable(GL.GL_CULL_FACE);
		gl.glFrontFace(GL.GL_CW);

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
	}

	@Override
	public void render(TRRenderContext context){
		GL2ES3 gl = (GL2ES3) ((TRGLRenderContext) context).getGL();
		GLCamera cam = (GLCamera) context.getScene().getCamera();
		//gl.glDisable(GL.GL_CULL_FACE);
		//gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_BLEND);
		//gl.glEnable(GL.GL_ALPHA);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		//gl.glBlendEquation(GL.GL_FUNC_ADD );
		
		//gl.glDisable(GL.GL_DEPTH_TEST);
		// let use the program for this object
		gl.glUseProgram(program.getID());

		// load data into the buffer
		gl.glBufferData(GL.GL_ARRAY_BUFFER, data.length * 4,
				FloatBuffer.wrap(data), GL.GL_STATIC_DRAW);

		// bind attribute
		gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 4*5, 0);
		gl.glEnableVertexAttribArray(1);
		gl.glVertexAttribPointer(2, 2, GL.GL_FLOAT, false, 4*5, 4*3);
		gl.glEnableVertexAttribArray(2);

		// update projection matrix
		gl.glUniformMatrix4fv(projMatLocation, 1, false, cam.getOrthoMatrix(), 0);

		// update model_view matrix
		Matrix4 mv_matrix = new Matrix4(); // load identity
		// mv_matrix.multMatrix(cam.getVM());
		// mv_matrix.multMatrix(cam.getViewMatrix()); //mult by view matrix
		mv_matrix.multMatrix(model_matrix); //mult by model matrix

		/*
		 * mv_matrix.translate(this.pos.x, this.pos.y, this.pos.z); float[]
		 * lookAt = new float[16]; float [] tmp = new float[16]; lookAt =
		 * FloatUtil.makeLookAt(lookAt, 0, cam.toArray(), 0, pos.toArray(), 0,
		 * Point3D.getYAxis().toArray(), 0, tmp); mv_matrix.multMatrix(lookAt);
		 * /*mv_matrix.translate((float) Math.sin(2.1f * f) * 0.5f, (float)
		 * Math.cos(1.7f * f) * 0.5f, (float) (Math.sin(1.3f * f) *
		 * Math.cos(1.5f * f)) * 2.0f); mv_matrix.rotate((float) f * 100 *
		 * 45.0f, 0.0f, 1.0f, 0.0f); mv_matrix.rotate((float) f * 100 * 81.0f,
		 * 1.0f, 0.0f, 0.0f);
		 */
		// setRotation(f, f, 0);
		// f+=f_step;
		
		long time = System.currentTimeMillis();
		time = time>>5;
		time = time % 100000;
		//float rgb[] = {((time>>4)%100)/255f, ((time>>2)%100)/255f, (time%100)/255f};
		float rgb[] = {Math.abs((float)Math.cos(0.0175f*time)), 
				Math.abs((float)Math.sin(0.0175f*time)), 
				Math.abs((float)Math.sin(0.0175f*time)-(float)Math.cos(0.0175f*time))};
		//System.out.println(time);
		//System.out.println("RGB Color: "+rgb[0]+", "+rgb[1]+", "+rgb[2]);
		gl.glUniform3f(colorLoc, rgb[0], rgb[1], rgb[2]);

		// pass the new location matrix to the shader
		gl.glUniformMatrix4fv(mv_location, 1, false, mv_matrix.getMatrix(), 0);
		
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
        texture.getTexture().enable(gl);
        texture.getTexture().bind(gl);
        gl.glUniform1i(gl.glGetUniformLocation(program.getID(), "tex_object"), 0); 

		// draw the object
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
		
		//gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	

}
