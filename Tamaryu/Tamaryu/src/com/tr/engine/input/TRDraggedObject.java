package com.tr.engine.input;

import com.jogamp.nativewindow.util.Point;
import com.tr.engine.grf.IRenderable;

public class TRDraggedObject {
	public Point startPos = new Point(0,0);
	public IRenderable r = null;
	
	public TRDraggedObject(TRGlobalMouseEvent e){
		r = e.getSource();
		startPos = new Point(e.x(), e.y());
	}
}
