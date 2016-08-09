package com.tr.engine.inventory;

class InventoryPositionComparator extends AbstractInventoryComparator
{
	InventoryPositionComparator()
	{
		this.comparatorName = "DefaultComparator";
	}
	
	@Override
	public int compare(IInventoryable o1, IInventoryable o2)
	{
		if(o1.getPosition() < o2.getPosition())
		{
			return -1;
		}
		if (o1.getPosition() > o2.getPosition())
		{
			return 1;
		}
		return 0;
	}
}
