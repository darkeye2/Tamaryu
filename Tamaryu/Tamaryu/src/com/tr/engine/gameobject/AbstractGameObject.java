package com.tr.engine.gameobject;

import com.tr.test.nick.main.GameWindow;

public abstract class AbstractGameObject extends TRRectangle
{
	protected int GameObjectID;
	
	protected int velocityX;
	protected int velocityY;
	
	public AbstractGameObject(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		this.velocityX = 0;
		this.velocityY = 0;
	}
	
	public abstract void update(long ct);
	
	public int getGameObjectID()
	{
		return this.GameObjectID;
	}
	
	public int getVelocityX()
	{
		return this.velocityX;
	}
	
	public int getVelocityY()
	{
		return this.velocityY;
	}
	
	public void setVelocityX(int velX)
	{
		this.velocityX = velX;
	}
	
	public void setVelocityY(int velY)
	{
		this.velocityY = velY;
	}
}
