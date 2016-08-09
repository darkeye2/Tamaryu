package com.tr.engine.inventory;

public interface IInventoryable
{
	/**
	 * 
	 * Getter
	 * 
	 */
	String getName();
	String getType();
	int getAmount();
	int getPosition();

	/**
	 * 
	 * Setter
	 * 
	 */
	void setName(String name);
	void setType(String type);
	void setAmount(int amount);
	void setPosition(int position);
	
	/**
	 * 
	 * Methods
	 * 
	 */
	void increaseAmount(int plus);

	void decreaseAmount(int minus);

}