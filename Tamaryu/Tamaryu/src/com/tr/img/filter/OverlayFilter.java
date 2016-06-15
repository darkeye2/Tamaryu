package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.tr.util.GraphicsUtility;

public class OverlayFilter extends Filter {
	protected BufferedImage overlay;
	protected int width, height;
	
	public OverlayFilter(BufferedImage o){
		overlay = o;
		width = o.getWidth();
		height = o.getHeight();
	}

	@Override
	public BufferedImage apply(BufferedImage img) {
		if(img.getWidth() != width || img.getHeight() != height){
			overlay = GraphicsUtility.resize(overlay, img.getWidth(), img.getHeight());
			width = img.getWidth();
			height = img.getHeight();
		}
		
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color pc = new Color(img.getRGB(x, y), true);
				Color oc = new Color(overlay.getRGB(x, y), true);
				
				if(pc.getAlpha() < 10){
					continue;
				}
				
				int r = (int)((float)pc.getRed()*(pc.getAlpha()/255f) + (float)oc.getRed()*(oc.getAlpha()/255f));
				int g = (int)((float)pc.getGreen()*(pc.getAlpha()/255f) + (float)oc.getGreen()*(oc.getAlpha()/255f));
				int b = (int)((float)pc.getBlue()*(pc.getAlpha()/255f) + (float)oc.getBlue()*(oc.getAlpha()/255f));
				int a = oc.getAlpha()+pc.getAlpha();
				
				r = Math.max(0, Math.min(r, 255));
				g = Math.max(0, Math.min(g, 255));
				b = Math.max(0, Math.min(b, 255));
				a = Math.max(0, Math.min(a, 255));

				img.setRGB(x, y, new Color(r,g,b, a).getRGB());
			}
		}

		return img;
	}

}
