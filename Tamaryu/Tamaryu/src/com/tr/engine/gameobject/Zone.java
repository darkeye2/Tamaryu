package com.tr.engine.gameobject;

import java.awt.Rectangle;

/** 
 * @author Daimonius
 *
 * Zones are Invisible Collision Areas
 *
 */

public class Zone extends AbstractGameObject
{	
	private static final long serialVersionUID = 2057825191254260642L;

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
	
	public Rectangle getHitbox()
	{
		return new Rectangle(this.x, this.y, this.width, this.height);
	}
}
