package com.tr.gl.core;

import com.jogamp.opengl.GL2ES3;
import com.tr.util.WeakValue;
import com.tr.util.WeakValueMap;

public class GLProgramMap extends WeakValueMap<String, GLProgramm> {
	
	protected void clearReferences(){
		
	}
	
	@SuppressWarnings("unchecked")
	public void clearReferences(GL2ES3 gl){
		WeakValue<String,GLProgramm> wk;
		
		while((wk = (WeakValue<String,GLProgramm>)queue.poll()) != null){
			map.remove(wk.getKey());
			gl.glDeleteProgram(wk.get().getID());
			wk = null;
		}
	}

}
