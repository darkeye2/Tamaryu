package com.tr.img.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorizationFilter extends Filter {
	public int type = 0x01;
	protected int r, g, b, a;
	protected boolean valueFromAlpha = false;
	
	public ColorizationFilter(Color c, boolean vba){
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
		a = c.getAlpha();
		valueFromAlpha = vba;
	}
	
	public ColorizationFilter(Color c){
		this(c, false);
	}
	
	public ColorizationFilter(int r, int g, int b, int a, boolean vba){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.valueFromAlpha = vba;
	}
	
	public ColorizationFilter(int r, int g, int b, int a){
		this(r, g, b, a, false);
	}
	
	public ColorizationFilter(int r, int g, int b, boolean vba){
		this(r, g, b, 0, vba);
	}
	
	public ColorizationFilter(int r, int g, int b){
		this(r, g, b, 0, false);
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
				
				if(this.valueFromAlpha){
					if(na < 10){
						continue;
					}
					
					if(na < 30){
						na = 255;
					}else{
						float fa = na/255f;
						
						nr = (int)(r*fa);
						ng = (int)(g*fa);
						nb = (int)(b*fa);
						na = 255;
					}
				}else{
					if(na < 30){
						continue;
					}
					
					nr += r;
					ng += g;
					nb += b;
					na += a;
				}
				
				//fix color values
				nr = (nr > 255)? 255 : ((nr < 0)? 0 : nr);
				ng = (ng > 255)? 255 : ((ng < 0)? 0 : ng);
				nb = (nb > 255)? 255 : ((nb < 0)? 0 : nb);
				na = (na > 255)? 255 : ((na < 0)? 0 : na);
				
				img.setRGB(x, y, new Color(nr, ng, nb, na).getRGB());
			}
		}
		
		return img;
	}

}
