package com.tr.engine.grf;

import com.jogamp.nativewindow.util.Dimension;

public abstract class TRGameWindow {
	protected static int screenIdx = 0;
	
	//window properties
	protected com.jogamp.nativewindow.util.Dimension windowSize = new Dimension(1024, 768);
	protected String TITEL = "TR Test";
	protected boolean undecorated = false;
	protected boolean alwaysOnTop = false;
	protected boolean fullscreen = false;
	protected boolean mouseVisible = true;
	protected boolean mouseConfined = false;
	protected boolean showFPS = true;
	
	//render context 
	protected TRRenderContext renderContext = null;
	
	
	public TRGameWindow(TRRenderContext rc){
		this.renderContext = rc;
	}
	
	
	public void setTitel(String titel){
		this.TITEL = titel;
	}
	
	public abstract void setTempTitel(String titel);
	
	public String getTitel(){
		return this.TITEL;
	}
	
	public abstract void setDecorated(boolean decoration);
	public abstract void setAlwaysOnTop(boolean alwaysontop);
	public abstract void setFullscreen(boolean fullscreen);
	public abstract void setMouseVisibility(boolean mouseVisible);
	public abstract void setMouseConfidation(boolean mouseConfined);
	public abstract void showFPSinTitel(boolean show);
	public abstract boolean showFPSinTitel();
	
	public abstract void start();
	public abstract void pause();
	public abstract void stop();



}
