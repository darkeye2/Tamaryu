package com.tr.engine.input;

import com.jogamp.nativewindow.util.Point;
import com.jogamp.newt.event.MouseEvent;
import com.tr.engine.grf.IRenderable;

public class TRGlobalMouseEvent extends TRMouseEvent{
	
	private IRenderable src = null;

	public TRGlobalMouseEvent(MouseEvent e, Point p) {
		super(e, p);
	}
	
	public IRenderable getSource(){
		if(src != null)
			return src;
		
		return null;
	}
	
	public void setSource(ITRMouseListener l){
		if(l instanceof IRenderable){
			this.src = (IRenderable) l;
		}
	}
	
}
