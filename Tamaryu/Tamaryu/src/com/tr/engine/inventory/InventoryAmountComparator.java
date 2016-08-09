package com.tr.engine.inventory;

class InventoryAmountComparator extends AbstractInventoryComparator
{
	InventoryAmountComparator()
	{
		this.comparatorName = "AmountComparator";
	}
	
	@Override
	public int compare(IInventoryable o1, IInventoryable o2)
	{
		if(o1.getAmount() < o2.getAmount())
		{
			return -1;
		}
		if (o1.getAmount() > o2.getAmount())
		{
			return 1;
		}
		int temp = o1.getType().compareToIgnoreCase(o2.getType());
		if(temp != 0)
		{
			return temp;
		}
		temp = o1.getName().compareToIgnoreCase(o2.getName());
		if(temp != 0)
		{
			return temp;
		}
		if(o1.getPosition() < o2.getPosition())
		{
			return -1;
		}
		if(o1.getPosition() > o2.getPosition())
		{
			return 1;
		}
		return 0;
	}
}
