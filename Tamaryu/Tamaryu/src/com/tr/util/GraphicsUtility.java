package com.tr.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class GraphicsUtility {
	private static Timer guiTimer = null;
	private static Object timerLock = new Object();

	public static Timer getTimer() {
		if (guiTimer == null) {
			synchronized (timerLock) {
				if (guiTimer == null)
					guiTimer = new Timer();
			}
		}

		return GraphicsUtility.guiTimer;
	}
	
	public static Dimension getImageSize(File f){
		try(ImageInputStream in = ImageIO.createImageInputStream(f)){
		    final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
		    if (readers.hasNext()) {
		        ImageReader reader = readers.next();
		        try {
		            reader.setInput(in);
		            return new Dimension(reader.getWidth(0), reader.getHeight(0));
		        } finally {
		            reader.dispose();
		        }
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public static ImageMetaInfo getMetaInfo(File f){
		ImageMetaInfo imi = new ImageMetaInfo();
		try(ImageInputStream in = ImageIO.createImageInputStream(f)){
		    final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
		    if (readers.hasNext()) {
		        ImageReader reader = readers.next();
		        try {
		            reader.setInput(in);
		            imi.w = reader.getWidth(0);
		            imi.h = reader.getHeight(0);
		            imi.format = reader.getFormatName();
		            imi.count = reader.getNumImages(true);
		            
		            return imi;
		        } finally {
		            reader.dispose();
		        }
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return imi;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	public static BufferedImage toImage(ImageIcon ic) {
		BufferedImage bi = new BufferedImage(ic.getIconWidth(),
				ic.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		// paint the Icon to the BufferedImage.
		ic.paintIcon(null, g, 0, 0);
		g.dispose();

		return bi;
	}

	public static BufferedImage toImage(JComponent c) {
		BufferedImage img = new BufferedImage(c.getWidth(), c.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		c.paint(g);
		g.dispose();

		return img;
	}

	public static void saveImage(String path, BufferedImage ba) {
		try {
			ImageIO.write(ba, "png", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveImage(BufferedImage ba) {
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			saveImage(file.getAbsolutePath(), ba);
		}
	}

	public static float[] rgbToHsl(Color color, float[] dest) {
		float r = (color.getRed()) / 255f;
		float g = (color.getGreen()) / 255f;
		float b = (color.getBlue()) / 255f;

		float min = Math.min(r, Math.min(g, b));
		float max = Math.max(r, Math.max(g, b));

		float h = 0;

		if (max == min) {
			h = 0;
		} else if (max == r) {
			h = (((g - b) / (max - min) / 6f) + 1) % 1;
		} else if (max == g) {
			h = ((b - r) / (max - min) / 6f) + 1f / 3f;
		} else if (max == b) {
			h = ((r - g) / (max - min) / 6f) + 2f / 3f;
		}

		float l = (max + min) / 2;

		float s = 0;

		if (max == min) {
			s = 0;
		} else if (l <= .5f) {
			s = (max - min) / (max + min);
		} else {
			s = (max - min) / (2 - max - min);
		}

		if (dest == null)
			dest = new float[3];

		dest[0] = h;
		dest[1] = s;
		dest[2] = l;

		return dest;
	}

	public static int hslToRgb(float h, float s, float l, float alpha) {
		float q = 0;

		if (l < 0.5) {
			q = l * (1 + s);
		} else {
			q = (l + s) - (s * l);
		}

		float p = 2 * l - q;

		int r = (int) (255 * Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f))));
		int g = (int) (255 * Math.max(0, HueToRGB(p, q, h)));
		int b = (int) (255 * Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f))));

		//int alphaInt = (int) (255 * alpha);
		int alphaInt = (int)alpha;

		return (alphaInt << 24) + (r << 16) + (g << 8) + (b);
	}

	private static float HueToRGB(float p, float q, float h) {
		if (h < 0)
			h += 1;

		if (h > 1)
			h -= 1;

		if (6 * h < 1) {
			return p + ((q - p) * 6 * h);
		}

		if (2 * h < 1) {
			return q;
		}

		if (3 * h < 2) {
			return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
		}

		return p;
	}

}
