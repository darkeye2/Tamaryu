package com.tr.engine.gameobject;

import com.tr.engine.grf.IRenderable;

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
	private IRenderable image;
	
	private TRRectangle hitboxHead;
	private TRRectangle hitboxBody;

	public Actor(int x, int y, IRenderable image)
	{
		super(x, y, 0, 0);	
		this.image = image;
		this.velocityX = 0;
		this.velocityY = 0;
	}
	
	/**
	 * 
	 * Methods for Image
	 * 
	 */
	
	public void setImage(IRenderable image)
	{
		this.image = image;
	}
	
	public IRenderable getImage()
	{
		return this.image;
	}
	
	@Override
	public void update(long ct) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * Methods for HitboxHead
	 * 
	 */
	
	public void setHitboxHead(int relativeX, int relativeY, int width, int height)
	{
		this.hitboxHead = new TRRectangle(relativeX, relativeY, width, height);
	}
	
	public void setHitboxHead(TRRectangle rect)
	{
		this.hitboxHead = rect;
	}
	
	public TRRectangle getHitboxHead()
	{
		return new TRRectangle(this.x + this.hitboxHead.x, this.y + this.hitboxHead.y, this.hitboxHead.width, this.hitboxHead.height);
	}
	
	/**
	 * 
	 * Methods for HitboxBody
	 * 
	 */
	
	public void setHitboxBody(int x, int y, int width, int height)
	{
		this.hitboxBody = new TRRectangle(x, y, width, height);
	}
	
	public void setHitboxBody(TRRectangle rect)
	{
		this.hitboxBody = rect;
	}
	
	public TRRectangle getHitboxBody()
	{
		return new TRRectangle(this.x + this.hitboxBody.x, this.y + this.hitboxBody.y, this.hitboxBody.width, this.hitboxBody.height);
	}

	
}
