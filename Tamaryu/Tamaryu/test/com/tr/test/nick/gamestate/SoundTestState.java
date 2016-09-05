package com.tr.test.nick.gamestate;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.tr.test.nick.main.GameWindow;
import com.tr.test.nick.tilemap.Background;
import com.tr.util.CenteringText;

import com.tr.engine.sound.AudioMaster;

public class SoundTestState extends AbstractGameState
{	
	private Background background;
	private Font regularFont;
			
	public SoundTestState(GameStateManager gsm)
	{
		this.gsm = gsm;		
				
		try
		{
			this.background = new Background("/img/savanna_bg_evening.png", 1);
			this.background.setVector(0.0, 0.0);
			
			this.regularFont = new Font("Arial", Font.PLAIN, 12);			
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
	}

	@Override
	public void draw(Graphics2D g)
	{
		this.background.draw(g);
		
		g.setFont(regularFont);
		
		g.drawString("< LoadSound, PlaySound >", CenteringText.getCenter(GameWindow.scaledWidth, GameWindow.scaledHeight, g, regularFont, "< LoadBuffer, v CreateSource v, PlaySound >"), 70);
	}

	@Override
	public void keyPressed(int k)
	{
		if (k == KeyEvent.VK_UP)
		{
			AudioMaster.clearData();
			System.out.println("Sound geleert");
		}
		
		if (k == KeyEvent.VK_DOWN)
		{
			
		}
		
		if (k == KeyEvent.VK_LEFT)
		{
			String[] audios = new String[1];
			audios[0] =  "res/sound/GitarrenLoop.wav";
			
			AudioMaster.loadAudioFiles(audios);
			System.out.println("Sound geladen");
		}
		
		if (k == KeyEvent.VK_RIGHT)
		{
			AudioMaster.playSource(0);
			System.out.println("Sound play");
		}
	}

	@Override
	public void keyReleased(int k)
	{
		
	}
}