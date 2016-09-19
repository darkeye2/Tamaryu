package com.tr.engine.rewardsystem;

import java.util.Random;

import com.tr.engine.inventory.InventoryItem;
import com.tr.engine.inventory.InventorySystem;

final class RewardManager
{
	private static int pittyCounterForRare;
	private static int pittyCounterForPremium;
	private static int pittyCounterForLuxery;
	
	private static Random rngesus;	
	
	static void initRewardManager()
	{
		//get Data from SaveFile/Server
		RewardManager.pittyCounterForLuxery = 32;
		RewardManager.pittyCounterForPremium = 32;
		RewardManager.pittyCounterForRare = 32;
		
		RewardManager.rngesus = new Random();
	}
	
	static void notifyLootBoxUse(String lootBoxName)
	{
		switch(lootBoxName)
		{
		case("luxerylootbox"):
			generateLuxeryLoot();
			break;
		case("premiumlootbox"):
			generatePremiumLoot();
			break;
		case("rarelootbox"):
			generateRareLoot();
			break;
		case("commonlootbox"):
		default:
			generateCommonLoot();
			break;
		}
	}
	
	static void notifyLootBoxUse(int lootBoxPositionID)
	{
		switch(lootBoxPositionID)
		{
		case(InventoryItem.LOOTBOX_LUXERY):
			generateLuxeryLoot();
			break;
		case(InventoryItem.LOOTBOX_PREMIUM):
			generatePremiumLoot();
			break;
		case(InventoryItem.LOOTBOX_RARE):
			generateRareLoot();
			break;
		case(InventoryItem.LOOTBOX_COMMON):
		default:
			generateCommonLoot();
			break;
		}
	}
	
	static void createLootBox()
	{
		InventorySystem.addItem(new LootBox(RewardManager.generateLootBoxRarity(), 1));
	}
	
	private static int generateLootBoxRarity()
	{
		int temp = rngesus.nextInt(128);
		
		if(temp >= 124 || pittyCounterForLuxery >= 64)
		{
			pittyCounterForLuxery -= 64;
			pittyCounterForPremium = 0;
			pittyCounterForRare = 0;
			return LootBox.LUXERY;
		}
		if(temp >= 112 || pittyCounterForPremium >= 32)
		{
			pittyCounterForLuxery++;
			pittyCounterForPremium -= 32;
			pittyCounterForRare = 0;
			return LootBox.PREMIUM;
		}
		if(temp >= 64 || pittyCounterForRare >= 16)
		{
			pittyCounterForLuxery++;
			pittyCounterForPremium++;
			pittyCounterForRare -= 16;
			return LootBox.RARE;
		}
		pittyCounterForLuxery++;
		pittyCounterForPremium++;
		pittyCounterForRare++;
		return LootBox.COMMON;		 
	}
	
	private static void generateLuxeryLoot()
	{		
		InventorySystem.addItem(new InventoryItem("goldenapple", "fruit", InventoryItem.GOLDEN_APPLE, rngesus.nextInt(5)));
		InventorySystem.addItem(new InventoryItem("ambrosia", "vegetable", InventoryItem.AMBROSIA, rngesus.nextInt(5)));
		InventorySystem.addItem(new InventoryItem("minotaursteak", "meat", InventoryItem.MINOTAUR_STEAK, rngesus.nextInt(5)));
		InventorySystem.addItem(new InventoryItem("nectar", "beverage", InventoryItem.NECTAR, rngesus.nextInt(5)));
	}
	
	private static void generatePremiumLoot()
	{
		InventorySystem.addItem(new InventoryItem("pitahaya", "fruit", InventoryItem.PITAHAYA, rngesus.nextInt(4)));
		InventorySystem.addItem(new InventoryItem("truffle", "vegetable", InventoryItem.TRUFFLE, rngesus.nextInt(4)));
		InventorySystem.addItem(new InventoryItem("fishfillet", "meat", InventoryItem.FISH_FILLET, rngesus.nextInt(4)));
		InventorySystem.addItem(new InventoryItem("tea", "beverage", InventoryItem.TEA, rngesus.nextInt(4)));
	}
	
	private static void generateRareLoot()
	{
		InventorySystem.addItem(new InventoryItem("orange", "fruit", InventoryItem.ORANGE, rngesus.nextInt(3)));
		InventorySystem.addItem(new InventoryItem("broccoli", "vegetable", InventoryItem.BROCCOLI, rngesus.nextInt(3)));
		InventorySystem.addItem(new InventoryItem("sausage", "meat", InventoryItem.SAUSAGE, rngesus.nextInt(3)));
		InventorySystem.addItem(new InventoryItem("milk", "beverage", InventoryItem.MILK, rngesus.nextInt(3)));
	}
	
	private static void generateCommonLoot()
	{
		InventorySystem.addItem(new InventoryItem("apple", "fruit", InventoryItem.APPLE, rngesus.nextInt(3)));
		InventorySystem.addItem(new InventoryItem("potato", "vegetable", InventoryItem.POTATO, rngesus.nextInt(3)));
		InventorySystem.addItem(new InventoryItem("chickennugget", "meat", InventoryItem.CHICKEN_NUGGET, rngesus.nextInt(3)));
		InventorySystem.addItem(new InventoryItem("mineralwater", "beverage", InventoryItem.MINERAL_WATER, rngesus.nextInt(3)));
	}
	
	//https://en.wikipedia.org/wiki/B._F._Skinner
	//https://en.wikipedia.org/wiki/Behaviorism
	//https://en.wikipedia.org/wiki/Reinforcement
	/*
	public static void getTokenReward()
	{
		//Token Conditioning
	}
	
	public static void continuousReinforcement()
	{
		//Win a Game
	}
	
	public static void fixedRatioReinforcement()
	{
		//Win 3 Games
	}
	
	public static void variableRatioReinforcement()
	{
		//Win x Games
	}
	
	public static void fixedIntervalReinforcement()
	{
		//Daily Reward
		
		
		//Low Response rate max X Response in Y Time
		//High Response rate min X Response in Y Time
	}
	
	public static void variableIntervalReinforcement()
	{
		//fishing
	}
	*/
}
