package com.tr.engine.debug;

import com.tr.engine.grf.TRRenderContext;

public abstract class TRDebugger {
	protected TRRenderContext rc;
	
	public TRDebugger(TRRenderContext rc){
		this.rc = rc;
	}
	
	public abstract void log(String msg);
	public abstract void logErr(String err);
	public abstract void stop();
	
}
