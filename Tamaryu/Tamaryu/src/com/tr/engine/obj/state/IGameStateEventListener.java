package com.tr.engine.obj.state;

import com.tr.engine.rewardsystem.RewardManager;

public final class IGameStateEventListener
{
	public static void notifyAfterLogin()
	{
		RewardManager.notifyAfterLogin();
	}
	
	public static void notifyAfterMiniGame(int time, int score)
	{
		RewardManager.notifyAfterMiniGame(time, score);
	}
}
