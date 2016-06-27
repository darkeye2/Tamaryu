package com.tr.img.animation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tr.img.filter.Filter;

public class TRComplexAnimationView extends TRAnimationView {
	private static final long serialVersionUID = 1L;
	
	protected LinkedHashMap<String, TRAnimationView> views = new LinkedHashMap<String, TRAnimationView>();
	protected HashMap<String, TRComplexAnimation> anis = new HashMap<String, TRComplexAnimation>();
	
	protected float scale = 1;
	
	//animation
	protected TRComplexAnimation curAni = null;
	
	public TRComplexAnimationView(int width, int height, float scale, TRAnimationView[] v, TRComplexAnimation[] a, String key){
		this.setPreferredSize(new Dimension(width,height));
		
		for(TRAnimationView av : v){
			views.put(av.getName(), av);
		}
		
		/*for(TRComplexAnimation ca : a){
			anis.put(ca.getName(), ca);
		}*/
		
		curAni = anis.get(key);
	}
	
	public TRComplexAnimationView(int width, int height, float scale){
		this.setPreferredSize(new Dimension(width,height));
		this.scale = scale;
	}
	
	public TRComplexAnimationView(int width, int height){
		this(width, height, 1);
	}
	
	public TRComplexAnimationView(String name){
		super(name);
	}
	
	public TRComplexAnimationView(){
		super();
	}
	
	
	public Component add(Component c){
		if(c instanceof TRAnimationView){
			views.put(c.getName(), (TRAnimationView) c);
			System.out.println("Add item: "+c.getName());
			return c;
		}else{
			return super.add(c);
		}
	} 
	
	
	/*---  SIZE ---*/
	
	public void setScale(float s){
		scale = s;
		for(TRAnimationView av : views.values()){
			av.setScale(s);
		}
		super.setScale(s);
	}
	
	public void setPreScale(float s){
		scale = s;
		for(TRAnimationView av : views.values()){
			av.setPreScale(s);
		}
		super.setPreScale(s);
	}
	
	/*public void setWidth(int w){
		this.setPreferredSize(new Dimension(w, this.getHeight()));
	}
	
	public void setHeight(int h){
		this.setPreferredSize(new Dimension(this.getWidth(), h));
	}
	
	public void setSize(int w, int h){
		this.setPreferredSize(new Dimension(w, h));
	}
	
	public void setSize(Dimension d){
		setSize(d.width, d.height);
	}
	
	
	public int getWidth(){
		return (int) (super.getWidth()*this.scale);
	}
	
	public int getHeight(){
		return (int) (super.getHeight()*this.scale);
	}
	
	public Dimension getSize(){
		return new Dimension(getWidth(), getHeight());
	}*/
	
	
	public TRAnimationView getView(String name){
		return views.get(name);
	}
	
	
	public void resetFilter() {
		for (TRAnimationView ani : views.values()) {
			ani.resetFilter();
		}
	}

	public void applyFilter() {
		for (TRAnimationView ani : views.values()) {
			ani.applyFilter();
		}
	}

	public void addFilter(Filter f) {
		for (TRAnimationView ani : views.values()) {
			ani.addFilter(f);
		}
	}

	public void removeFilter(Filter f) {
		for (TRAnimationView ani : views.values()) {
			ani.removeFilter(f);
		}
	}

	public void removeAllFilter() {
		for (TRAnimationView ani : views.values()) {
			ani.removeAllFilter();
		}
	}

	public void removeFilterType(Filter f) {
		for (TRAnimationView ani : views.values()) {
			ani.removeFilterType(f);
		}
	}
	
	protected void paintTRComponent(Graphics2D g){
		super.paintComponent(g);
		for(TRAnimationView trav : views.values()){
			Point p = trav.getPosition();
			g.translate(p.x, p.y);
			trav.paintComponent(g);
			g.translate(-p.x, -p.y);
		}
	}
	
	
	public void paintComponent(Graphics g){
		if(!this.transform){
			this.transformImage = null;
			paintTRComponent((Graphics2D) g);
		}else{
			if(transformImage.getWidth() != getWidth() ||
					transformImage.getHeight() != getHeight()){
				this.updateTransform();
			}
			Graphics2D imgG = ((Graphics2D)transformImage.getGraphics());
			int posX = getX(), posY = getY();
			imgG.setBackground(new Color(255, 255, 255, 0));
			imgG.clearRect(0, 0, transformImage.getWidth(), transformImage.getHeight());
			paintTRComponent((Graphics2D) transformImage.getGraphics());
			Graphics2D g2d = (Graphics2D) g.create();
			AffineTransform tx;
			if(this.flipHor && this.flipVert){
				tx = AffineTransform.getScaleInstance(-1, -1);
				tx.translate(getWidth(), getHeight());
				posX *= -1;
				posY *= -1;
			}else if(this.flipVert){
				tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-transformImage.getWidth(null), 0);
				posX *= -1;
			}else{
				tx = AffineTransform.getScaleInstance(1, -1);
				tx.translate(0, -transformImage.getHeight(null));
				posY *= -1;
			}
			g2d.setTransform(tx);
			g2d.drawImage(transformImage, posX, posY, this);
			g2d.dispose();
		}
	}

}
