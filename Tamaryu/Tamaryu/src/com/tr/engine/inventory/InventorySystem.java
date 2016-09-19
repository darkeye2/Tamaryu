package com.tr.engine.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.tr.engine.obj.state.IGameStateEventListener;
import com.tr.engine.rewardsystem.ListenerForReward;
import com.tr.engine.rewardsystem.LootBox;

public final class InventorySystem
{
	public static ArrayList<IInventoryable> inventoryItems;
	private static Comparator<IInventoryable> comparator;
	private static IInventoryable newestItem;
	
	private static IInventoryEventListener[] arrayOfListeners = new IInventoryEventListener[]{ListenerForReward.getInstance()};
	
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
			IInventoryable item0 = new InventoryItem("apple", "fruit", InventoryItem.APPLE, 6);
			
			IInventoryable item1 = new InventoryItem("commonlootbox", "lootbox", InventoryItem.LOOTBOX_COMMON, 5);
			IInventoryable item2 = new InventoryItem("rarelootbox", "lootbox", InventoryItem.LOOTBOX_RARE, 4);
			IInventoryable item3 = new InventoryItem("premiumlootbox", "lootbox", InventoryItem.LOOTBOX_PREMIUM, 3);
			IInventoryable item4 = new InventoryItem("luxerylootbox", "lootbox", InventoryItem.LOOTBOX_LUXERY, 2);
			
			InventorySystem.newestItem = item4;
			
			InventorySystem.inventoryItems.add(item0);
			InventorySystem.inventoryItems.add(item1);
			InventorySystem.inventoryItems.add(item2);
			InventorySystem.inventoryItems.add(item3);
			InventorySystem.inventoryItems.add(item4);
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
			InventorySystem.newestItem = item;
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
					for(IInventoryEventListener listener : arrayOfListeners)
					{
						listener.notifyItemUse(entry.getPosition());
					}
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
	
	public static IInventoryable getNewestItem()
	{
		return InventorySystem.newestItem;
	}
}
