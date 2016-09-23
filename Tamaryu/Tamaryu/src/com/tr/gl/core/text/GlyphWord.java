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
		for(int i = 0; i < chars.length; i++){
			addGlyph(chars[i], ((i+1)<chars.length)?chars[i+1]:-1, font);
		}
	}
	
	public void addGlyph(int c, int c2, BitmapFont font){
		Glyph g = new Glyph();
		g.gd = font.getGlyphData(c);
		x += g.gd.xoffset;			//add offset
		g.x = x;					//set image pos
		g.y = y + g.gd.yoffset;		//set image pos for y (with offset)
		x += g.gd.xadvance;			//add width to x pos in the word
		
		//check for kerning
		if(c2 < -1){
			x += font.getKerning(c, c2);
		}
		
		wordWidth = x;
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
