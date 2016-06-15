package com.tr.img.mng;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GifLoader {
	
	public static BufferedImage[] loadAll(File f) throws IOException{
		//try to get reader and return null if not available
		ImageReader reader = getImageReader(f);
		if(reader == null){
			return null;
		}
		
		//create array for images
		BufferedImage[] images;
		
		//create buffer for frames
		int numberOfFrames = reader.getNumImages(true);
		ArrayList<GifFrame> frames = new ArrayList<GifFrame>();
		
		//try to get width and height from metadata
		int width = -1;
		int height = -1;
		Dimension d = getImageSizeByMeta(reader);
		if(d != null){
			width = d.width;
			height = d.height;
		}
		
		//read each image
		BufferedImage master = null;
		Graphics2D masterGraphics = null;
		for(int i = 0; i < numberOfFrames; i++){
			//read next image, if null, stop loop
			BufferedImage bi = readFrame(reader, i);
			if(bi == null){
				break;
			}
			
			//if size still undefined, take size from first image
			if (width == -1 || height == -1) {
				width = bi.getWidth();
				height = bi.getHeight();
			}
			
			//get disposal method for this frame 
			String disposal = getDisposal(reader, i);
			
			//image start position
			int x = 0;
			int y = 0;

			//if this is the first image, take it as master
			if (master == null) {
				//create black rgba image as master
				master = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_ARGB);
				masterGraphics = master.createGraphics();
				masterGraphics.setBackground(new Color(0, 0, 0, 0));
			} else {
				//get image offset for this frame
				Point p = getOffset(reader, i);
				x = p.x;
				y = p.y;
			}
			
			//draw image
			masterGraphics.drawImage(bi, x, y, null);

			//create copy of the image (to reuse master)
			BufferedImage copy = new BufferedImage(master.getColorModel(),
					master.copyData(null), master.isAlphaPremultiplied(), null);
			
			//add image to buffer
			frames.add(new GifFrame(copy, disposal));
			
			//check the disposal method of the frame
			if (disposal.equals("restoreToPrevious")) {
				//if restoreToPrevois is defined, go back through the frames,
				// until last frame with other disposal method
				BufferedImage from = null;
				for (int j = i - 1; j >= 0; j--) {
					if (!frames.get(j).disposal.equals("restoreToPrevious")
							|| j == 0) {
						from = frames.get(j).img;
						break;
					}
				}

				//draw the selected image on the master frame
				master = new BufferedImage(from.getColorModel(),
						from.copyData(null), from.isAlphaPremultiplied(), null);
				masterGraphics = master.createGraphics();
				masterGraphics.setBackground(new Color(0, 0, 0, 0));
			} else if (disposal.equals("restoreToBackgroundColor")) {
				//if restoreToBackgroundColor is selected, clear the master frame
				masterGraphics.clearRect(x, y, bi.getWidth(),
						bi.getHeight());
			}
		}
		
		//close reader
		reader.dispose();
		
		//init the array and put the frames inside the array
		images = new BufferedImage[frames.size()];
		for(int i = 0; i < frames.size(); i++){
			images[i] = frames.get(i).img;
		}
		
		return images;
	}
	
	public static BufferedImage load(File f, int index) throws IOException{
		BufferedImage[] images = loadAll(f);
		if(images != null){
			if(images.length > index){
				return images[index];
			}
			
			return images[0];
		}
		
		return null;
	}
	
	public static BufferedImage load(File f) throws IOException{
		return load(f, 0);
	}
	
	private static ImageReader getImageReader(File f) throws IOException{
		// create image reader for gif
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		reader.setInput(ImageIO.createImageInputStream(f));
		
		return reader;
	}
	
	private static Dimension getImageSizeByMeta(ImageReader r)
			throws IOException {
		//get metadata
		IIOMetadata metadata = r.getStreamMetadata();
		if (metadata != null) {
			//get root node
			IIOMetadataNode globalRoot = (IIOMetadataNode) metadata
					.getAsTree(metadata.getNativeMetadataFormatName());
			
			//get logical screen descriptor from root node
			NodeList globalScreenDescriptor = globalRoot
					.getElementsByTagName("LogicalScreenDescriptor");
			
			//if screen descriptor not null gry to get width and height
			if (globalScreenDescriptor != null
					&& globalScreenDescriptor.getLength() > 0) {
				IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreenDescriptor
						.item(0);

				if (screenDescriptor != null) {
					int w = Integer.parseInt(screenDescriptor
							.getAttribute("logicalScreenWidth"));
					int h = Integer.parseInt(screenDescriptor
							.getAttribute("logicalScreenHeight"));
					return new Dimension(w, h);
				}
			}
		}
		
		//return null if no metadata or no screen descriptor available
		return null;
	}
	
	private static BufferedImage readFrame(ImageReader r, int index){
		BufferedImage bi = null;
		try {
			bi = r.read(index);
		} catch (IndexOutOfBoundsException | IOException io) {
			return null;
		}
		
		return bi;
	}
	
	private static String getDisposal(ImageReader r, int index) throws IOException{
		//get root metadata node for this frame
		IIOMetadataNode root = (IIOMetadataNode) r.getImageMetadata(
				index).getAsTree("javax_imageio_gif_image_1.0");
		
		//get graphics control extension node
		IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName(
				"GraphicControlExtension").item(0);
		
		//get disposal methode string from metadata
		String disposal = gce.getAttribute("disposalMethod");
		
		return disposal;
	}
	
	private static Point getOffset(ImageReader r, int index) throws IOException{
		Point offset = new Point(0,0);
		
		//get root metadata node for this frame
		IIOMetadataNode root = (IIOMetadataNode) r.getImageMetadata(
				index).getAsTree("javax_imageio_gif_image_1.0");
		
		//get node list and iterate through until the image descriptor node
		// read offset data from image descriptor node if available;
		NodeList children = root.getChildNodes();
		for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++) {
			Node nodeItem = children.item(nodeIndex);
			if (nodeItem.getNodeName().equals("ImageDescriptor")) {
				NamedNodeMap map = nodeItem.getAttributes();
				offset.x = Integer.valueOf(map.getNamedItem(
						"imageLeftPosition").getNodeValue());
				offset.y = Integer.valueOf(map
						.getNamedItem("imageTopPosition")
						.getNodeValue());
			}
		}
		
		return offset;
	}
	
	
	
	
	private static class GifFrame{
		public BufferedImage img = null;
		public String disposal = "";
		
		public GifFrame(BufferedImage i, String d) {
			img = i;
			disposal = d;
		}
	}

}
