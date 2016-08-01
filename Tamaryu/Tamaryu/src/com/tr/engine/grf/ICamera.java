package com.tr.engine.grf;


public interface ICamera {
	
	public void setWinSize(int w, int h);
	public float getWinWidth();
	public float getWinHeigth();

	public void setPosition(float x, float y, float z);

	public void setRotation(float xAng, float yAng, float zAng);

	public void increasePos(float x, float y, float z);

	public void increaseRot(float x, float y, float z);
	public void setReferenceSize(float sceneWidth, float sceneHeight);
}
