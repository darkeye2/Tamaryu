package com.tr.engine.grf.gl;

import java.util.ArrayList;

import com.tr.engine.grf.TRLabel;
import com.tr.gl.core.text.BitmapFont;
import com.tr.gl.core.text.BitmapFontManager;
import com.tr.gl.core.text.Glyph;
import com.tr.gl.core.text.GlyphLine;
import com.tr.gl.core.text.GlyphWord;

public class TRGLLabel extends TRGL2DRenderable implements TRLabel {
	private String text = "";
	private int alignment = TRLabel.LEFT;
	private float fontSize = 20;
	private float width, height;
	private ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
	private BitmapFont font = BitmapFontManager.load("Arial");
	
	public TRGLLabel(){
		this.setNormalized(false);
	}
	
	public TRGLLabel(String txt){
		this();
		setText(txt);
	}

	@Override
	public void setText(String txt) {
		this.text = txt;
		this.updateText();
	}

	private void updateText() {
		// list for all lines
		ArrayList<GlyphLine> glyphLines = new ArrayList<GlyphLine>();

		// split string into lines
		String tmp = text.replaceAll("\r\n", "\r");
		tmp = tmp.replaceAll("\n", "\r");
		String[] lines = tmp.split("\r");

		// build lines
		float y = 0;
		if (this.width < 1) {
			// no line width set, take longest line
			for (String line : lines) {
				// create glyphs for the line
				GlyphLine gl = new GlyphLine(line, font);
				
				//set width to maximum line width
				this.width = Math.max(gl.lineWidth, this.width);
				
				//add line
				gl.setYOffset(y);
				glyphLines.add(gl);
				
				//update y;
				y += font.getLineHeight();
			}
		}else{
			//max width set, calculate lines
			for (String line : lines) {
				// create glyphs for the line
				GlyphLine gl = new GlyphLine();
				
				// words
				String[] words = line.split(" ");
				
				for(int i = 0; i<words.length; i++){
					while(gl.lineWidth < this.width){
						GlyphWord gw = null;
						if(i < words.length-1){
							gw = new GlyphWord(words[i]+" ", font);
						}else{
							gw = new GlyphWord(words[i], font);
						}
						if(gl.lineWidth+gw.wordWidth <= this.width || gl.lineWidth == 0){
							gl.addWord(gw);
						}
					}
					
					//add line
					gl.setYOffset(y);
					glyphLines.add(gl);
					
					//update y;
					y += font.getLineHeight();
					
					gl = new GlyphLine();
				}
			}
			
		}

		if (this.height < 1) {
			this.height = glyphLines.size() * font.getLineHeight();
		}
		
		if(this.alignment == TRLabel.CENTER){
			for(GlyphLine gl : glyphLines){
				float offset = this.width - gl.lineWidth;
				gl.setXOffset(offset/2);
			}
		}else if(this.alignment == TRLabel.RIGHT){
			for(GlyphLine gl : glyphLines){
				float offset = this.width - gl.lineWidth;
				gl.setXOffset(offset);
			}
		}
		
		for(GlyphLine gl : glyphLines){
			this.glyphs.addAll(gl.getGlyphs());
		}
	}
	
	private void updateData(){
		//create array for vertex data (amount of glyphs * points per glyph * floats per point)
		float[] data = new float[this.glyphs.size()*6*5];
		
		//create vertex and texture data
		for(int i = 0; i<glyphs.size(); i++){
			Glyph g = glyphs.get(i);
			float[] texCoord = getNormalizedTexCoord(g);
			int di = i*6*5;
			data[di] = (g.x+g.gd.width)/this.width;
			data[di+1] = (g.y+g.gd.height)/this.height;
			data[di+2] = 0;
			data[di+3] = texCoord[2];
			data[di+3] = texCoord[3];
			data[di] = (g.x+g.gd.width)/this.width;
			data[di+1] = (g.y+g.gd.height)/this.height;
			data[di+2] = 0;
			data[di+3] = texCoord[0];
			data[di+3] = texCoord[3];
		}
	}
	
	private float[] getNormalizedTexCoord(Glyph g){
		float x1 = g.gd.x/font.getWScale();
		float y1 = 1 - (g.gd.y/font.getHScale());
		float x2 = (g.gd.x+g.gd.width)/font.getWScale();
		float y2 = 1 - ((g.gd.y+g.gd.height)/font.getHScale());
		
		return new float[]{x1,y1,x2,y2};
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		updateText();
	}

	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y, 0);
	}

	@Override
	public void setAlignment(int a) {
		this.alignment = a;
		updateText();
	}

	@Override
	public void setFont(String fontName, boolean resource) {
		if (resource) {
			this.font = BitmapFontManager.load(fontName);
		} else {
			String rawName = fontName.replace(".fnt", "");
			this.font = BitmapFontManager.load(rawName + ".fnt", rawName + ".png", false);
		}
		this.fontSize = this.font.getSize();
	}

	@Override
	public void setFontSize(float size) {
		this.fontSize = size;
	}

}
