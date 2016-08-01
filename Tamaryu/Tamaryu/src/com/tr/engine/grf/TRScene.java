package com.tr.engine.grf;

import java.util.ArrayList;

public abstract class TRScene {
	protected TRRenderContext rc;
	
	//component list
	protected ArrayList<IRenderable> components = new ArrayList<IRenderable>();
	protected IRenderable bg = null;
	protected ICamera cam = null;
	
	public TRScene(TRRenderContext context){
		this.rc = context;
	}
	
	public abstract void addComponent(IRenderable ra);
	public abstract boolean removeComponent(IRenderable ra);
	public abstract void clearScene();
	public abstract void setBackground(IRenderable bg);
	public abstract float getFPS();
	public abstract void setTargetFPS(int fps);
	
	public ICamera getCamera(){
		return cam;
	}

	public void setSceneSize(float sceneWidth, float sceneHeight) {
		cam.setReferenceSize(sceneWidth, sceneHeight);
	}
}
