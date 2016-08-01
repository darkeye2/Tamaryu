package com.tr.engine.grf;

import com.tr.gl.core.Point3D;

public interface IRenderable extends Comparable<IRenderable>{
	public void init(TRRenderContext context);
	public void render(TRRenderContext context);
	public void resize(TRRenderContext context, int w, int h);
	public void destruct(TRRenderContext context);
	public void setPosition(float x, float y, float z);
	public Point3D getPosition();
	public void setX(float x);
	public void setY(float y);
	public void setZ(float z);
	public void increasePos(float x, float  y, float z);
	public void setRotation(float xAng, float yAng, float zAng);
	public void setXRotation(float ang);
	public void setYRotation(float ang);
	public void setZRotation(float ang);
	public void increaseRot(float x, float  y, float z);
	public void addComponent(IRenderable c);
	public IRenderable removeComponent(IRenderable c);
	public void removeAll();
	public void setRenderPropertie(TRRenderPropertie p);
}
