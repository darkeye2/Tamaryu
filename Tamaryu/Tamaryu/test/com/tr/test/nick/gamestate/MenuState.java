package com.tr.test.nick.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.tr.util.CenteringText;
import com.tr.util.LanguageTranslator;
import com.tr.test.nick.main.GameWindow;
import com.tr.test.nick.tilemap.Background;

public class MenuState extends AbstractGameState
{
	private Background background;
	private int currentChoice = 0;
	
	private String[] options = {
			"start",
			"language",
			"help",
			"inventory",
			"soundtest",
			"",
			"quit"
		};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font regularFont;
	
	public MenuState(GameStateManager gsm)
	{
		this.gsm = gsm;
		
		try
		{
			this.background = new Background("/img/savanna_bg_day.png", 1);
			this.background.setVector(5.0, -2.0);
			
			this.titleColor = new Color(128, 0, 0);
			this.titleFont = new Font("Century Gothic", Font.PLAIN, 28);
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
		background.update();
	}

	@Override
	public void draw(Graphics2D g)
	{
		background.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		
		g.drawString(LanguageTranslator.getString("tamaryu"), CenteringText.getCenter(GameWindow.scaledWidth, GameWindow.scaledHeight, g, titleFont, LanguageTranslator.getString("tamaryu")), 70);
		
		//draw Menu Options
		g.setFont(regularFont);
		for(int i = 0; i < options.length; i++)
		{
			if(i == currentChoice)
			{
				g.setColor(Color.WHITE);
			}
			else
			{
				g.setColor(Color.RED);
			}
			
			g.drawString(LanguageTranslator.getString(options[i]), CenteringText.getCenter(GameWindow.scaledWidth, GameWindow.scaledHeight, g, regularFont, LanguageTranslator.getString(options[i])), (140 + (i * 15)));
		}
	}
	
	private void select()
	{
		if(currentChoice == 0)
		{
			//Start
			gsm.setState(GameStateManager.TESTSTATE);
		}
		if(currentChoice == 1)
		{
			//Language
			gsm.setState(GameStateManager.LANGUAGESTATE);
		}
		if (currentChoice == 2)
		{
			//Help
		}
		if (currentChoice == 3)
		{
			//Inventory
			gsm.setState(GameStateManager.INVENTORYTESTSTATE);
		}
		if (currentChoice == 4)
		{
			//Soundtest
			gsm.setState(GameStateManager.SOUNDTESTSTATE);
		}
		if(currentChoice == (options.length-1))
		{
			System.exit(0);
		}
	}
	
	@Override
	public void keyPressed(int k)
	{
		if (k == KeyEvent.VK_ENTER)
		{
			select();
		}
		if (k == KeyEvent.VK_UP)
		{
			currentChoice--;
			if(currentChoice < 0)
			{
				currentChoice = (options.length - 1);
			}
			//Empty Space
			if(currentChoice == (options.length - 2))
			{
				currentChoice = (options.length - 3);
			}
		}
		if (k == KeyEvent.VK_DOWN)
		{
			currentChoice++;
			if(currentChoice >= options.length)
			{
				currentChoice = 0;
			}
			//Empty Space
			if(currentChoice == (options.length - 2))
			{
				currentChoice = (options.length - 1);
			}
		}
	}

	@Override
	public void keyReleased(int k)
	{
		// TODO Auto-generated method stub
		
	}
}
