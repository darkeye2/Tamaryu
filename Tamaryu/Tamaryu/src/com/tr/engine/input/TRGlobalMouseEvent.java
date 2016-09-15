package com.tr.engine.input;

import com.jogamp.nativewindow.util.Point;
import com.jogamp.newt.event.MouseEvent;
import com.tr.engine.grf.IRenderable;

public class TRGlobalMouseEvent extends TRMouseEvent{

	public TRGlobalMouseEvent(MouseEvent e, Point p) {
		super(e, p);
	}
	
	public TRGlobalMouseEvent(TRMouseEvent me){
		super(me.e, me.lastPos);
		this.setTranslatedPos(me.x(), me.y());
		this.dra = me.dra;
		this.src = me.src;
		this.listening = me.listening;
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
