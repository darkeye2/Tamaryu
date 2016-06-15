package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ContrastFilter extends Filter {
	protected float contrast = 0;
	
	public ContrastFilter(float contrast){
		this.contrast = contrast;
	}

	@Override
	public BufferedImage apply(BufferedImage img) {
		double rf,gf,bf;
		
		contrast = (100.0f-contrast)/100.0f;
		contrast = contrast*contrast;
		
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				Color pc = new Color(img.getRGB(x, y), true);
				int nr = pc.getRed();
				int ng = pc.getGreen();
				int nb = pc.getBlue();
				int na = pc.getAlpha();
				
				rf = (double)nr/255.0;
				rf = rf-0.5;
				rf = rf*contrast;
				rf = rf+0.5;
				rf = rf*255.0;

				bf = (double)nb/255.0;
				bf = bf-0.5;
				bf = bf*contrast;
				bf = bf+0.5;
				bf = bf*255.0;

				gf = (double)ng/255.0;
				gf = gf-0.5;
				gf = gf*contrast;
				gf = gf+0.5;
				gf = gf*255.0;

				rf = (rf > 255.0)? 255.0 : ((rf < 0.0)? 0.0:rf);
				gf = (gf > 255.0)? 255.0 : ((gf < 0.0)? 0.0:gf);
				bf = (bf > 255.0)? 255.0 : ((bf < 0.0)? 0.0:bf);
				
				img.setRGB(x, y, new Color((int)rf, (int)gf, (int)bf, na).getRGB());
			}
		}
		
		// TODO Auto-generated method stub
		return img;
	}

}
