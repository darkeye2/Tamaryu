package com.tr.engine.rewardsystem;

import com.tr.engine.inventory.IInventoryEventListener;
import com.tr.engine.inventory.InventorySystem;
import com.tr.engine.obj.state.IGameStateEventListener;

public class ListenerForReward implements IGameStateEventListener, IInventoryEventListener
{
	private static final ListenerForReward thisListener = new ListenerForReward(); 
	
	private ListenerForReward()
	{
		int bla = InventorySystem.getInventory().size();
		RewardManager.initRewardManager();
	}
	
	public static ListenerForReward getInstance()
	{
		return thisListener;
	}
	
	@Override
	public void notifyGameStateChange(int gameStateID)
	{
		switch(gameStateID)
		{
		//Help State
		case(9):
			System.out.println("Listener for Reward: Help State");
			break;
		//Setting Language State
		case(8):
			System.out.println("Listener for Reward: Language State");
			break;
		//Setting State
		case(7):
			//RewardManager.createLootBox();
			System.out.println("Listener for Reward: Settings State");
			break;
		//Result State
		case(6):
			RewardManager.createLootBox();
			System.out.println("Listener for Reward: Result State");
			break;
		//Minigame State
		case(5):
			System.out.println("Listener for Reward: Minigame State");
			break;
		//Breeding State
		case(4):
			System.out.println("Listener for Reward: Breeding State");
			break;
		//Interact State
		case(3):
			System.out.println("Listener for Reward: Interact State");
			break;
		//Dragon State
		case(2):
			System.out.println("Listener for Reward: Dragon State");
			break;
		//Island State
		case(1):
			System.out.println("Listener for Reward: Island State");
			break;
		//Login State
		case(0):
		default:
			System.out.println("Listener for Reward: Login State");
			break;
		}
	}

	@Override
	public void notifyItemUse(int itemPositionID)
	{
		RewardManager.notifyLootBoxUse(itemPositionID);
	}
}
