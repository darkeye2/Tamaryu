package com.tr.engine.obj.state;

import com.tr.engine.core.TRGameLooper;
import com.tr.engine.grf.TRScene;
import com.tr.engine.rewardsystem.ListenerForReward;

public final class TRGameStateManager {
	protected static TRScene scene = null;
	protected static TRGameLooper looper = null;
	
	protected static TRAbstractGameState curState = null;
	protected static TRGameStateFactory factory = null;
	
	private static IGameStateEventListener[] arrayOfListeners = new IGameStateEventListener[]{ListenerForReward.getInstance()};
	
	public static void init(TRScene scene, TRGameLooper looper){
		TRGameStateManager.scene = scene;
		TRGameStateManager.looper = looper;
	}
	
	public static void setState(TRAbstractGameState gs){
		if(curState != null){
			if(curState.getID() == gs.getID()){
				return;
			}
			curState.unload(scene, looper);
		}
		curState = gs;
		curState.load(scene, looper);
		
		for(IGameStateEventListener listener : arrayOfListeners)
		{
			listener.notifyGameStateChange(gs.getID());
		}
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
