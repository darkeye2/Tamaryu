package com.tr.gl.core.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.tr.gl.core.GLTexture;

public class BitmapFont {
	protected int fontSize = 20;
	protected int base = 0;
	protected int lineHeight = 0;
	protected int scaleWidth = 100;
	protected int scaleHeight = 100;
	protected int count = 0;
	protected GLTexture texture = null;
	protected String fontName = "";
	
	private HashMap<Integer, GlyphData> glyphs = new HashMap<Integer, GlyphData>();
	private HashMap<String, Integer> kerning = new HashMap<String, Integer>();
	
	public BitmapFont(String fntFile, String pngFile, boolean resource){
		InputStream in = null;
		BufferedReader br = null;
		if(resource){
			in = this.getClass().getResourceAsStream(fntFile);
			//System.out.println("Font Stream: "+in);
		}else{
			try {
				in = new FileInputStream(new File(fntFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if(in != null){
			br = new BufferedReader(new InputStreamReader(in));
		}
		
		if(br != null){
			try {
				//read metadata
				String line = br.readLine();
				parseInfoLine(cleanUpLine(line));
				line = br.readLine();
				parseCommonLine(cleanUpLine(line));
				line = br.readLine();		//skip page line
				line = br.readLine();
				parseCharsLine(cleanUpLine(line));
				
				//read glyphdata
				boolean stop = false;
				String[] arr = null;
				while(!stop && (line = br.readLine()) != null){
					arr = (cleanUpLine(line)).split(" ");
					if(arr == null || arr.length < 8 || !arr[0].equalsIgnoreCase("char")){
						stop = true;
						break;
					}
					GlyphData gd = new GlyphData(arr);
					glyphs.put(gd.id, gd);
				}
				
				//skip until kerning data
				stop = false;
				boolean found = false;
				while(arr != null && !stop){
					if(arr.length > 0 && arr[0].equalsIgnoreCase("kernings")){
						stop = true;
						found = true;
					}
					if((line = br.readLine()) == null){
						stop = true;
						continue;
					}
					arr = (cleanUpLine(line)).split(" ");
				}
				
				//read kerning data
				stop = false;
				while(!stop && found && (line = br.readLine()) != null){
					arr = (cleanUpLine(line)).split(" ");
					if(arr == null || arr.length < 4 || !arr[0].equalsIgnoreCase("kerning")){
						stop = true;
						break;
					}
					
					String name = arr[1].replace("first=", "")+"_"+arr[2].replace("second=", "");
					int value = Integer.parseInt(arr[3].replace("amount=", ""));
					kerning.put(name, value);
				}
					
					
				//System.out.println("Glyphs read: "+glyphs.size());
				
			} catch (IOException e) {
				System.err.println("ERROR in .fnt file: "+fntFile);
				e.printStackTrace();
			}
		}
		
		this.texture = new GLTexture(pngFile, fntFile);
	}
	
	public float getSize(){
		return this.fontSize;
	}
	
	public float getBase(){
		return this.getBase();
	}
	
	public float getLineHeight(){
		return this.lineHeight;
	}
	
	public float getWScale(){
		return this.scaleWidth;
	}
	
	public float getHScale(){
		return this.scaleHeight;
	}
	
	public GLTexture getTexture(){
		return this.texture;
	}
	
	public GlyphData getGlyphData(int c){
		if(glyphs.containsKey(c)){
			return glyphs.get(c);
		}
		return glyphs.get('a');
	}
	
	public int getKerning(int f, int s){
		String key = f+"_"+s;
		if(kerning.containsKey(key)){
			return kerning.get(key);
		}
		
		return 0;
	}
	
	private String cleanUpLine(String l){
		while(l.contains("  ")){
			l = l.replaceAll("  ", " ");
		}
		
		return l;
	}
	
	private void parseInfoLine(String line){
		if(line == null)
			return;
		String[] arr = line.split(" ");
		if(arr.length < 3)
			return;
		this.fontSize = Integer.parseInt(arr[2].replace("size=", ""));
	}
	
	private void parseCommonLine(String line){
		if(line == null)
			return;
		String[] arr = line.split(" ");
		if(arr.length < 5)
			return;
		this.lineHeight = Integer.parseInt(arr[1].replace("lineHeight=", ""));
		this.base = Integer.parseInt(arr[2].replace("base=", ""));
		this.scaleWidth = Integer.parseInt(arr[3].replace("scaleW=", ""));
		this.scaleHeight = Integer.parseInt(arr[4].replace("scaleH=", ""));
	}
	
	private void parseCharsLine(String line){
		if(line == null)
			return;
		String[] arr = line.split(" ");
		if(arr.length < 2)
			return;
		this.count = Integer.parseInt(arr[1].replace("count=", ""));
	}
	
}
