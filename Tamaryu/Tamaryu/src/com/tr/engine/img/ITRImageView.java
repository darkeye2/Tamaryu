package com.tr.engine.img;

import com.jogamp.nativewindow.util.Dimension;
import com.tr.engine.grf.IRenderable;
import com.tr.gl.core.Point3D;

public interface ITRImageView extends IRenderable{
	public void setImage(TRImage i);
	public void setSize(int w, int h);
	public Dimension getSize(Dimension d);
	public void setAnchor(int  x, int y, int z);
	public float getAnchorX();
	public float getAnchorY();
	public float getAnchorZ();
	public Point3D getAnchor();
}
