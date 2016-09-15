package com.tr.engine.input;

import com.jogamp.newt.event.KeyEvent;

public class TRKeyEvent {
	public KeyEvent e = null;
	public String eventPath = "";
	public boolean artifical = false;
	
	public TRKeyEvent(KeyEvent e, String ep){
		this.e = e;
		this.eventPath = ep;
	}
	
	public TRKeyEvent(KeyEvent e){
		this(e, "");
	}
}
