package com.tr.engine.core;

public class GameLooper
{
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	private long elapsed;
	private long wait;
	
	private boolean running;
	
	public boolean getRunning()
	{
		return this.running;
	}
	
	public GameLooper()
	{
		this.running = true;
	}
	
	public void think(long startTimeStamp)
	{		
		elapsed = System.nanoTime() - startTimeStamp;
		wait = targetTime - (elapsed / 1000000);
		
		try
		{
			if(wait > 0)
			{
				Thread.sleep(wait);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}