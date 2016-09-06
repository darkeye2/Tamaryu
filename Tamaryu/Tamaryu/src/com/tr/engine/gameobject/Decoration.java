package com.tr.engine.gameobject;

import com.tr.engine.grf.IRenderable;

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
	private IRenderable image;

	public Decoration(int x, int y, IRenderable image)
	{
		super(x, y, image!=null?image.getWidth():0, image!=null?image.getHeight():0);	
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
}
