package com.tr.engine.rewardsystem;

import com.tr.engine.inventory.IInventoryable;
import com.tr.engine.inventory.InventoryItem;

public class LootBox implements IInventoryable
{	
	public static final int LUXERY = 0;
	public static final int PREMIUM = 1;
	public static final int RARE = 2;
	public static final int COMMON = 3;
	
	private String name;
	private String type;
	private int position;
	private int amount;
	
	LootBox(int rarity, int amount)
	{
		this.type = "lootbox";
		this.amount = amount;
		
		switch(rarity)
		{
		case(LUXERY):
			this.name = "luxerylootbox";
			this.position = InventoryItem.LOOTBOX_LUXERY;
		case(PREMIUM):
			this.name = "premiumlootbox";
		this.position = InventoryItem.LOOTBOX_PREMIUM;
		case(RARE):
			this.name = "rarelootbox";
		this.position = InventoryItem.LOOTBOX_RARE;
		case(COMMON):
		default:
			this.name = "commonlootbox";
			this.position = InventoryItem.LOOTBOX_COMMON;
		}
	}

	/**
	 * 
	 * Getter
	 * 
	 */
	
	@Override
	public String getName()
	{
		return this.name;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public int getAmount()
	{
		return this.amount;
	}
	
	public int getPosition()
	{
		return this.position;
	}
	
	/**
	 * 
	 * Setter
	 * 
	 */
		
	public void setName(String name)
	{
		this.name = name;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public void setPosition(int position)
	{
		this.position = position;
	}
	
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
		
	public void increaseAmount(int plus)
	{
		this.amount += plus;
	}
	
	public void decreaseAmount(int minus)
	{
		if(this.amount > 0)
		{
			this.amount -= minus;
		}
		else
		{
			this.amount = 0;
		}
	}
}
