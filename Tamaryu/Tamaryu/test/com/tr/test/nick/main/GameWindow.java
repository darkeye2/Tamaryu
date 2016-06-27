package com.tr.test.nick.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.tr.test.nick.gamestate.GameStateManager;

public class GameWindow extends JPanel implements Runnable, KeyListener
{
	private static final long serialVersionUID = -6370606734462068811L;
	// Game Classes
	private GameLooper gameLooper;
	private GameStateManager gameStateManager;
	
	// Dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	public static int scaledWidth = WIDTH * SCALE;
	public static int scaledHeight = HEIGHT * SCALE;
	
	// Thread
	private Thread thread;
	
	// Image
	private BufferedImage image;
	private Graphics2D g;
	
	public GameWindow()
	{
		super();
		setPreferredSize(new Dimension(scaledWidth, scaledHeight));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void update()
	{
		gameStateManager.update();
	}

	private void draw()
	{
		gameStateManager.draw(g);
	}
	
	private void drawToScreen()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
		g2.dispose();
	}
	
	private void init()
	{
		image = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		this.gameLooper = new GameLooper();
		this.gameStateManager = new GameStateManager();
	}
	
	public void run()
	{
		init();
		long startTimeStamp;
		
		while(gameLooper.getRunning())
		{
			startTimeStamp = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			gameLooper.think(startTimeStamp);
		}
	}
	
	//Key Listener
	@Override
	public void keyPressed(KeyEvent key)
	{
		gameStateManager.keyPressed(key.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent key)
	{
		gameStateManager.keyReleased(key.getKeyCode());	
	}

	@Override
	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub
		
	}
}
