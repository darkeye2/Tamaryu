package com.tr.img.mng;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.tr.img.TRImage;
import com.tr.util.GraphicsUtility;
import com.tr.util.ImageMetaInfo;
import com.tr.util.MathUtility;

public class ImageLoader2 {
	public static final String DEFAULT_PATH = "res/img/";

	private static ArrayList<String> imageType = new ArrayList<String>();
	private static boolean loaded = false;

	private static ArrayList<String> globalPathes = new ArrayList<String>();
	private ArrayList<String> privatePathes = new ArrayList<String>();

	private boolean globalInstance = true;
	private boolean resourceMode = true;

	public ImageLoader2(boolean global) {
		globalInstance = global;
		if (!loaded) {
			// add image file types
			imageType.add("jpg");
			imageType.add("png");
			imageType.add("jpeg");
			imageType.add("gif");
			imageType.add("bmp");
			imageType.add("wbmp");

			loaded = true;
		}
	}

	public ImageLoader2() {
		this(true);
	}

	public BufferedImage[] loadAllAsBI(String name) throws IOException {
		// check if file exists
		File f = getFileIfExists(name);
		if (f == null) {
			throw new FileNotFoundException("[ImageLoader] Image " + name
					+ " was not found!");
		}

		// if given name is full file name, return full image
		if (name.equals(f.getName())) {
			return new BufferedImage[] { ImageIO.read(f) };
		}

		// if sprite sheet, load images
		if (isSpriteSheet(f)) {
			Dimension d = getSpriteSize(f.getName());
			return SpriteSheetLoader.loadAll(f, d.width, d.height);
		}

		// if gif animation load images
		if (isGifAnimation(f)) {
			return GifLoader.loadAll(f);
		}

		// if not a spritesheet and not a gif animation
		// load it image with imageio
		return new BufferedImage[] { ImageIO.read(f) };
	}

	public BufferedImage loadAsBI(String name) throws IOException {
		int index = 0;
		String me = getMetaExtension(name);
		if (MathUtility.isInteger(me)) {
			index = Integer.parseInt(me);
			index = Math.max(0, index-1);
		}
		BufferedImage[] images = loadAllAsBI(name);
		if (images != null) {
			if (images.length > index) {
				return images[index];
			} else {
				return images[0];
			}
		}

		return null;
	}

	public TRImage[] loadAll(String name) throws IOException {
		String trname = getPureName(name);
		BufferedImage[] images = loadAllAsBI(name);
		TRImage[] trimages = new TRImage[images.length];
		for (int i = 0; i < images.length; i++) {
			trimages[i] = new TRImage(trname, i, new ImageIcon(images[i]));
		}

		return trimages;
	}

	public TRImage load(String name) throws IOException {
		String trname = getPureName(name);
		int index = 0;
		String me = getMetaExtension(name);
		if (MathUtility.isInteger(me)) {
			index = Integer.parseInt(me);
		}

		TRImage tri = new TRImage(trname, index, new ImageIcon(loadAsBI(name)));

		return tri;
	}

	public boolean isSpriteSheet(File f) {
		if (f == null) {
			return false;
		}
		
		Dimension d = getSpriteSize(f.getName());
		if(d.width == -1 || d.height == -1){
			return false;
		}

		// get meta info
		ImageMetaInfo imi = GraphicsUtility.getMetaInfo(f);

		// check if image size is multiple of per image size
		if ((imi.w > d.width || imi.h > d.height) && (imi.w % d.width) == 0 && (imi.h % d.height) == 0) {
			return true;
		}

		return false;
	}

	private Dimension getSpriteSize(String name) {
		Dimension d = new Dimension(-1, -1);

		// get meta extension
		String e = getMetaExtension(name);
		if (e == null || e.isEmpty()) {
			return d;
		}
		String[] tmp = e.split("x");
		if (tmp.length != 2) {
			return d;
		}

		// parse image size to integer values
		int w = Integer.parseInt(tmp[0]), h = Integer.parseInt(tmp[1]);
		
		d.width = w; 
		d.height = h;
		
		return d;
	}

	public boolean isGifAnimation(File f) {
		if (f == null) {
			return false;
		}

		// get meta info
		ImageMetaInfo imi = GraphicsUtility.getMetaInfo(f);

		// check if is a gif AND has multiple images (in
		// other case, it is not necessary to read it in
		// a special way.
		if (imi.format.toLowerCase().equals("gif") && imi.count > 1) {
			return true;
		}

		return false;
	}

	/*
	 * Try to find image file with given name. if full name is given (with file
	 * type extension) search for it in resources or in local directory
	 * 
	 * if no file type extension, list all files from known paths and check for
	 * filename.
	 * 
	 * return null if no file found
	 */

	public File getFileIfExists(String name) {
		if (isCompleteName(name)) {
			return getFileByName(name);
		} else {
			return lookForFile(getPureName(name));
		}
	}

	private File getFileByName(String name) {
		File f = null;

		for (String p : privatePathes) {
			f = getFile(p + name);
			if (f != null && f.exists()) {
				return f;
			}
		}

		if (globalInstance) {
			for (String p : globalPathes) {
				f = getFile(p + name);
				if (f != null && f.exists()) {
					return f;
				}
			}
		}

		return null;
	}

	private File lookForFile(String ucname) {
		File f = null;
		File[] files = null;

		// search in private paths for the filename
		for (String p : privatePathes) {
			f = getFile(p);

			// check if path exists and is directory
			if (f != null && f.exists() && f.isDirectory()) {
				files = f.listFiles();

				// check all files in directory for the given name
				for (File pf : files) {
					if (pf != null && pf.isFile()
							&& getPureFileName(pf.getName()) == ucname) {
						return pf;
					}
				}
			}
		}

		// search in global paths for the filename
		if (globalInstance) {
			for (String p : globalPathes) {
				f = getFile(p);

				// check if path exists and is directory
				if (f != null && f.exists() && f.isDirectory()) {
					files = f.listFiles();

					// check all files in directory for the given name
					for (File pf : files) {
						if (pf != null && pf.isFile()
								&& getPureFileName(pf.getName()).equals(ucname)) {
							return pf;
						}
					}
				}
			}
		}

		return null;
	}

	private File getFile(String path) {
		if (resourceMode) {
			try {
				URL u = ClassLoader.getSystemResource(path);
				if (u == null) {
					return null;
				}
				return new File(u.toURI());
			} catch (URISyntaxException e) {
				System.out
						.println("[ImageLoader] URISyntaxException in getFile methode!");
				e.printStackTrace();
			}
		}

		return new File(path);
	}

	private boolean isCompleteName(String name) {
		int i = name.lastIndexOf('.');
		if (i == -1 || (i + 1) >= name.length()) {
			return false;
		}

		String suffix = name.substring(i + 1);
		if (imageType.contains(suffix.toLowerCase())) {
			return true;
		}

		return false;
	}

	private String getPureFileName(String name) {
		String n = name;

		// check if has file extension and remove it
		int i = name.lastIndexOf('.');
		if (i != -1) {
			n = name.substring(0, i);
		}

		// check for sprite sheet extension and remove
		n = n.replaceFirst("_(\\d){1,3}(x(\\d){1,3})?$", "");

		return n;
	}

	public String getMetaExtension(String name) {
		String n = name;

		// check if has file extension and remove it
		int i = name.lastIndexOf('.');
		if (i != -1) {
			n = name.substring(0, i);
		}

		// check if has meta extension and separate it
		i = n.lastIndexOf('_');
		if (i != -1 && (i + 1) < n.length()) {
			n = n.substring(i + 1);
		}

		// if valid extension, return it or return null
		if (n.matches("(\\d){1,3}(x(\\d){1,3})?$")) {
			return n;
		}

		return null;
	}

	public String getFileExtension(String name) {
		int i = name.lastIndexOf('.');
		if (i != -1 && (i + 1) < name.length()) {
			return name.substring(i + 1);
		}

		return "";
	}

	private String getPureName(String name) {
		return name.replaceFirst("_(\\d){1,2}$", "");
	}

	public boolean exists(String name) {
		return (getFileIfExists(name) != null);
	}

	public boolean addPath(String p, boolean global) {
		p = p.replaceAll("\\\\", "/");
		if (!p.endsWith("/")) {
			p += "/";
		}

		if (!global) {
			if (privatePathes.contains(p)) {
				return false;
			}
			privatePathes.add(p);
			return true;
		}

		if (globalPathes.contains(p)) {
			return false;
		}
		globalPathes.add(p);
		return true;
	}

	public boolean addPath(String p) {
		return addPath(p, globalInstance);
	}

	public void setResourceMode(boolean b) {
		this.resourceMode = b;
	}

	public static void main(String[] args) {
		ImageLoader2 il = new ImageLoader2();
		il.addPath("com/tr/res/img");

		System.out.println(il.exists("full_1.png"));
		System.out.println(il.exists("full_0.png"));

		il.isCompleteName("test.png");
		System.out.println(il.exists("link"));

		System.out.println(il.getMetaExtension("test_12x16.png"));
		System.out.println(il.getMetaExtension("test_12x16"));
		System.out.println(il.getMetaExtension("test_12x"));
		System.out.println(il.getMetaExtension("test_12"));
		System.out.println(il.getMetaExtension("test.png"));

		System.out.println(il.isSpriteSheet(il
				.getFileIfExists("link_32x48.png")));
		System.out.println(il.isSpriteSheet(il.getFileIfExists("link")));

		System.out.println(il.isGifAnimation(il.getFileIfExists("knight")));

		System.out.println(MathUtility.isInteger(""));
	}

}
