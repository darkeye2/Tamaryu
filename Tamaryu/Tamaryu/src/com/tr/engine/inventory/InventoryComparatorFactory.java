package com.tr.engine.inventory;

import java.util.Comparator;

final class InventoryComparatorFactory
{
	private static int currentComparatorID = 0;
	
	private static final int NAME_COMPARATOR_ID = 3;
	private static final int TYPE_COMPARATOR_ID = 2;
	private static final int AMOUNT_COMPARATOR_ID = 1;
	private static final int DEFAULT_COMPARATOR_ID = 0;
	
	static Comparator<IInventoryable> createComparator(int comparatorID)
	{
		switch(comparatorID)
		{		
			case NAME_COMPARATOR_ID:
				currentComparatorID = NAME_COMPARATOR_ID; 
				return new InventoryNameComparator();
			
			case TYPE_COMPARATOR_ID:
				currentComparatorID = TYPE_COMPARATOR_ID;
				return new InventoryTypeComparator();
			
			case AMOUNT_COMPARATOR_ID:
				currentComparatorID = AMOUNT_COMPARATOR_ID;
				return new InventoryAmountComparator();
			
			case DEFAULT_COMPARATOR_ID:
			default:
				currentComparatorID = DEFAULT_COMPARATOR_ID;
				return new InventoryPositionComparator();
		}
	}
	
	public static int getCurrentComparatorID()
	{
		return currentComparatorID;
	}
}
