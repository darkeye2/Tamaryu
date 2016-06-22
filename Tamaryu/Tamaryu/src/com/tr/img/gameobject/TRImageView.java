package com.tr.img.gameobject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.tr.img.filter.Filter;
import com.tr.util.GraphicsUtility;

public class TRImageView extends JComponent {
	private static final long serialVersionUID = 1L;

	/*
	 * Scaling Mode FIT: resize internal image to fit the component size, but
	 * considering the aspect ratio FILL: resize internal image to fit the
	 * component size, but not considering the aspect ratio ORG: resize the
	 * component to image size => scale only be setting scaling factor
	 */
	public static final int SCALE_MODE_FIT = 0x00;
	public static final int SCALE_MODE_FILL = 0x01;
	public static final int SCALE_MODE_ORG = 0x02;

	// properties
	protected TRImage img = null;
	protected int scalingMode = 1;
	protected Dimension size = new Dimension(0, 0);
	public int x = 0, y = 0;			//position in other object
	protected int ax = 0, ay = 0;		//anchor point (rotation point)
	protected float deg = 0;			//rotation degree (rotating around ax and ay);
	protected float scalingFactor = 1f;
	protected float preScale = 1f;

	public TRImageView(TRImage img, int scalingMode, int width, int height) {
		this.img = img;
		this.scalingMode = scalingMode;
		this.size = new Dimension(width, height);
		this.setLayout(null);
	}

	public TRImageView(TRImage img, int scalingMode) {
		this(img, scalingMode, img.width, img.height);
	}

	public TRImageView(TRImage img) {
		this(img, 0);
	}

	public TRImageView() {
		this.setLayout(null);
	}

	public void setScalingMode(int mode) {
		this.scalingMode = mode;
	}

	public void setImage(TRImage i) {
		this.img = i;
	}

	public void setScale(float s) {
		this.scalingFactor = s;
		repaint();
	}
	
	public void setPreScale(float s){
		this.preScale = s;
		repaint();
	}

	public Dimension getSize() {
		if (scalingMode == SCALE_MODE_ORG) {
			return new Dimension(Math.round(img.width * scalingFactor * preScale),
					Math.round(img.height * scalingFactor * preScale));
		}

		int dw = Math.round(size.width * scalingFactor * preScale);
		int dh = Math.round(size.height * scalingFactor * preScale);
		Dimension d = new Dimension(dw, dh);
		return d;
		
		//return super.getSize();
	}
	
	public Dimension getSize(Dimension d){
		return this.getSize();
	}
	
	public void setPreferredSize(Dimension d){
		super.setPreferredSize(d);
		this.size = d;
		System.out.println("new size: "+d);
	}
	
	public void setSize(int w, int h){
		setSize(new Dimension(w,h));
	}
	
	public void setSize(Dimension s){
		super.setSize(s.width, s.height);
		this.setPreferredSize(new Dimension(s));
	}
	
	public int getWidth(){
		return Math.round(scalingFactor*size.width * preScale);
	}
	
	public int getHeight(){
		return Math.round(scalingFactor*size.height * preScale);
	}

	public Dimension getPreferredSize() {
		if (scalingMode == SCALE_MODE_ORG) {
			return new Dimension(Math.round(img.width * scalingFactor * preScale),
					Math.round(img.height * scalingFactor * preScale));
		}
		
		return new Dimension(Math.round(size.width * scalingFactor * preScale),
				Math.round(size.height * scalingFactor * preScale));

		//return super.getPreferredSize();
	}
	
	public Point getPosition(){
		return new Point(Math.round(x*scalingFactor * preScale),Math.round(y*scalingFactor * preScale));
	}
	
	public int getX(){
		return Math.round(x*scalingFactor * preScale);
	}
	
	public int getOrcX(){
		return x;
	}
	
	public int getY(){
		return Math.round(y*scalingFactor * preScale);
	}
	
	public int getOrcY(){
		return y;
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setAnchor(int x, int y){
		this.ax = x;
		this.ay = y;
	}
	
	public void setRotation(float deg){
		this.deg = deg%360;
	}
	
	public Point getAnchor(){
		return new Point(Math.round(ax*scalingFactor * preScale), Math.round(ay*scalingFactor * preScale));
	}
	
	public int getAx(){
		return Math.round(ax*scalingFactor * preScale);
	}
	
	public int getAy(){
		return Math.round(ay*scalingFactor * preScale);
	}
	
	public void setBounds(int x, int y, int w, int h){
		super.setBounds(Math.round(scalingFactor*x * preScale), Math.round(scalingFactor*y * preScale), w, h);
		setPosition(x,y);
		size.width = w;
		size.height = h;
	}
	
	public Rectangle getBounds(Rectangle r){
		/*if(r != null){
			r.height = Math.round(r.height *scalingFactor);
			r.width = Math.round(r.width *scalingFactor);
			r.x = Math.round(r.x *scalingFactor);
			r.y = Math.round(r.y *scalingFactor);
			return r;
		}*/
		if(this.scalingMode == SCALE_MODE_ORG){
			return new Rectangle(Math.round(this.x*scalingFactor * preScale), Math.round(this.y*scalingFactor * preScale),
					Math.round(img.width * scalingFactor * preScale),
					Math.round(img.height * scalingFactor * preScale));
		}
		
		return new Rectangle(Math.round(this.x*scalingFactor * preScale), Math.round(this.y*scalingFactor * preScale),
				Math.round(size.width*scalingFactor * preScale), Math.round(size.height*scalingFactor * preScale));
	}
	
	public Rectangle getBounds(){
		return getBounds(null);
	}
	
	public void setBounds(Rectangle r){
		setBounds(r.x, r.y, r.width, r.height);
	}

	private BufferedImage toImage() {
		BufferedImage img = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		this.paint(g);
		g.dispose();

		return img;
	}

	public boolean isTransparent(int x, int y, int treshold) {
		BufferedImage bi = toImage();
		Color c = new Color(bi.getRGB(x, y), true);
		float[] hsl = new float[3];
		GraphicsUtility.rgbToHsl(c, hsl);
		System.out.println("Color: "+c+"  ["+hsl[0]+", "+hsl[1]+", "+hsl[2]+"]");

		return (c.getAlpha() <= treshold);
	}

	public boolean isTransparent(int x, int y) {
		return isTransparent(x, y, 30);
	}

	public void resetFilter() {
		if (img != null) {
			img.resetFilter();
		}
	}

	public void applyFilter() {
		if (img != null) {
			img.applyFilter();
		}
	}

	public void addFilter(Filter f) {
		if (img != null) {
			img.addFilter(f);
		}
	}

	public void removeFilter(Filter f) {
		if (img != null) {
			img.removeFilter(f);
		}
	}

	public void removeAllFilter() {
		if (img != null) {
			img.removeAllFilter();
		}
	}

	public void removeFilterType(Filter f) {
		if (img != null) {
			img.removeFilterType(f);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) {
			// get graphics object and setup antialising
			Graphics2D g2d;
			if(deg != 0){
				g2d = (Graphics2D) g.create();
				g2d.rotate(Math.toRadians(deg), getAx(), getAy());
			}else{
				g2d = (Graphics2D) g;
			}
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// calculate image size
			double pW = this.getSize().width, pH = this.getSize().height;

			double iWidth, iHeight;

			// padding to center image
			int pL = 0, pT = 0;

			if (scalingMode == SCALE_MODE_FIT) {
				double ratio = 0;
				double rP = pH / pW, rI = (double) img.getSize().height
						/ (double) img.getSize().width;

				if (rP < rI) {
					ratio = pH / img.getSize().height;
				} else {
					ratio = pW / img.getSize().width;
				}

				iWidth = (int) (img.getSize().width * ratio);
				iHeight = (int) (img.getSize().height * ratio);

				pL = (int) ((pW - iWidth) / 2);
				pT = (int) ((pH - iHeight) / 2);
			} else {
				iWidth = (int) pW;
				iHeight = (int) pH;
			}

			// draw image
			g2d.drawImage(img.getImageIcon().getImage(), pL, pT, (int) iWidth,
					(int) iHeight, this);
			
			if(deg != 0){
				g2d.dispose();
			}
		}
	}

}
