package com.tr.engine.gameobject;

import com.tr.img.gameobject.TRLayerdImage;

/** 
 * @author Daimonius
 *
 * Decoration only represents an Image.
 * 
 * No GameObject interacts with a Decoration.
 *
 */

public class Decoration extends AbstractGameObject
{
	private static final long serialVersionUID = 4402244606590167628L;
	
	private TRLayerdImage image;

	public Decoration(int x, int y, TRLayerdImage image)
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
}
