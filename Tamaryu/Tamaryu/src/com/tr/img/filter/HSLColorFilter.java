package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.tr.util.GraphicsUtility;

public class HSLColorFilter extends Filter {
	public int type = 0x08;
	protected int r, g, b;
	protected float[] hsl = new float[3];
	protected float[] hslPc = new float[3]; 

	public HSLColorFilter(Color c) {
		this.r = c.getRed();
		this.g = c.getGreen();
		this.b = c.getBlue();
		GraphicsUtility.rgbToHsl(c, hsl);
	}

	public HSLColorFilter(int r, int g, int b) {
		this(new Color(r,g,b));
	}

	@Override
	public BufferedImage apply(BufferedImage img) {
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color pc = new Color(img.getRGB(x, y), true);
				GraphicsUtility.rgbToHsl(pc, hslPc);
				int la = pc.getAlpha();

				// init arrays
				hslPc[0] = hsl[0];
				
				if(hslPc[1] > 0.1){
					if(hslPc[2] < 0.9){
						hslPc[2] -= 0.5f * (0.5 - hsl[2]);
						hslPc[2] = (float) Math.min(1.0f, Math.max(0f, hslPc[2]));
					}
					hslPc[1] = hsl[1];
				}
				Color nc = new Color(GraphicsUtility.hslToRgb(hslPc[0], hslPc[1], hslPc[2], la), true);

				img.setRGB(x, y, nc.getRGB());
			}
		}

		return img;
	}

}
