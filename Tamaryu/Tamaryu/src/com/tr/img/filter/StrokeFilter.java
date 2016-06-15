package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class StrokeFilter extends Filter {
	
	public StrokeFilter(){
		
	}

	@Override
	public BufferedImage apply(BufferedImage img) {
		BufferedImage org = deepCopy(img);
		
		for(int x=0; x<img.getWidth();x++){
		    for(int y=0;y<img.getHeight();y++){
		    	Color pc = new Color(img.getRGB(x, y), true);
		        int pxUp = toGray(new Color(org.getRGB(x, Math.max(y-1, 0))));
		        int pxDown = toGray(new Color(org.getRGB(x, Math.min(y+1, img.getHeight()-1))));
		        int pxLeft = toGray(new Color(org.getRGB(Math.max(x-1, 0), y)));
		        int pxRight = toGray(new Color(org.getRGB(Math.min(x+1, img.getWidth()-1), y)));
		        int pxUpLeft = toGray(new Color(org.getRGB(Math.max(x-1, 0), Math.max(y-1, 0))));
		        int pxUpRight = toGray(new Color(org.getRGB(Math.min(x+1, img.getWidth()-1), Math.max(y-1, 0))));
		        int pxDownLeft = toGray(new Color(org.getRGB(Math.max(x-1, 0), Math.min(y+1, img.getHeight()-1))));
		        int pxDownRight = toGray(new Color(org.getRGB(Math.min(x+1, img.getWidth()-1), Math.min(y+1, img.getHeight()-1))));
		        
		        // mask
		        int cx = (pxUpRight+(pxRight*2)+pxDownRight)-(pxUpLeft+(pxLeft*2)+pxDownLeft);
		        int cy = (pxUpLeft+(pxUp*2)+pxUpRight)-(pxDownLeft+(pxDown*2)+pxDownRight);
		        
		        // distance
		        //int gray = (int) Math.sqrt(cx*cx+cy+cy);
		        int gray = Math.abs(cx)+Math.abs(cy);
		                     
		        gray = 255-gray;
		        
		        gray = Math.min(Math.max(gray, 0), 255);
		             
		        img.setRGB(x, y, new Color(gray, gray, gray, pc.getAlpha()).getRGB());          
		    }
		}
		
		
		return img;
	}
	
	private int toGray(Color c){
		return (int)(c.getRed()*0.3f + c.getGreen()*0.59f + c.getBlue()*0.11);
		//return (int)(c.getRed()*0.299f + c.getGreen()*0.587f + c.getBlue()*0.114);
	}

}
