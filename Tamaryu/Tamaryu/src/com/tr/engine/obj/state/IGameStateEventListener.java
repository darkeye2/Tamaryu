package com.tr.engine.obj.state;

import com.tr.engine.rewardsystem.RewardManager;

public final class IGameStateEventListener
{
	
	//public void stateChanged(int id, String name);
	public static void notifyAfterLogin()
	{
		RewardManager.notifyAfterLogin();
	}
	
	public static void notifyAfterMiniGame(int time, int score)
	{
		RewardManager.notifyAfterMiniGame(time, score);
	}
	
	
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
