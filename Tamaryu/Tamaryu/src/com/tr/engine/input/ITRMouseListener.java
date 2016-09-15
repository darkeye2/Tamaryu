package com.tr.engine.input;

import com.tr.engine.grf.IRenderable;

public interface ITRMouseListener  {
	public void mouseEnter(TRMouseEvent e);
	public void mouseLeave(TRMouseEvent e);
	public void mouseRelease(TRMouseEvent e);
	public void mousePress(TRMouseEvent e);
	public void mouseDragged(TRMouseEvent tre);
	public void mouseMoved(TRMouseEvent tre);
	public void mouseClicked(TRMouseEvent e);
	
	public int getZ();
	
	public IRenderable getSrc();
}
