package com.tr.engine.inventory;

public class InventoryItem implements IInventoryable
{
	public static final int LOOTBOX_LUXERY = 0;
	public static final int LOOTBOX_PREMIUM = 1;
	public static final int LOOTBOX_RARE = 2;
	public static final int LOOTBOX_COMMON = 3;
	
	public static final int GOLDEN_APPLE = 10;
	public static final int AMBROSIA = 11;
	public static final int MINOTAUR_STEAK = 12;
	public static final int NECTAR = 13;
	
	public static final int PITAHAYA = 20;
	public static final int TRUFFLE = 21;
	public static final int FISH_FILLET = 22;	
	public static final int TEA = 23;
	
	public static final int ORANGE = 30;
	public static final int BROCCOLI = 31;
	public static final int SAUSAGE = 32;
	public static final int MILK = 33;
	
	public static final int APPLE = 40;
	public static final int POTATO = 41;
	public static final int CHICKEN_NUGGET = 42;
	public static final int MINERAL_WATER = 43;
	
	private String name;
	private String type;
	private int position;
	private int amount;
	
	/**
	 * 
	 * Constructor
	 * 
	 */
	public InventoryItem(String name, int posi)
	{
		this.name = name;
		this.type = "Misc.";
		this.amount = 1;
		this.position = posi;
	}
	
	public InventoryItem(String name, String type, int posi)
	{
		this.name = name;
		this.type = type;
		this.amount = 1;
		this.position = posi;
	}
	
	public InventoryItem(String name, int posi, int amount)
	{
		this.name = name;
		this.type = "Misc.";
		this.amount = amount;
		this.position = posi;
	}
	
	public InventoryItem(String name, String type, int posi, int amount)
	{
		this.name = name;
		this.type = type;
		this.amount = amount;
		this.position = posi;
	}
	
	/**
	 * 
	 * Getter
	 * 
	 */
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
		this.name = type;
	}
	
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	
	public void setPosition(int position)
	{
		this.position = position;
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
