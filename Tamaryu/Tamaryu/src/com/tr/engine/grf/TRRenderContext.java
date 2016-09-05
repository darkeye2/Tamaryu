package com.tr.engine.grf;

import java.util.ArrayList;

public abstract class TRRenderContext {
	//time properties
	protected long startTime = 0;
	protected long curTime = 0;
	protected long elapsedTime = 0;
	
	//debug
	protected boolean debug = true;
	
	//components
	protected float sceneWidth = 0, sceneHeight = 0;
	protected TRScene scene = null;
	protected TRGameWindow gameWindow = null;
	
	public abstract void init();
	public abstract void destruct();
	public abstract void addOnClose(Runnable close);
	public abstract void refresh();
	public abstract TRGameWindow createWindow();
	public abstract TRScene getScene();
	
	//render properties
	private boolean enableGlobalRenderProperties = false;
	private ArrayList<TRRenderPropertie> renderProperties = new ArrayList<TRRenderPropertie>();
	
	public TRRenderContext(int sceneWidth, int sceneHeight){
		this.sceneWidth = sceneWidth;
		this.sceneHeight = sceneHeight;
	}
	
	
	public void setDebug(boolean enable){
		debug = enable;
	}
	
	public void setGRP(boolean enable){
		this.enableGlobalRenderProperties = enable;
	}
	
	public boolean areGRPEnabled(){
		return this.enableGlobalRenderProperties;
	}
	
	
	
	public ArrayList<TRRenderPropertie> getRenderProperties(){
		return this.renderProperties;
	}
	
	public void addRenderPropertie(TRRenderPropertie p){
		for(int i = 0; i < renderProperties.size(); i++){
			if(renderProperties.get(i).getID() == p.getID()){
				renderProperties.set(i, p);
				return;
			}
		}
		renderProperties.add(p);
	}
	
	public void removeRenderPropertie(int propertieConstant){
		int i = 0;
		if(renderProperties.size() <=0){
			return;
		}
		for(; i < renderProperties.size(); i++){
			if(renderProperties.get(i).getID() == propertieConstant){
				break;
			}
		}
		if(i < renderProperties.size()){
			renderProperties.remove(i);
		}
	}
	
	
	public void removeRenderProperties(TRRenderPropertie p){
		if(renderProperties.contains(p)){
			renderProperties.remove(p);
		}
	}
	
	public void clearRenderProperties(){
		this.renderProperties.clear();
	}
}
