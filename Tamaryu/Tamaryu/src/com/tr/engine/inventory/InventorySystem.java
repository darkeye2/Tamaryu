package com.tr.engine.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.tr.engine.rewardsystem.LootBox;
import com.tr.engine.rewardsystem.RewardManager;

public final class InventorySystem
{
	public static ArrayList<IInventoryable> inventoryItems;
	private static Comparator<IInventoryable> comparator;

	private InventorySystem()
	{
		//Inventory System is Static
	}
	
	public static ArrayList<IInventoryable> getInventory()
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
			InventorySystem.inventoryItems = new ArrayList<IInventoryable>();
			
			//TODO
			//Load Items from somewhere (DataBase, File)
			IInventoryable item = new InventoryItem("Apfel", "Obst", 4);
			
			InventorySystem.inventoryItems.add(item);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void addItem(IInventoryable item)
	{
		if(item.getAmount() > 0)
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
				
				for(IInventoryable entry : InventorySystem.inventoryItems)
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
	}
	
	public static void useItem(IInventoryable item)
	{
		for(IInventoryable entry : InventorySystem.inventoryItems)
		{
			if(entry.getName() == item.getName())
			{
				entry.decreaseAmount(1);
				if(entry.getType() == "lootbox")
				{
					RewardManager.notifyLootBoxUse(entry.getName());
				}
			}
			if(entry.getAmount() <= 0)
			{
				InventorySystem.inventoryItems.remove(entry);
				break;
			}
		}
	}
	
	public static void sortInventory()
	{
		Collections.sort(inventoryItems, comparator);
	}
	
	public static void setComparator(int comparatorID)
	{
		if(comparatorID != InventoryComparatorFactory.getCurrentComparatorID())
		{
			comparator = InventoryComparatorFactory.createComparator(comparatorID);	
		}
		
	}
	
	public static void setNextComparator()
	{
		comparator = InventoryComparatorFactory.createComparator(InventoryComparatorFactory.getCurrentComparatorID() + 1);
	}
}
