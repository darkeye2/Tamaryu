package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class RGBFilter extends Filter {
	public int type = Filter.COLOR_FILTER;
	private boolean includeAlpha = false;
	private int color = 0;
	private int r, g, b;
	private int alpha = 0;
	
	public RGBFilter(int r, int g, int b, int a, boolean ia){
		this.includeAlpha = ia;
		this.alpha |= (a << 24);
		this.color |= ((r << 16) | (g << 8) | (b)); 
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public RGBFilter(int r, int g, int b, int a){
		this(r,g,b,a, false);
	}
	
	public RGBFilter(int r, int g, int b){
		this(r,g,b,0xff,false);
	}
	
	public RGBFilter(Color c, boolean ia){
		this(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), ia);
	}
	
	public RGBFilter(Color c){
		this(c, false);
	}
	
	

	@Override
	public BufferedImage apply(BufferedImage img) {
		int orgAlpha = 0;
		DataBufferInt imgDB = (DataBufferInt) img.getRaster().getDataBuffer();
		int banks = imgDB.getNumBanks();
		int cr, cg, cb;
		
		for (int bank = 0; bank < banks; bank++) {
			int[] actual = imgDB.getData(bank);

			for(int i = 0; i < actual.length; i++){
				cb = actual[i] & 0x000000ff;
				cg = (actual[i] >> 8) & 0x000000ff;
				cr = (actual[i] >> 16) & 0x000000ff;
				orgAlpha = (actual[i] >> 24) & 0x000000ff;
				
				cr = Math.min(255, Math.max(cr+r, 0));
				cg = Math.min(255, Math.max(cg+g, 0));
				cb = Math.min(255, Math.max(cb+b, 0));
				
				actual[i] = ((orgAlpha << 24) | (cr << 16) | (cg << 8) | (cb));
				//System.out.format("Col: %x%n", actual[i]);
			}
		}
		
		return img;
	}

}
