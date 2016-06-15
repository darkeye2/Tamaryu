package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BrightnessFilter extends Filter {
	protected int brightness = 0;
	
	public BrightnessFilter(int brightness){
		this.brightness = Math.min(Math.max(brightness, -255), 255);
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
				
				nr = nr + brightness;
				ng = ng + brightness;
				nb = nb + brightness;

				nr = (nr > 255)? 255 : ((nr < 0)? 0:nr);
				ng = (ng > 255)? 255 : ((ng < 0)? 0:ng);
				nb = (nb > 255)? 255 : ((nb < 0)? 0:nb);
				
				img.setRGB(x, y, new Color(nr, ng, nb, na).getRGB());
			}
		}
		
		return img;
	}

}
