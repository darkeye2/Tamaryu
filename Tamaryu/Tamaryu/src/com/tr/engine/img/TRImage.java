package com.tr.engine.img;

public class TRImage {
	protected String imageName = "";
	protected String fileName = "";
	protected String fileExtension  = "";
	protected String path = "";
	protected int index = 0; 
	protected int offsetX = 0;
	protected int offsetY = 0;
	protected int imageWidth = 0;
	protected int imageHeight = 0;
	protected int fullWidth = 0;
	protected int fullHeight = 0;
	
	public TRImage(String name, String filename, String fileExtension, String path, 
			int x, int y, int index, int imgW, int imgH, int fullW, int fullH){
		this.imageName = name;
		this.fileName = filename;
		this.fileExtension = fileExtension;
		this.index = index;
		this.offsetX = x;
		this.offsetY = y;
		this.imageWidth = imgW;
		this.imageHeight = imgH;
		this.fullWidth = fullW;
		this.fullHeight = fullH;
		this.path = path;
	}
	
	public String getFullFileName(){
		return fileName+"."+fileExtension;
	}
	
	public String getFullFilePath(){
		return path+"/"+getFullFileName();
	}
	
	public String getIndexName(){
		return fileName+"_"+index+"."+fileExtension;
	}
	
	public String getName(){
		return imageName;
	}
	
	public int getIndex(){
		return index;
	}
	
	public float getWidth(){
		return this.imageWidth;
	}
	
	public float getNormalizedWidth(){
		return Math.round((float)this.imageWidth/(float)this.fullWidth);
	}
	
	public float getHeight(){
		return this.imageHeight;
	}
	
	public float getNormalizedHeight(){
		return Math.round((float)this.imageHeight/(float)this.fullHeight);
	}
	
	public float getX(){
		return this.offsetX;
	}
	
	public float getNormalizedX(){
		return Math.round((float)this.offsetX/(float)this.fullWidth);
	}
	
	public float getY(){
		return this.offsetY;
	}
	
	public float getNormalizedY(){
		return Math.round((float)this.offsetY/(float)this.fullWidth);
	}
}
