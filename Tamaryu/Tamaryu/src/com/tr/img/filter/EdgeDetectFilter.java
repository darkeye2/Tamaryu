package com.tr.img.filter;

import java.awt.image.BufferedImage;

public class EdgeDetectFilter extends Filter {
	
	public EdgeDetectFilter(){
		
	}

	@Override
	public BufferedImage apply(BufferedImage img) {
		float filter[][] =	{{-1.0f,0.0f,-1.0f},
				{0.0f,4.0f,0.0f},
				{-1.0f,0.0f,-1.0f}};

		return Filter.convolution(img, filter, 1, 127);
	}

}
