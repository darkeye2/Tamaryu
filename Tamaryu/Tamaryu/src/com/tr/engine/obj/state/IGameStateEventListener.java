package com.tr.engine.obj.state;

public interface IGameStateEventListener
{
	public void notifyGameStateChange(int gameStateID);
		
	/*
	 * Listener = Interface
	 * Reward implementierts
	 * TRGameStateManager Arrayliste fur Listener
	 * add, remove, clear
	 * 
	 * notifys: state Changed = ID & Stringname
	 * 
	 */
}
