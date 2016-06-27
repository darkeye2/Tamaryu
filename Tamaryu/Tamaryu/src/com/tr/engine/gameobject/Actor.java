package com.tr.engine.gameobject;

import java.awt.Rectangle;

import com.tr.img.gameobject.TRLayerdImage;

/** 
 * @author Daimonius
 *
 * Actors can be controlled through Keylistener
 * which are used by the Player or an AI.
 * 
 *  Actors are visible through an Image.
 *  
 *  Actors do have a Collision
 *
 */

public class Actor extends AbstractGameObject
{
	private static final long serialVersionUID = -8035304360453003726L;

	private TRLayerdImage image;
	
	private Rectangle hitboxHead;
	private Rectangle hitboxBody;

	public Actor(int x, int y, TRLayerdImage image)
	{
		super(x, y, image.getSize().width, image.getSize().height);	
		this.image = image;
		this.velocityX = 0;
		this.velocityY = 0;
	}
	
	/**
	 * 
	 * Methods for Image
	 * 
	 */
	
	public void setImage(TRLayerdImage image)
	{
		this.image = image;
	}
	
	public TRLayerdImage getImage()
	{
		return this.image;
	}
	
	/**
	 * 
	 * Methods for HitboxHead
	 * 
	 */
	
	public void setHitboxHead(int relativeX, int relativeY, int width, int height)
	{
		this.hitboxHead = new Rectangle(relativeX, relativeY, width, height);
	}
	
	public void setHitboxHead(Rectangle rect)
	{
		this.hitboxHead = rect;
	}
	
	public Rectangle getHitboxHead()
	{
		return new Rectangle(this.x + this.hitboxHead.x, this.y + this.hitboxHead.y, this.hitboxHead.width, this.hitboxHead.height);
	}
	
	/**
	 * 
	 * Methods for HitboxBody
	 * 
	 */
	
	public void setHitboxBody(int x, int y, int width, int height)
	{
		this.hitboxBody = new Rectangle(x, y, width, height);
	}
	
	public void setHitboxBody(Rectangle rect)
	{
		this.hitboxBody = rect;
	}
	
	public Rectangle getHitboxBody()
	{
		return new Rectangle(this.x + this.hitboxBody.x, this.y + this.hitboxBody.y, this.hitboxBody.width, this.hitboxBody.height);
	}
}
