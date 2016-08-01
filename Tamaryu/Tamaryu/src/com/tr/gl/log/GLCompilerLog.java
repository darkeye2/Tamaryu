package com.tr.gl.log;

import java.util.ArrayList;

import com.jogamp.opengl.GL2ES3;

public class GLCompilerLog {
	private ArrayList<GLShaderLog> logs = new ArrayList<GLShaderLog>();
	private int programLength = 0;
	private boolean success = true;
	
	public void createLog(GL2ES3 gl, int shaderId){
		GLShaderLog l = new GLShaderLog(gl, shaderId);
		logs.add(l);
		programLength += l.getSrcLength();
		success = success && l.getStatus();
	}
	
	public void addLogRequest(int shaderId){
		logs.add(new GLShaderLog(shaderId));
	}
	
	public void update(GL2ES3 gl){
		programLength = 0;
		success = true;
		for(GLShaderLog l : logs){
			l.update(gl);
			programLength += l.getSrcLength();
			success = success && l.getStatus();
		}
	}
	
	public void clear(){
		programLength = 0;
		success = true;
		logs.clear();
	}
	
	public boolean containsError(){
		return success;
	}
	
	public String toString(){
		String out = "";
		
		out += "Programm with "+logs.size()+" Shaders ("+programLength+") was compiled ";
		out += (success?"without ":"with ")+" Errors!\r\n";
		out += "------------------------------\r\n";
		for(GLShaderLog l : logs){
			out += l.toString();
			out += "\r\n";
		}
		
		return out;
	}
}
