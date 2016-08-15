package com.tr.engine.input;

import java.util.Comparator;

public class EventComparator implements Comparator<ITRMouseListener> {

	@Override
	public  int compare(ITRMouseListener arg0, ITRMouseListener arg1) {
		return arg0.getZ() - arg1.getZ();
	}

}
