package com.tr.engine.gameobject;

import com.tr.img.gameobject.TRLayerdImage;

/** 
 * @author Daimonius
 *
 * Props represent a GameObject with Collision and an Image.
 * Props are never controlled by Player or AI
 *
 */

public class Prop extends AbstractGameObject
{	
	private TRLayerdImage image;
	private TRRectangle hitbox;
	
	public Prop(int x, int y, TRLayerdImage image)
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
	 * Methods for Hitbox
	 * 
	 */
	
	public void setHitbox(int relativeX, int relativeY, int width, int height)
	{
		this.hitbox = new TRRectangle(relativeX, relativeY, width, height);
	}
	
	public void setHitbox(TRRectangle rect)
	{
		this.hitbox = rect;
	}
	
	public TRRectangle getHitbox()
	{
		return new TRRectangle(this.x + this.hitbox.x, this.y + this.hitbox.y, this.hitbox.width, this.hitbox.height);
	}

	@Override
	public void update(long ct) {
		// TODO Auto-generated method stub
		
	}
}
