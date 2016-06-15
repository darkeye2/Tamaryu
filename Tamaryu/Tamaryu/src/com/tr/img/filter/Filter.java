package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public abstract class Filter {
	public int type = 0;
	
	public static final int COLOR_FILTER = 1;
	public static final int EDGE_FILTER = 2;
	public static final int OVERLAY_FILTER = 3;
	public static final int GEOMETRIE_FILTER = 4;
	public static final int LIGHT_FILTER = 5;

	public abstract BufferedImage apply(BufferedImage img);
	
	public static BufferedImage convolution(BufferedImage bi, float[][] filter, float filter_div, float offset ){
		int r = 0, g = 0, b = 0, a;
		Color pc = new Color(bi.getRGB(0, 0), true);
		BufferedImage org = deepCopy(bi);
		
		for (int y = 0; y < bi.getHeight(); y++) {
			for (int x = 0; x < bi.getWidth(); x++) {
				r = g  = b = 0;
				a = pc.getAlpha();
				
				for (int j=0; j<3; j++) {
					int yv = Math.min(Math.max(y - 1 + j, 0), bi.getHeight() - 1);
					for (int i=0; i<3; i++) {
						pc = new Color(org.getRGB(Math.min(Math.max(x - 1 + i, 0), bi.getWidth() - 1), yv), true);
						r += (float)pc.getRed() * filter[j][i];
						g += (float)pc.getGreen() * filter[j][i];
						b += (float)pc.getBlue() * filter[j][i];
					}
				}

				r = (int) ((r/filter_div)+offset);
				g = (int) ((g/filter_div)+offset);
				b = (int) ((b/filter_div)+offset);

				//fix color values
				r = (r > 255)? 255 : ((r < 0)? 0 : r);
				g = (g > 255)? 255 : ((g < 0)? 0 : g);
				b = (b > 255)? 255 : ((b < 0)? 0 : b);
				
				bi.setRGB(x, y, new Color(r,g,b,a).getRGB());
			}
		}
		
		return bi;
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static BufferedImage fillRect(BufferedImage bi, int x1, int y1,
			int x2, int y2, Color c) {

		int x, y;

		if (x1 == x2 && y1 == y2) {
			bi.setRGB(x1, y1, c.getRGB());
			return bi;
		}

		if (x1 > x2) {
			x = x1;
			x1 = x2;
			x2 = x;
		}

		if (y1 > y2) {
			y = y1;
			y1 = y2;
			y2 = y;
		}

		if (x1 < 0) {
			x1 = 0;
		}

		if (x2 >= bi.getWidth()) {
			x2 = bi.getWidth() - 1;
		}

		if (y1 < 0) {
			y1 = 0;
		}

		if (y2 >= bi.getHeight()) {
			y2 = bi.getHeight() - 1;
		}

		for (y = y1; y <= y2; y++) {
			for (x = x1; x <= x2; x++) {
				bi.setRGB(x, y, c.getRGB());
			}
		}

		return bi;
	}

}
