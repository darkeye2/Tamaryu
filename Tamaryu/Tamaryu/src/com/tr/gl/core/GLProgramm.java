package com.tr.gl.core;

import com.jogamp.opengl.GL2ES3;

public class GLProgramm {
	public String name = "";
	public String shaderPath = "/shader/"; 
	public String[] shaders;
	public int[] types;
	public int id = 0;
	
	public GLProgramm(String shaderPath, String[] shaders, int[] types, String progName){
		this.name = progName;
		this.shaderPath = shaderPath;
		this.shaders = shaders;
		this.types = types;
	}
	
	public GLProgramm init(GL2ES3 gl){
		return GLProgramManager.loadProgram(gl, this);
	}
	
	public int getID(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
}
