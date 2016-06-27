package com.tr.test.nick.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.tr.engine.gameobject.Actor;
import com.tr.engine.gameobject.Zone;
import com.tr.img.gameobject.TRLayerdImage;
import com.tr.test.nick.tilemap.Background;

public class TestState extends AbstractGameState
{	
	private Background background;
	
	private Actor dragonPC;
	
	private Zone testObject;
	
	public TestState(GameStateManager gsm)
	{
		this.gsm = gsm;
		
		try
		{
			this.background = new Background("/img/savanna_bg_evening.png", 1);
			this.background.setVector(0.0, 0.0);
			
			this.testObject = new Zone(200, 200, 40, 60);
			
			/*
			this.dragonPC = new Actor(200, 200, new TRLayerdImage(null, 0, null));
			this.dragonPC.setHitboxHead(relativeX, relativeY, width, height);
			this.dragonPC.setHitboxBody(relativeX, relativeY, width, height);
			*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void init()
	{
		
	}

	@Override
	public void update()
	{
		this.background.update();
		this.testObject.update();
	}

	@Override
	public void draw(Graphics2D g)
	{
		this.background.draw(g);
		
		g.setColor(Color.RED);
		g.draw(this.testObject.getHitbox());
	}

	@Override
	public void keyPressed(int k)
	{
		if (k == KeyEvent.VK_UP)
		{
			this.testObject.setVelocityY(this.testObject.getVelocityY()-1);
		}
		
		if (k == KeyEvent.VK_DOWN)
		{
			this.testObject.setVelocityY(this.testObject.getVelocityY()+1);
		}
		
		if (k == KeyEvent.VK_LEFT)
		{
			this.testObject.setVelocityX(this.testObject.getVelocityX()-1);
		}
		
		if (k == KeyEvent.VK_RIGHT)
		{
			this.testObject.setVelocityX(this.testObject.getVelocityX()+1);
		}
	}

	@Override
	public void keyReleased(int k)
	{
		
	}

}
