package com.tr.engine.components.gl;

import java.util.ArrayList;

import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.util.packrect.Rect;
import com.tr.engine.components.TRLabel;
import com.tr.engine.grf.gl.TRGL2DRenderable;
import com.tr.gl.core.GLProgramm;
import com.tr.gl.core.text.BitmapFont;
import com.tr.gl.core.text.BitmapFontManager;
import com.tr.gl.core.text.Glyph;
import com.tr.gl.core.text.GlyphLine;
import com.tr.gl.core.text.GlyphWord;

public class TRGLLabel extends TRGL2DRenderable implements TRLabel{
	private String text = "";
	private int alignment = TRLabel.LEFT;
	private float fontSize = 20;
	//private float width, height;
	private float maxW = 0, maxH = 0;
	protected Rect hitbox = new Rect();
	private ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
	private BitmapFont font = BitmapFontManager.load("Arial");
	
	public TRGLLabel(){
		this.setNormalized(false);
		this.setTexture(font.getTexture());
		this.setProgram(new GLProgramm("/shader/", new String[]{"default_f", "default_v"},
				new int[]{GL2ES3.GL_FRAGMENT_SHADER, GL2ES3.GL_VERTEX_SHADER}, "default"));
	}
	
	public TRGLLabel(String txt){
		this();
		setText(txt);
		System.out.println("TRGLLabel ["+width+", "+height+"] text="+text);
		//GLCamera.printFloatMatrix(this.data, 5, 6, true);
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
		if (this.maxW < 1) {
			this.width = 0;
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

		if (this.maxH < 1) {
			this.height = glyphLines.size() * font.getLineHeight();
		}
		
		if(this.alignment == TRLabel.CENTER){
			System.out.println("Center Glyphs!");
			for(GlyphLine gl : glyphLines){
				float offset =  0;
				if(this.maxW>0){
					offset = this.maxW - gl.lineWidth;
				}else{
					offset = this.width - gl.lineWidth;
				}
				gl.setXOffset(offset/2);
			}
		}else if(this.alignment == TRLabel.RIGHT){
			for(GlyphLine gl : glyphLines){
				float offset =  0;
				if(this.maxW>0){
					offset = this.maxW - gl.lineWidth;
				}else{
					offset = this.width - gl.lineWidth;
				}
				gl.setXOffset(offset);
			}
		}
		
		glyphs.clear();
		for(GlyphLine gl : glyphLines){
			this.glyphs.addAll(gl.getGlyphs());
		}
		
		updateData();
		hitbox.setSize(this.getWidth(), this.getHeight());
		System.out.println("Glyphs: "+glyphs.size());
	}
	
	private void updateData(){
		//create array for vertex data (amount of glyphs * points per glyph * floats per point)
		float[] data = new float[this.glyphs.size()*6*5];
		
		//create vertex and texture data
		for(int i = 0; i<glyphs.size(); i++){
			Glyph g = glyphs.get(i);
			float[] texCoord = getNormalizedTexCoord(g);
			float [] vecCoord = getNormalizedVecCoord(g);
			
			int di = i*6*5;
			for(int j = 0; j < 6; j++){
				System.arraycopy(genNormalizedVertex(j, vecCoord,  texCoord), 0, data, di+j*5, 5);
			}
		}
		
		this.setData(data, DATA_FORMAT_XYZUV);
	}
	
	private float[] genNormalizedVertex(int i, float[] v, float[] t){
		float[] vertex = null;
		
		switch(i){
		case 0:
			vertex = new float[]{v[2], v[1], 0, t[2], t[1]};
			break;
		case 1:
		case 4:
			vertex = new float[]{v[0], v[1], 0, t[0], t[1]};
			break;
		case 2:
		case 3:
			vertex = new float[]{v[2], v[3], 0, t[2], t[3]};
			break;
		case 5:
			vertex = new float[]{v[0], v[3], 0, t[0], t[3]};
			break;
		}
		return vertex;
	}
	
	private float[] getNormalizedVecCoord(Glyph g){
		float x1 = g.x/(this.width/2f) - 1;
		float y1 = (g.y/(this.height/2f) - 1)*-1;
		float x2 = (g.x+g.gd.width)/(this.width/2f) - 1;
		float y2 = ((g.y+g.gd.height)/(this.height/2f) - 1)*-1;
		/*float x1 = g.x;
		float y1 = g.y;
		float x2 = g.x+g.gd.width;
		float y2 = g.y+g.gd.height;*/
		
		return new float[]{x1,y1,x2,y2};
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
		this.maxW = w;
		this.maxH = h;
		updateText();
	}
	
	public int getWidth(){
		return Math.round(Math.max(this.width, maxW));
	}
	
	public int getHeight(){
		return Math.round(Math.max(this.height, maxH));
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
