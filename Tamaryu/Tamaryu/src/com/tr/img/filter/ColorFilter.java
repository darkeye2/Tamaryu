package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorFilter extends Filter {
	public int type = 0x08;
	protected int r, g, b;
	protected float[] hsv = new float[3];
	protected float[] hsvPc = new float[3]; 

	public ColorFilter(Color c) {
		this.r = c.getRed();
		this.g = c.getGreen();
		this.b = c.getBlue();
		Color.RGBtoHSB(r, g, b, hsv);
	}

	public ColorFilter(int r, int g, int b) {
		this(new Color(r,g,b));
	}

	@Override
	public BufferedImage apply(BufferedImage img) {
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color pc = new Color(img.getRGB(x, y), true);
				Color.RGBtoHSB(pc.getRed(), pc.getGreen(), pc.getBlue(), hsvPc);
				int la = pc.getAlpha();

				// init arrays
				hsvPc[0] = hsv[0];
				Color nc = new Color(Color.HSBtoRGB(hsvPc[0], hsvPc[1], hsvPc[2]));

				img.setRGB(x, y,
						new Color(nc.getRed(), nc.getGreen(), nc.getBlue(), la).getRGB());
			}
		}

		return img;
	}

}
