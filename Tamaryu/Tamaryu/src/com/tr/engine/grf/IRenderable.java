package com.tr.engine.grf;

import com.jogamp.opengl.util.packrect.Rect;
import com.tr.gl.core.Point3D;

public interface IRenderable extends Comparable<IRenderable>{
	public final static int FIXED_POS_NONE = -1;
	public final static int FIXED_POS_CENTER = 0x00;
	public final static int FIXED_POS_TOP = 0x01;
	public final static int FIXED_POS_TOP_LEFT = 0x02;
	public final static int FIXED_POS_TOP_RIGHT = 0x03;
	public final static int FIXED_POS_RIGHT = 0x04;
	public final static int FIXED_POS_BOTTOM = 0x05;
	public final static int FIXED_POS_BOTTOM_LEFT = 0x06;
	public final static int FIXED_POS_BOTTOM_RIGHT = 0x07;
	public final static int FIXED_POS_LEFT = 0x08;
	
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
	public void setScale(float s);
	public float getScale();
	public void setSize(int w, int h);
	public int getWidth();
	public int getHeight();
	public float getOverallScale();
	public void increaseRot(float x, float  y, float z);
	public void addComponent(IRenderable c);
	public IRenderable removeComponent(IRenderable c);
	public void removeAll();
	public void setRenderPropertie(TRRenderPropertie p);
	public Point3D getAbsolutPosition();
	public IRenderable getParent();
	
	public void setFixedPosition(int posConstant);
	
	public Rect getHitbox();
	public void setHitbox(Rect r);
	public boolean isHit(float x, float y);
	public void propagateHit(String hitPath);
	public boolean ignore();
	public void setIgnore(boolean b);
	public void setClipping(boolean b);
	public Rect getClip(TRScene s);
	
	public void setName(String name);
	public String getName();
	public void setID(int id);
	public int getID();
	public IRenderable getComponentByName(String nn);
	public IRenderable getComponentByID(String idid);
}
