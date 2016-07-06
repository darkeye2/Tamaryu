package com.tr.test.nick.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


import com.tr.engine.inventory.InventoryItem;
import com.tr.engine.inventory.InventorySystem;
import com.tr.test.nick.main.GameWindow;
import com.tr.test.nick.tilemap.Background;
import com.tr.util.CenteringText;

public class InventoryTestState extends AbstractGameState
{	
	private Background background;

	private Font regularFont;
	
	private ArrayList<String> listOfItems;
	
	public InventoryTestState(GameStateManager gsm)
	{
		this.gsm = gsm;		
		this.listOfItems = new ArrayList<String>();
		
		try
		{
			this.background = new Background("/img/savanna_bg_evening.png", 1);
			this.background.setVector(0.0, 0.0);
			
			this.regularFont = new Font("Arial", Font.PLAIN, 12);
			
			for(InventoryItem item : InventorySystem.getInventory())
			{
				this.listOfItems.add(item.getName() + " " + item.getType() + " " + item.getAmount());
			}			
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
		
		//draw Menu Options
		g.setFont(regularFont);
		for(int i = 0; i < listOfItems.size(); i++)
		{
			g.setColor(Color.WHITE);
			g.drawString(listOfItems.get(i), CenteringText.getCenter(GameWindow.scaledWidth, GameWindow.scaledHeight, g, regularFont, listOfItems.get(i)), (140 + (i * 15)));
		}
	}

	@Override
	public void keyPressed(int k)
	{
		if (k == KeyEvent.VK_UP)
		{
			InventorySystem.addItem(new InventoryItem("Apfel", "Obst", 1));
			
			this.listOfItems.clear();
			for(InventoryItem item : InventorySystem.getInventory())
			{
				this.listOfItems.add(item.getName() + " " + item.getType() + " " + item.getAmount());
			}
		}
		
		if (k == KeyEvent.VK_DOWN)
		{
			InventorySystem.useItem(new InventoryItem("Apfel", "Obst", 1));
			
			this.listOfItems.clear();
			for(InventoryItem item : InventorySystem.getInventory())
			{
				this.listOfItems.add(item.getName() + " " + item.getType() + " " + item.getAmount());
			}
		}
		
		if (k == KeyEvent.VK_LEFT)
		{
			InventorySystem.addItem(new InventoryItem("Birne", "Obst", 1));
			
			this.listOfItems.clear();
			for(InventoryItem item : InventorySystem.getInventory())
			{
				this.listOfItems.add(item.getName() + " " + item.getType() + " " + item.getAmount());
			}
		}
		
		if (k == KeyEvent.VK_RIGHT)
		{
			InventorySystem.addItem(new InventoryItem("Karrote", "Gemüse", 1));
			
			this.listOfItems.clear();
			for(InventoryItem item : InventorySystem.getInventory())
			{
				this.listOfItems.add(item.getName() + " " + item.getType() + " " + item.getAmount());
			}
		}
	}

	@Override
	public void keyReleased(int k)
	{
		
	}

}
