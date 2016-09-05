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
	
	public void update(long ct)
	{
		this.x += this.velocityX;
		this.y += this.velocityY;
		
		if(this.x <= 0)
		{
			this.x = 0;
		}
		
		if(this.y <= 0)
		{
			this.y = 0;
		}
		
		if(this.x + width >= GameWindow.scaledWidth)
		{
			this.x = GameWindow.scaledWidth - width;
		}
		
		if(this.y + height >= GameWindow.scaledHeight)
		{
			this.y = GameWindow.scaledHeight - height;
		}
	}
	
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
