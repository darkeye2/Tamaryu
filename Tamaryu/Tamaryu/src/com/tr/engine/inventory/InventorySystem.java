package com.tr.engine.inventory;

import java.util.ArrayList;

public final class InventorySystem
{
	public static ArrayList<InventoryItem> inventoryItems;

	private InventorySystem()
	{
		
	}
	
	public static ArrayList<InventoryItem> getInventory()
	{
		if(InventorySystem.inventoryItems == null)
		{
			InventorySystem.loadInventory();
		}
		
		return InventorySystem.inventoryItems;
	}
	
	private static void loadInventory()
	{
		try
		{
			InventorySystem.inventoryItems = new ArrayList<InventoryItem>();
			
			//TODO
			//Load Items from somewhere (DataBase, File)
			InventoryItem item = new InventoryItem("Apfel", "Obst", 4);
			
			InventorySystem.inventoryItems.add(item);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void addItem(InventoryItem item)
	{
		if(InventorySystem.inventoryItems == null)
		{
			InventorySystem.getInventory();
		}
		if(InventorySystem.inventoryItems.isEmpty())
		{
			InventorySystem.inventoryItems.add(item);
		}
		else
		{
			boolean newItem = true;
			
			for(InventoryItem entry : InventorySystem.inventoryItems)
			{
				if(entry.getName() == item.getName())
				{
					entry.increaseAmount(item.getAmount());
					newItem = false;
				}
			}
			if(newItem)
			{
				InventorySystem.inventoryItems.add(item);
			}
		}
	}
	
	public static void useItem(InventoryItem item)
	{
		for(InventoryItem entry : InventorySystem.inventoryItems)
		{
			if(entry.getName() == item.getName())
			{
				entry.decreaseAmount(1);
			}
			if(entry.getAmount() <= 0)
			{
				InventorySystem.inventoryItems.remove(entry);
			}
		}
	}
}
