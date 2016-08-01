package com.tr.engine.grf.gl;

import com.jogamp.nativewindow.util.Dimension;
import com.tr.engine.img.ITRImageView;
import com.tr.engine.img.TRImage;
import com.tr.gl.core.Point3D;

public class TRGLImageView extends TRGL2DRenderable implements ITRImageView {
	
	protected Point3D anchor = new Point3D(0,0,0);

	@Override
	public void setImage(TRImage i) {
		// TODO request texture from texturemanager and set it for this view

	}

	@Override
	public void setSize(int w, int h) {
		// TODO Auto-generated method stub

	}

	@Override
	public Dimension getSize(Dimension d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAnchor(int x, int y, int z) {
		this.anchor = new Point3D(x,y,z);
	}

	@Override
	public float getAnchorX() {
		return anchor.x;
	}

	@Override
	public float getAnchorY() {
		return anchor.y;
	}

	@Override
	public float getAnchorZ() {
		return anchor.z;
	}

	@Override
	public Point3D getAnchor() {
		return anchor;
	}

}
