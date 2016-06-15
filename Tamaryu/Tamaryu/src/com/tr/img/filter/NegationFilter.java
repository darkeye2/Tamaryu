package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class NegationFilter extends Filter {
	public int type = 0x03;
	
	public NegationFilter(){
		
	}

	@Override
	public BufferedImage apply(BufferedImage img) {
		
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				Color pc = new Color(img.getRGB(x, y), true);
				int nr = pc.getRed();
				int ng = pc.getGreen();
				int nb = pc.getBlue();
				int na = pc.getAlpha();
				
				img.setRGB(x, y, new Color(255-nr, 255-ng, 255-nb, na).getRGB());
			}
		}
		
		return img;
	}

}
