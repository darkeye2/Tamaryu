package com.tr.test.gl;

import com.jogamp.opengl.GL2ES3;
import com.tr.engine.grf.gl.TRGL2DRenderable;
import com.tr.gl.core.GLProgramm;
import com.tr.gl.core.GLTexture;

public class TestGLTama extends TRGL2DRenderable{
	
	float[] data = new float[]{
			//	   X	   Y	 Z			U	V
		         0.25f,  0.25f, 0f, 	   1f, 1f,
		        -0.25f,  0.25f, 0f,	   0f, 1f,
		         0.25f, -0.25f, 0f,	   1f, 0f,
		         0.25f, -0.25f, 0f, 	   1f, 0f,
		        -0.25f,  0.25f, 0f,	   0f, 1f,
		        -0.25f, -0.25f, 0f,	   0f, 0f}; 
	
	public TestGLTama(){
		this.setData(data, DATA_FORMAT_XYZUV);
		
		this.setProgram(new GLProgramm("/shader/", new String[]{"default_f", "default_v"},
				new int[]{GL2ES3.GL_FRAGMENT_SHADER, GL2ES3.GL_VERTEX_SHADER}, "default"));
		this.setTexture(new GLTexture("dancing1.png", true, "dancing1"));
	}
	

}
