package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;

public class ColorChangeFilter extends Filter {
	public int type = 0x07;
	protected int r, g, b;
	protected Integer[] indices = { 0, 1, 2 };
	protected final int[] trgRgb = new int[3];

	public ColorChangeFilter(Color c) {
		this.r = c.getRed();
		this.g = c.getGreen();
		this.b = c.getBlue();
		trgRgb[0] = r;
		trgRgb[1] = g;
		trgRgb[2] = b;
		
		Arrays.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return Integer.compare(trgRgb[o1], trgRgb[o2]);
			}
		});
	}

	public ColorChangeFilter(int r, int g, int b) {
		this(new Color(r,g,b));
	}

	@Override
	public BufferedImage apply(BufferedImage img) {
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color pc = new Color(img.getRGB(x, y), true);
				int la = pc.getAlpha();

				// init arrays
				int[] altRgb = new int[3];
				int[] newRgb = new int[3];

				// wirte color values into array
				altRgb[0] = pc.getRed();
				altRgb[1] = pc.getGreen();
				altRgb[2] = pc.getBlue();

				Arrays.sort(altRgb);
				

				// fill position array
				for (int i = 0; i < trgRgb.length; i++) {
					newRgb[indices[i]] = altRgb[i];
				}

				img.setRGB(x, y,
						new Color(newRgb[0], newRgb[1], newRgb[2], la).getRGB());
			}
		}

		return img;
	}

}
