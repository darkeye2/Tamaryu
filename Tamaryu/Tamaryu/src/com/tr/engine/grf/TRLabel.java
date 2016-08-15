package com.tr.engine.grf;

public interface TRLabel extends IRenderable{
	public static final int CENTER = 0;
	public static final int RIGHT = 1;
	public static final int LEFT = 2;
	
	public void setText(String txt);
	public String getText();
	public void setSize(int w, int h);
	public int getWidth();
	public int getHeight();
	public void setPosition(int x, int y);
	public void setAlignment(int a);
	public void setFont(String fontName, boolean resource);
	public void setFontSize(float size);
	public void setColor(Color c);
}
