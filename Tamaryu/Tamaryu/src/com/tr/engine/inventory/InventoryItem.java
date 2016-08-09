package com.tr.engine.inventory;

public class InventoryItem implements IInventoryable
{
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
