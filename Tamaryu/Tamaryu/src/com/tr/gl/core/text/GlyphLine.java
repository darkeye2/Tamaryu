package com.tr.gl.core.text;

import java.util.ArrayList;

public class GlyphLine {
	public float lineWidth = 0;
	private float x = 0, y = 0;
	private ArrayList<GlyphWord> words = new ArrayList<GlyphWord>();
	
	public GlyphLine(){
		
	}
	
	public GlyphLine(GlyphWord[] words){
		for(GlyphWord gw : words){
			addWord(gw);
		}
	}
	
	public GlyphLine(String line, BitmapFont font){
		String[] words = line.split(" ");
		for(int i = 0; i<words.length; i++){
			if(i < words.length-1){
				addWord(new GlyphWord(words[i], font));
			}else{
				addWord(new GlyphWord(words[i]+" ", font));
			}
			
		}
	}
	
	public void addWord(GlyphWord gw){
		gw.setXOffset(x);
		x += gw.wordWidth;
		lineWidth += gw.wordWidth;
		words.add(gw);
	}
	
	public void setYOffset(float y){
		this.y = y;
		for(GlyphWord g : words){
			g.setYOffset(this.y);
		}
	}
	
	public void setXOffset(float x){
		this.x = x;
		for(GlyphWord g : words){
			g.setXOffset(x);
		}
	}
	
	public ArrayList<Glyph> getGlyphs(){
		ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
		for(GlyphWord gw : words){
			glyphs.addAll(gw.getGlyphs());
		}
		return glyphs;
	}

}
