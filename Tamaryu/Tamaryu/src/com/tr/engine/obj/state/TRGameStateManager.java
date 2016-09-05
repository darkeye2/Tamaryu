package com.tr.engine.obj.state;

import com.tr.engine.core.TRGameLooper;
import com.tr.engine.grf.TRScene;

public final class TRGameStateManager {
	protected static TRScene scene = null;
	protected static TRGameLooper looper = null;
	
	protected static TRAbstractGameState curState = null;
	protected static TRGameStateFactory factory = null;
	
	public static void init(TRScene scene, TRGameLooper looper){
		TRGameStateManager.scene = scene;
		TRGameStateManager.looper = looper;
	}
	
	public static void setState(TRAbstractGameState gs){
		if(curState != null){
			curState.unload(scene, looper);
		}
		curState = gs;
		//System.out.println("Loading State: "+gs.getName());
		curState.load(scene, looper);
	}
	
	public static void setState(int gsId){
		if(factory != null){
			TRAbstractGameState g = factory.getState(gsId);
			if(g != null){
				TRGameStateManager.setState(g);
			}else{
				TRGameStateManager.setState(factory.getDefaultState());
			}
		}
	}
	
	public static void error(){
		if(factory != null){
			TRGameStateManager.setState(factory.getErrorState());
		}
	}
	
	public static void setFactory(TRGameStateFactory f){
		TRGameStateManager.factory = f;
	}
	
	public static void reset(){
		curState.reset(scene, looper);
	}
	
	
}
