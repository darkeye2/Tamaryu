package com.tr.test.nick.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.tr.test.nick.main.GameWindow;

public class Background
{
	private BufferedImage image;
	private int imageWidth;
	private int imageHeight;
	
	private int imageEndX;
	private int imageEndY;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String filepath, double movescale)
	{
		try
		{
			this.image = ImageIO.read(getClass().getResourceAsStream(filepath));
			this.imageWidth = image.getWidth();
			this.imageHeight = image.getHeight();
			
			imageEndX = GameWindow.scaledWidth - imageWidth;
			imageEndY = GameWindow.scaledHeight - imageHeight;
			
			this.moveScale = movescale;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y)
	{
		this.x = (x * moveScale) % imageWidth;
		this.y = (y * moveScale) % imageHeight;
	}
	
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update()
	{
		this.x += this.dx;
		this.y += this.dy;
		
		if(this.x >= imageWidth)
		{
			this.x = 0;
		}
		
		if(this.y >= imageHeight)
		{
			this.y = 0;
		}
		
		if(this.x <= imageEndX)
		{
			this.x = GameWindow.scaledWidth;
		}
		
		if(this.y <= imageEndY)
		{
			this.y = GameWindow.scaledHeight;
		}
	}
	
	public void draw (Graphics2D g)
	{
		g.drawImage(this.image, (int)x, (int)y, null);
		if (this.x > 0 && this.y > 0)
		{
			g.drawImage(this.image, (int)(x - imageWidth), (int)(y - imageHeight), null);
		}
		if(this.x > 0)
		{
			g.drawImage(this.image, (int)(x - imageWidth), (int)y, null);
		}
		if(this.y > 0)
		{
			g.drawImage(this.image, (int)x, (int)(y - imageHeight), null);
		}
	}
}
