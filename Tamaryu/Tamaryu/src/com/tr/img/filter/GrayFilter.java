package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GrayFilter extends Filter {
	public int type = 0x02;
	protected float rm = 0.299f;
	protected float gm = 0.587f;
	protected float bm = 0.114f;
	
	public GrayFilter(float rm, float gm, float bm){
		this.rm = rm;
		this.gm = gm;
		this.bm = bm;
	}
	
	public GrayFilter(){
		
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
				
				nr = ng = nb = (int) (rm * nr + gm * ng + bm * nb);
				nr = Math.min(Math.max(0, nr), 255);
				ng = nb = nr;
				
				img.setRGB(x, y, new Color(nr, ng, nb, na).getRGB());
			}
		}
		
		return img;
	}

}
