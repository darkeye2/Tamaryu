package com.tr.engine.obj.state;

import com.tr.engine.core.TRGameLooper;
import com.tr.engine.grf.TRScene;

public abstract class TRAbstractGameState {
	protected int id = -1;
	protected String name = "";
	
	public TRAbstractGameState(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public abstract void load(TRScene scene, TRGameLooper gl);
	
	public abstract void unload(TRScene scene, TRGameLooper gl);
	
	public void reset(TRScene scene, TRGameLooper gl){
		unload(scene, gl);
		load(scene, gl);
	}
}
