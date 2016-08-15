package com.tr.gl.core.text;

import java.util.ArrayList;

public class GlyphWord {
	public float wordWidth = 0;
	private float x = 0, y = 0;
	private ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
	
	public GlyphWord(){
		
	}
	
	public GlyphWord(String word, BitmapFont font){
		char[] chars = word.toCharArray();
		for(char c : chars){
			addGlyph(c, font);
		}
	}
	
	public void addGlyph(int c, BitmapFont font){
		Glyph g = new Glyph();
		g.gd = font.getGlyphData(c);
		g.x = x + g.gd.xoffset;
		g.y = y + g.gd.yoffset;
		x += g.gd.width;
		wordWidth += g.gd.width;
		glyphs.add(g);
	}
	
	public void setYOffset(float y){
		this.y = y;
		for(Glyph g : glyphs){
			g.y += y; 
		}
	}
	
	public void setXOffset(float x){
		this.x = x;
		for(Glyph g : glyphs){
			g.x += x; 
		}
	}
	
	public ArrayList<Glyph> getGlyphs(){
		return glyphs;
	}

}
