package com.tr.engine.input;

import com.jogamp.opengl.util.packrect.Rect;
import com.tr.engine.grf.IRenderable;

public interface ITRMouseListener  {
	public void mouseEnter(TRMouseEvent e);
	public void mouseLeave(TRMouseEvent e);
	public void mouseRelease(TRMouseEvent e);
	public void mousePress(TRMouseEvent e);
	public void mouseDragged(TRMouseEvent tre);
	public void mouseMoved(TRMouseEvent tre);
	
	public Rect getHitbox();
	public void setHitbox(Rect hitbox);
	public int getZ();
	
	public boolean isHit(int  x, int y);
	public IRenderable getSrc();
}
