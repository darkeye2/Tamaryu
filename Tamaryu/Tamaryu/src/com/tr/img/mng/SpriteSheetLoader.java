package com.tr.img.mng;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheetLoader {
	
	public static BufferedImage[] loadAll(File f, int w, int h) throws IOException{
		//load full image
		BufferedImage fullImage = ImageIO.read(f);
		
		//get size of image and calculate number of sub images
		int fw = fullImage.getWidth();
		int fh = fullImage.getHeight();
		int col = (fw/w);
		int row = (fh/h);
		int numOfFrames = col*row;
		
		//create array for frames
		BufferedImage[] images = new BufferedImage[numOfFrames];
		BufferedImage tmp;
		
		//create subimages
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				tmp = fullImage.getSubimage(w*j, h*i, w, h);
				images[i*col+j] = tmp;
			}
		}
		
		return images;
	}
	
	public static BufferedImage load(File f, int index, int w, int h) throws IOException{
		BufferedImage[] images = loadAll(f, w, h);
		if(images != null){
			if(images.length > index){
				return images[index];
			}
			
			return images[0];
		}
		
		return null;
	}
	
	public static BufferedImage load(File f, int w, int h) throws IOException{
		return load(f, 0, w, h);
	}

}
