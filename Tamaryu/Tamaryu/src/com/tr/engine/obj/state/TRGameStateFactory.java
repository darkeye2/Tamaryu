package com.tr.engine.obj.state;

public abstract class TRGameStateFactory {
	
	public abstract TRAbstractGameState getState(int id);
	
	public abstract TRAbstractGameState getDefaultState();
	
	public abstract TRAbstractGameState getErrorState();
}
