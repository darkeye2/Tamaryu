package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class PixelationFilter extends Filter {
	public int type = 0x04;
	protected int blockSize = 10;
	protected boolean upperLeft = false;
	
	public PixelationFilter(int blockSize, boolean upperLeft){
		this.blockSize = blockSize;
		this.upperLeft = upperLeft;
	}
	
	public PixelationFilter(int blockSize){
		this(blockSize, false);
	}
	
	public PixelationFilter(){
		this(10, false);
	}
	

	@Override
	public BufferedImage apply(BufferedImage img) {
		
		if(this.upperLeft){
			for (int y = 0; y < img.getHeight(); y += blockSize) {
				for (int x = 0; x < img.getWidth(); x += blockSize) {
					Color pc = new Color(img.getRGB(x, y), true);
					img = Filter.fillRect(img, x, y, x + blockSize - 1, y + blockSize - 1, pc);
				}
			}
		}else{
			for(int y = 0; y < img.getHeight(); y += blockSize) {
				for (int x = 0; x < img.getWidth(); x += blockSize) {
					int a, r, g, b;
					int total;
					int cx, cy;

					a = r = g = b = total = 0;
					
					/* sampling */
					for (cy = 0; cy < blockSize; cy++) {
						for (cx = 0; cx < blockSize; cx++) {
							Color pc = new Color(img.getRGB(x, y), true);
							a += pc.getAlpha();
							r += pc.getRed();
							g += pc.getGreen();
							b += pc.getBlue();
							total++;
						}
					}
					
					/* drawing */
					if (total > 0) {
						a = a/total;
						r = r/total;
						g = g/total;
						b = b/total;
						img = Filter.fillRect(img, x, y, x + blockSize -1, y + blockSize -1, new Color(r,g,b,a));
					}
				}
			}
		}
		
		return img;
	}

}
