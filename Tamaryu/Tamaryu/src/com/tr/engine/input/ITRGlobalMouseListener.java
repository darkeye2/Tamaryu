package com.tr.engine.input;

public interface ITRGlobalMouseListener {
	public void mouseEnter(TRGlobalMouseEvent e);
	public void mouseLeave(TRGlobalMouseEvent e);
	public void mouseRelease(TRGlobalMouseEvent e);
	public void mousePress(TRGlobalMouseEvent e);
	public void mouseDragged(TRGlobalMouseEvent e);	
	public void mouseMoved(TRGlobalMouseEvent tre);
	public void mouseClicked(TRGlobalMouseEvent e);
}
