package com.tr.engine.gameobject;

/** 
 * @author Daimonius
 *
 * Zones are Invisible Collision Areas
 *
 */

public class Zone extends AbstractGameObject
{	
	public Zone(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		this.velocityX = 0;
		this.velocityY = 0;
	}
	
	/**
	 * 
	 * Methods for Hitbox
	 * 
	 */
	
	public TRRectangle getHitbox()
	{
		return new TRRectangle(this.x, this.y, this.width, this.height);
	}
}
