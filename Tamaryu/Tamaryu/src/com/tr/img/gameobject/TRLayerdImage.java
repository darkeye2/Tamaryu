package com.tr.img.gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.tr.img.filter.Filter;

public class TRLayerdImage extends TRImage {
	protected TRImage[] layers;
	
	public TRLayerdImage(String file, int index, TRImage[] l){
		super(file, index);
		layers = l;
		
		if(l != null && l.length > 0){
			this.width = l[0].width;
			this.height = l[0].height;
		}
		
		buildImage();
	}
	
	protected void buildImage(){
		BufferedImage bi = new BufferedImage(width,
				height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		
		for(TRImage img : layers){
			img.getImageIcon().paintIcon(null, g, 0, 0);
		}

		g.dispose();
		
		this.img = new ImageIcon(bi);
		resetFilter();
		applyFilter();
	}
	
	public void resetFilter(int layer){
		this.layers[layer].resetFilter();
		buildImage();
	}
	
	public void applyFilter(int layer){
		this.layers[layer].applyFilter();
		buildImage();
	}
	
	public void addFilter(Filter f, int layer){
		this.layers[layer].addFilter(f);
		buildImage();
	}
	
	public void removeFilter(Filter f, int layer){
		this.layers[layer].removeFilter(f);
		buildImage();
	}
	
	public void removeAllFilter(int layer){
		this.layers[layer].removeAllFilter();
		buildImage();
	}
	
	public void removeFilterType(Filter f, int layer){
		this.layers[layer].removeFilterType(f);
		buildImage();
	}

}
