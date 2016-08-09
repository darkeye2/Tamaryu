package com.tr.engine.inventory;

import java.util.Comparator;

abstract class AbstractInventoryComparator implements Comparator<IInventoryable>
{
	protected String comparatorName = "";
	
	String getName()
	{
		return comparatorName;
	}
}
