package com.tr.img.gameobject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.tr.img.filter.Filter;

public class TRImage {
	protected String fileName = "";
	protected int index = -1;
	protected int width = 0, height = 0;
	protected ImageIcon img = null;
	protected ImageIcon filteredImg = null;
	
	protected ArrayList<Filter> filter = new ArrayList<Filter>();

	public TRImage(String file, int index, ImageIcon img) {
		this.fileName = file;
		this.index = index;
		this.img = img;
		this.width = img.getIconWidth();
		this.height = img.getIconHeight();
		//resetFilter();
	}
	
	public TRImage(String name, ImageIcon img){
		this(name, -1, img);
	}
	
	protected TRImage(String file, int index){
		this.fileName = file;
		this.index = index;
	}

	public String getName() {
		if(index != -1)
			return fileName + "_" + index;
		
		return fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public int getIndex() {
		return index;
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public ImageIcon getImageIcon() {
		if(filter.size() == 0 || filteredImg == null){
			return img;
		}
		return filteredImg;
	}

	public ImageIcon getOriginalImage() {
		return img;
	}
	
	public void setImage(ImageIcon i){
		this.img = i;
		resetFilter();
	}
	
	public void resetFilter(){
		if(filteredImg != null){
			if(filter.size() == 0){
				filteredImg = null;
			}else{
				this.filteredImg = toImageIcon(deepCopy(toBufferedImage(img)));
			}
		}else if(filter.size() > 0){
			this.filteredImg = toImageIcon(deepCopy(toBufferedImage(img)));
		}
	}
	
	public void applyFilter(){
		if(filter.size() == 0){
			return;
		}
		for(Filter f: filter){
			filteredImg = toImageIcon(f.apply(toBufferedImage(filteredImg)));
		}
	}
	
	public void addFilter(Filter f){
		filter.add(f);
		resetFilter();
		applyFilter();
	}
	
	public void removeFilter(Filter f){
		filter.remove(f);
		resetFilter();
		applyFilter();
	}
	
	public void removeAllFilter(){
		filter.clear();
		resetFilter();
	}
	
	public void removeFilterType(Filter f){
		for(Filter fl: filter){
			if(fl.type == f.type){
				filter.remove(fl);
			}
		}
		resetFilter();
		applyFilter();
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static BufferedImage toBufferedImage(ImageIcon i) {
		BufferedImage bi = new BufferedImage(i.getIconWidth(),
				i.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		i.paintIcon(null, g, 0, 0);
		g.dispose();
		return bi;
	}
	
	public static ImageIcon toImageIcon(BufferedImage bi){
		return new ImageIcon(bi);
	}

}
