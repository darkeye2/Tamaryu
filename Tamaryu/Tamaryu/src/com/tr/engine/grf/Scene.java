package com.tr.engine.grf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.tr.util.GraphicsUtility;

public class Scene extends JComponent implements ComponentListener {
	private static final long serialVersionUID = 1L;

	// background constants
	public static final int BG_ANCHOR_CENTER = 0x01;
	public static final int BG_ANCHOR_TOP = 0x02;
	public static final int BG_ANCHOR_BOTTOM = 0x03;
	public static final int BG_ANCHOR_LEFT = 0x04;
	public static final int BG_ANCHOR_RIGHT = 0x05;
	public static final int BG_ANCHOR_TOP_LEFT = 0x06;
	public static final int BG_ANCHOR_TOP_RIGHT = 0x07;
	public static final int BG_ANCHOR_BOTTOM_LEFT = 0x08;
	public static final int BG_ANCHOR_BOTTOM_RIGHT = 0x09;
	public static final int BG_ANCHOR_FULL = 0x0A;

	// size
	protected Dimension stageSize; // unscaled size
	protected Dimension minStageSize; // minimal stage size (start scrolling if
										// size < minStageSize)

	// scaling factor
	protected float scale = 1.0f;

	// background
	protected ITRBackground bg;

	// scroll properties
	protected Point scrollPos = new Point(0, 0);
	protected boolean scrolling = false;

	// rendering properties
	protected boolean doubleBuffering = false;
	protected boolean useRenderingThread = false;
	protected boolean autoRepaint = false;
	protected boolean smoothScrolling = true;
	protected boolean fadeoutText = true;
	
	// rendering controlls
	protected int targetFps = 25;				//fps, the rendering thread tries to reach
	protected int possibleFps = 25;				//maximal possible fps, calculated.
	protected int sleepTime = 40;			//sleep time at targetFps
	protected long renderingStart = 0;			//start time of rendering method
	protected long renderingStop = 0;			//stop time of rendering method
	protected int renderingOverflow = 0;		//sleep = sleepTime - renderingOverflow
	protected int framesSkipped = 0;			//amount of frames, have to be skipped because of renderingOverflow
	protected int maxFrameSkip = 2;				//maximal frames, can be skipped, without dropping frame rate
	protected long measureTimeStart = 0;		//start time of fps measuring
	protected long measureTimeStop = 0;			//stop time of fps measuring
	protected long elapsedTime = 0;				//measureTimeStop - measureTimeStart
	protected int fpsCounter = 0;				//counter for fps
	protected int lastFps = 0;					//last calculated fps

	// rendering components
	protected volatile BufferedImage preRenderedImage;
	protected volatile BufferedImage preRenderedImage2;
	protected volatile BufferedImage renderedImage;
	protected volatile Object renderLock = new Object();
	protected Graphics2D prGraphics = null;
	protected Graphics2D prGraphics2 = null;
	protected volatile boolean bufferReady = false;
	protected Thread renderingThread = null;
	protected TimerTask repaintTask = null;
	
	// debug components
	protected JLabel debugLabel = new JLabel();

	// components
	protected ArrayList<Component> components = new ArrayList<Component>();

	public Scene(ITRBackground bg, int stageWidth, int stageHeight) {
		this.bg = bg;
		this.stageSize = new Dimension(stageWidth, stageHeight);
		this.setLayout(null);
		
		debugLabel.setBounds(5, 5, getWidth(), 25);
		debugLabel.setOpaque(false);
		super.add(debugLabel);

		// rendering properties
		if (this.doubleBuffering) {
			this.preRenderedImage = new BufferedImage(stageWidth + 1,
					stageHeight + 1, BufferedImage.TYPE_INT_ARGB);
			if (this.preRenderedImage.getGraphics() != null) {
				this.prGraphics = (Graphics2D) this.preRenderedImage
						.getGraphics();
			} else {
				this.prGraphics = (Graphics2D) this.preRenderedImage
						.createGraphics();
			}
			this.prGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			this.prGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			//bufferReady = false;
		}

		// component listener
		this.addComponentListener(this);
	}
	
	
	
	/**
	 * METHODS FOR RENDERING CONTROLL
	 * Set and get render properties.
	 *  - public void setTargetFPS(int)
	 *  - public int getTargetFPS()
	 *  - public int getLastFPS();
	 *  - public int getPossibleFPS()
	 *  - public void setDoubleBuffering(boolean)
	 *  - public boolean isDoubleBuffered()
	 *  - public void useRenderingThread(boolean)
	 *  - public boolean isRenderingThreadUsed()
	 *  - public void setAutoRepaint(boolean)
	 *  - public boolean isAutoRepainting()
	 *  - public void setTextFading(boolean)
	 *  - public boolean isTextFading()
	 *  - public void setSmoothScrolling(boolean)
	 *  - public boolean isSmoothScrolling()
	 */
	public void setTargetFPS(int fps){
		this.targetFps = fps;
		this.possibleFps = fps;
		this.sleepTime = Math.round(1000f/fps);
	}
	
	public int getTargetFPS(){
		return this.targetFps;
	}
	
	public int getLastFPS(){
		return this.lastFps;
	}
	
	public int getPossibleFPS(){
		return this.possibleFps;
	}
	
	public void setDoubleBuffering(boolean enable){
		super.setDoubleBuffered(true);
		this.doubleBuffering = enable;
		
		this.reinitBuffer();
		
		if(this.doubleBuffering && this.useRenderingThread && this.renderingThread == null){
			this.createRenderingThread();
			this.renderingThread.start();
		}
	}
	
	public boolean isDoubleBuffered(){
		return this.doubleBuffering;
	}
	
	public void useRenderingThread(boolean enable){
		if(enable && !this.useRenderingThread){
			this.createRenderingThread();
			this.useRenderingThread = enable;
			this.renderingThread.start();
		}
		
		this.useRenderingThread = enable;
	}
	
	public boolean isRenderingThreadUsed(){
		return this.useRenderingThread;
	}
	
	public void setAutoRepaint(boolean enable){
		if(enable && !this.autoRepaint){
			this.createRepaintTask();
			GraphicsUtility.getTimer().schedule(this.repaintTask, this.sleepTime, this.sleepTime);
		}
		
		this.autoRepaint = enable;
	}
	
	public boolean isAutoRepainting(){
		return this.autoRepaint;
	}
	
	public void setTextFading(boolean enable){
		this.fadeoutText = enable;
	}
	
	public boolean isTextFading(){
		return this.fadeoutText;
	}
	
	public void setSmoothScrolling(boolean enable){
		this.smoothScrolling = enable;
	}
	
	public boolean isSmoothScrolling(){
		return this.smoothScrolling;
	}
	
	

	/**
	 * METHODS FOR CREATING WORKER THREADS o. TASKS
	 * Following methods create and initialize worker
	 * Threads and Tasks for rendering, repainting,
	 * controlling and updating components.
	 *  - void createRenderingThread();
	 *  - void createRepaintTask();
	 */
	protected void createRenderingThread(){
		renderingThread = new Thread(new Runnable(){

			@Override
			public void run() {
				while(doubleBuffering && useRenderingThread){
					if(!bufferReady){
						render(prGraphics);
						renderedImage = preRenderedImage;
						bufferReady = true;
					}else{
						render(prGraphics2);
						renderedImage = preRenderedImage2;
						bufferReady = false;
					}
					try {
						Thread.sleep(Math.max(0, sleepTime - renderingOverflow));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				renderingThread = null;
				
			}});
	}
	
	protected void createRepaintTask(){
		repaintTask = new TimerTask(){

			@Override
			public void run() {
				if(!autoRepaint){
					this.cancel();
					repaintTask = null;
				}else{
					repaint();
				}
			}};
	}

	/**
	 * METHODS FOR PREPARE RENDERING AND RENDER SCENE 
	 * Following methods are used for preparing the scene 
	 * for rendering and draw it on a graphics object 
	 *  - void reinitBuffer();
	 *  - void reorderComponents();
	 *  - void updateFOComponents(); 
	 *  - void paintScene(Graphics2D);
	 *  - void render(Graphics2D ?); ({@link #render(Graphics2D g) render})
	 */
	public void reinitBuffer(){
		if (this.doubleBuffering) {
			this.preRenderedImage = new BufferedImage(this.getWidth(),
					this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			if (this.preRenderedImage.getGraphics() != null) {
				this.prGraphics = (Graphics2D) this.preRenderedImage
						.getGraphics();
			} else {
				this.prGraphics = (Graphics2D) this.preRenderedImage
						.createGraphics();
			}
			
			this.preRenderedImage2 = new BufferedImage(this.getWidth(),
					this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			if (this.preRenderedImage2.getGraphics() != null) {
				this.prGraphics2 = (Graphics2D) this.preRenderedImage2
						.getGraphics();
			} else {
				this.prGraphics2 = (Graphics2D) this.preRenderedImage2
						.createGraphics();
			}
			
			bufferReady = false;
		}
	}
	
	public void reorderComponents() {
		// change component order depending on position
	}

	public void updateFOComponents() {
		// update fade-out components (set new transparency and position)
	}

	public void paintScene(Graphics2D g) {
		// if double buffered and buffer still full, return
		/*if (this.doubleBuffering && this.bufferReady) {
			return;
		}*/

		// prepare graphics object (set rendering hints)
		if (!this.doubleBuffering) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
		}

		// paint background
		bg.setTime(0, 0, 0);
		bg.paintComponent(g);

		// paint components
		for (Component c : components) {
			g.translate(c.getX(), c.getY());
			c.paint(g);
			g.translate(-c.getX(), -c.getY());
		}

		if (this.doubleBuffering) {
			//this.bufferReady = true;
		}
	}
	
	/* This method counts FPS and control rendering.
	 * If doubleBuffering is enabled, the parameter should be null.
	 * If renderingThread is enabled, this method will be called
	 * automatically from the thread to render on the buffer.
	 * 
	 * FPS control is only available by using the renderingThread. 
	 * If using own rendering thread, you have to take care of 
	 * proper FPS control and rendering speed.
	 * 
	 * If doubleBuffering is disabled, this method just count
	 * FPS and call paintScene without calculating FPS control
	 * states.
	 * */
	public void render(Graphics2D g){
		//rendering start time
		this.renderingStart = System.currentTimeMillis();
		debugLabel.setText("FPS: "+lastFps+"          Target: "+possibleFps+" / "+targetFps);
		
		//update cur fps counter
		this.fpsCounter++;
		this.elapsedTime = (this.measureTimeStop = System.currentTimeMillis()) - this.measureTimeStart;
		if( this.elapsedTime >= 500){
			this.lastFps  =  Math.round( this.fpsCounter*1000f / this.elapsedTime );
			
			if(this.elapsedTime > 1200){
				this.fpsCounter = 0;
				this.measureTimeStart = System.currentTimeMillis();
			}
		}
		
		//if no renderingThread, call paint scene and return
		if(!this.useRenderingThread){
			if(g != null){
				this.paintScene(g);
			}else if(this.doubleBuffering){
				this.paintScene(this.prGraphics);
			}
		}else{
			//render
			this.paintScene(g);
			
			//calculate rendering time
			this.renderingOverflow = (int) ((this.renderingStop = System.currentTimeMillis()) - this.renderingStart);
			
			//calculate skipped frames
			this.framesSkipped = (int) (this.renderingOverflow / this.sleepTime);
			
			//drop frame rate if more frames skipped than maxFrameSkip
			// or rise fps if < targetFps and no frames skipped
			if(this.framesSkipped > this.maxFrameSkip){
				this.possibleFps -= (this.framesSkipped - this.maxFrameSkip); 
			}else if(this.framesSkipped == 0 && this.possibleFps < this.targetFps){
				this.possibleFps++;
			}
			
			//adjust sleep time
			this.sleepTime = Math.round(1000f/this.possibleFps);
		}
	}

	/**
	 * METHODS INHERITED FROM JCOMPONENT AND OVERWRITEN 
	 * Following methods overwrite JComponent functions. 
	 *  - Component add(Component); 
	 *  - void remove(Component); 
	 *  - void removeAll();
	 */
	public Component add(Component c) {
		if (c != null) {
			components.add(c);
		}

		return c;
	}

	public void remove(Component c) {
		int i = components.indexOf(c);
		if (i > -1) {
			components.remove(i);
		}
		super.remove(c);
	}

	public void removeAll() {
		components.clear();
		super.removeAll();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (this.doubleBuffering) {
			g2d.drawImage(renderedImage, 0, 0, this);
		} else {
			this.render(g2d);
		}
		super.paintComponent(g);
	}

	/**
	 * METHODS INHERITED FROM COMPONENTLISTENER 
	 * Following methods implement functions from 
	 * ComponentListener interface.
	 *  - void componentHidden(ComponentEvent) {@unused} 
	 *  - void componentMoved(ComponentEvent) {@unused} 
	 *  - void componentShown(ComponentEvent) {@unused} 
	 *  - void componentResized(ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// unused
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// unused
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		debugLabel.setBounds(5, 5, getWidth(), 25);
		// rendering properties
		this.reinitBuffer();

		// TODO Auto-generated method stub
		((JComponent) bg).setBounds(0, 0, this.getWidth(), this.getHeight());

	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// unused
	}

}
