package com.tr.engine.components;

import com.tr.engine.input.ITRMouseListener;

public interface TRTextButton extends TRLabel, ITRMouseListener {
	public static final int MOUSE_ENTER_ACTION = 0;
	public static final int MOUSE_LEAVE_ACTION = 1;
	public static final int MOUSE_DOWN_ACTION = 2;
	public static final int MOUSE_UP_ACTION = 3;
	
	public static final int STATE_COUNT = 4;
	
	public void addClickAction(Runnable r);
	public void addStateChangeAction(int state, Runnable r);
}
