package com.tr.engine.grf;

import java.awt.Graphics;

public interface ITRBackground{
	
	//can be left empty if not animated background
	public void start();
	public void pause();
	public void stop();
	
	//can be left empty if not day/night changes (hh {0-24}, mm {0-59}, ss {0-59})
	public void setTime(int hh, int mm, int ss);
	
	//
	public void setScale(float f);
	
	//rendering methods (if extending component, no overwrite necessary
	public void paintComponent(Graphics g);
	
}
