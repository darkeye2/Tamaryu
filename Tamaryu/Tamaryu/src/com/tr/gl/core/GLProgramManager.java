package com.tr.gl.core;

import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.tr.gl.log.GLCompilerLog;

public final class GLProgramManager {
	//programm map
	protected static GLProgramMap programs = new GLProgramMap();
	
	//DEBUG
	public static boolean DEBUG = true;
	
	//compilation log of the last compiled program (only if DEBUG = true)
	protected static GLCompilerLog clog = new GLCompilerLog();
	
	public static GLProgramm getProgram(String name){
		if(programs.containsKey(name)){
			return programs.get(name);
		}
		return null;
	}
	
	public static void cleanup(GL2ES3 gl){
		programs.clearReferences(gl);
	}
	
	public static GLProgramm loadProgram(GL2ES3 gl, GLProgramm prog){
		GLProgramm p = getProgram(prog.getName());
		if(p != null)
			return p;
		
		clog.clear();
		ShaderProgram shaderProgram = new ShaderProgram();

		for (int i = 0; i < prog.shaders.length; i++) {
			ShaderCode sc = GLProgramManager.loadShader(gl, prog.shaderPath, prog.types[i], prog.shaders[i]);
			shaderProgram.add(sc);
			if(DEBUG){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				clog.addLogRequest(sc.id());
			}
		}
		
		shaderProgram.init(gl);
		prog.id = shaderProgram.program();
		
		programs.put(prog.getName(), prog);

		shaderProgram.link(gl, System.err);
		
		if(DEBUG){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clog.update(gl);
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(clog.containsError()){
				System.err.println(clog.toString());
			}else{
				System.out.println(clog.toString());
			}
		}

		
		return prog;
	}
	
	public static GLCompilerLog getCompilationLog(){
		return clog;
	}
	
	protected static ShaderCode loadShader(GL2ES3 gl, String path, int type, String name){
		ShaderCode sc =  ShaderCode.create(gl, type, GLProgramManager.class, path, null,
				name, "sl", null, true);
		sc.defaultShaderCustomization(gl, true, true);
		
		return sc;
	}

}
