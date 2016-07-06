package com.tr.engine.inventory;

public class InventoryItem
{
	private String name;
	private String type;
	private int amount;
	
	/**
	 * 
	 * Constructor
	 * 
	 */
	public InventoryItem(String name)
	{
		this.name = name;
		this.type = "Misc.";
		this.amount = 1;
	}
	
	public InventoryItem(String name, String type)
	{
		this.name = name;
		this.type = type;
		this.amount = 1;
	}
	
	public InventoryItem(String name, int amount)
	{
		this.name = name;
		this.type = "Misc.";
		this.amount = amount;
	}
	
	public InventoryItem(String name, String type, int amount)
	{
		this.name = name;
		this.type = type;
		this.amount = amount;
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
	
	/**
	 * 
	 * Methods
	 * 
	 */
	
	public void increaseAmount(int plus)
	{
		this.amount += plus;
	}
	
	public void decreaseAmount(int minus)
	{
		this.amount -= minus;
	}
}
