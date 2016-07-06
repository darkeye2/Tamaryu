package com.tr.engine.gameobject;

public class TRRectangle
{
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	
	/**
	 * 
	 * Constructor
	 * 
	 */
	public TRRectangle()
	{
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}
		
	public TRRectangle(int width, int height)
	{
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
	}
	
	public TRRectangle(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public TRRectangle(TRRectangle rect)
	{
		this.x = 0;
		this.y = 0;
		this.width = rect.getWidth();
		this.height = rect.getHeight();
	}
	
	public TRRectangle(int x, int y, TRRectangle rect)
	{
		this.x = x;
		this.y = y;
		this.width = rect.getWidth();
		this.height = rect.getHeight();
	}
	
	/**
	 * 
	 * Getter
	 * 
	 */
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getSize()
	{
		return (this.width * this.height);
	}
	
	/**
	 * 
	 * Setter
	 * 
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/**
	 * 
	 * Methods
	 * 
	 */
	
	public boolean contains(int x, int y)
	{
		return !(x < this.x || y < this.y || x > (this.x + this.width) || y > (this.y + this.height));
	}
	
	public boolean contains(TRRectangle rect)
	{		
		return !((contains(rect.getX(), rect.getY())) || (rect.getX() + rect.getWidth()) > (this.x + this.width) || (rect.getY() + rect.getHeight()) > (this.y + this.height));
	}
	
	public boolean intersects(TRRectangle rect)
	{
		return((rect.getX() + rect.getWidth()) > this.x && (rect.getX() < (this.x + this.width)) && (rect.getY() + rect.getHeight()) > this.y && (rect.getY() < (this.y + this.height)));
	}
}
