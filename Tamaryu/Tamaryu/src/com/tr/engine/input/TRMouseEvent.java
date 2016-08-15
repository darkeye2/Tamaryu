package com.tr.engine.input;

import com.jogamp.nativewindow.util.Point;
import com.jogamp.newt.event.MouseEvent;

public class TRMouseEvent{
	private int transX = 0, transY = 0;
	public MouseEvent e = null;
	public Point lastPos = new  Point(0,0);

	public TRMouseEvent(MouseEvent e, Point p){
		this.e = e;
		lastPos = p;
	}
	
	public int x(){
		return transX;
	}
	
	public int y(){
		return transY;
	}
	
	public void setTranslatedPos(int x, int  y){
		transX = x;
		transY = y;
	}

}
