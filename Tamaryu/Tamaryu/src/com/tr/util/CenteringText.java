package com.tr.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class CenteringText
{	
	public static int getCenter(int windowWidth, int windowHeight, Graphics g, Font font, String text)
	{
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = (windowWidth - metrics.stringWidth(text)) / 2;
	    
	    return x;
	}
}
