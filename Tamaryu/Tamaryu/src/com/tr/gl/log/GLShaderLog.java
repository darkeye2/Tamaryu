package com.tr.gl.log;

import com.jogamp.opengl.GL2ES3;

public class GLShaderLog {
	private int shaderName = -1;
	private boolean status = false;
	private int type = -1;
	private int srcLength = -1;
	private int infoLength = -1;
	private String info = new String();
	
	private int[] tmp = new int[1];
	
	public GLShaderLog(GL2ES3 gl, int id){
		shaderName = id;
		update(gl);
	}
	
	public GLShaderLog(int id){
		shaderName = id;
	}
	
	public void update(GL2ES3 gl){
		gl.glGetShaderiv(shaderName, GL2ES3.GL_COMPILE_STATUS, tmp, 0);
		status = (tmp[0] == 1);
		
		gl.glGetShaderiv(shaderName, GL2ES3.GL_SHADER_TYPE, tmp, 0);
		type = tmp[0];
		
		gl.glGetShaderiv(shaderName, GL2ES3.GL_SHADER_SOURCE_LENGTH, tmp, 0);
		srcLength = tmp[0];
		
		gl.glGetShaderiv(shaderName, GL2ES3.GL_INFO_LOG_LENGTH, tmp, 0);
		infoLength = tmp[0];
		
		if(infoLength > 0){
			byte[] log = new byte[infoLength];
			gl.glGetShaderInfoLog(shaderName, infoLength, tmp, 0, log, 0);
			info = new String(log);
		}
	}
	
	public String getShaderTypeString(){
		System.out.println("Type: "+type+"; Name: "+shaderName);
		switch(type){
		case GL2ES3.GL_VERTEX_SHADER: return "GL_VERTEX_SHADER";
		case GL2ES3.GL_FRAGMENT_SHADER: return "GL_FRAGMENT_SHADER";
		/*case GL2ES3.GL_GEOMETRY_SHADER: return "GL_GEOMETRY_SHADER";
		case GL2ES3.GL_COMPUTE_SHADER: return "GL_COMPUTE_SHADER";
		case GL2ES3.GL_TESS_CONTROL_SHADER: return "GL_TESS_CONTROL_SHADER";
		case GL2ES3.GL_TESS_EVALUATION_SHADER: return "GL_TESS_EVALUATION_SHADER";*/
		default: return "UNKNOWN_SHADER_TYPE";
		}
	}
	
	public int getSrcLength(){
		return srcLength;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	
	public String toString(){
		String out = "";
		
		out += "SHADER "+shaderName+" ("+getShaderTypeString()+") - STATUS: "+status+"\r\n";
		out += "----------------------------------------------";
		out += info;
		
		return out;
	}
}
