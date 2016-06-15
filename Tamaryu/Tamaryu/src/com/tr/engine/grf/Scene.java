package com.tr.engine.grf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Scene extends JComponent {
	private static final long serialVersionUID = 1L;
	
	//size
	protected Dimension stageSize;		//unscaled size
	protected Dimension minDispSize;	//minimal display size (start scrolling if size < mindispsize)
	
	//scaling factor
	protected float scale = 1.0f;
	
	//background
	protected ITRBackground bg;
	
	//scroll properties
	protected Point scrollPos = new Point(0,0);
	protected boolean scrolling = false;
	
	//rendering
	protected boolean doubleBuffering = true;
	protected boolean useRenderingThread = true;
	protected boolean autoRepaint = true;
	protected boolean smoothScrolling = true;
	protected boolean fadeoutText = true;
	
	//rendering components
	protected BufferedImage prebufferedImage;
	protected Graphics2D pbGraphics;
	protected boolean bufferReady = false;
	
	//components
	protected ArrayList<Component> components = new ArrayList<Component>();
	
	
	public Scene(ITRBackground bg, int stageWidth, int stageHeight){
		this.bg = bg;
		this.stageSize = new Dimension(stageWidth, stageHeight);
	}
	
	
	
	/** METHODS FOR PREPARE RENDERING AND RENDER SCENE 
	 * Following methods are used for preparing the scene
	 * for rendering and draw it on a graphics object
	 *  - void reorderComponents();
	 *  - void updateFOComponents();
	 *  - void paintScene(Graphics2D);
	 */
	public void reorderComponents(){
		//change component order depending on position
	}
	
	public void updateFOComponents(){
		//update fade-out components (set new transparency and position)
	}
	
	public void paintScene(Graphics2D g){
		//if double buffered and buffer still full, return
		if(this.doubleBuffering && this.bufferReady){
			return;
		}
		
		//prepare graphics object (set rendering hints)
		if(!this.doubleBuffering){
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}
		
		//paint background
		bg.paintComponent(g);
		
		//paint components
		for(Component c : components){
			c.paint(g);
		}
	}
	
	
	
	/** METHODS INHERITED FROM JCOMPONENT AND OVERWRITEN
	 * Following methods overwrite JComponent functions.
	 *  - Component add(Component);
	 *  - void remove(Component);
	 *  - void removeAll();
	 */
	public Component add(Component c){
		if(c != null){
			components.add(c);
		}
		
		return c;
	}
	
	public void remove(Component c){
		int i  = components.indexOf(c);
		if(i > -1){
			components.remove(i);
		}
		super.remove(c);
	}
	
	
	public void removeAll(){
		components.clear();
		super.removeAll();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		if(this.doubleBuffering){
			if(this.bufferReady){
				g2d.drawImage(prebufferedImage, 0, 0, this);
				this.bufferReady = false;
			}
		}else{
			paintScene(g2d);
		}
		super.paintComponent(g);
	}
	
	

}
