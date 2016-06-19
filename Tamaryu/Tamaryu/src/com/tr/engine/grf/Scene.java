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

import javax.swing.JComponent;

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

	// rendering
	protected boolean doubleBuffering = false;
	protected boolean useRenderingThread = true;
	protected boolean autoRepaint = true;
	protected boolean smoothScrolling = true;
	protected boolean fadeoutText = true;

	// rendering components
	protected BufferedImage preRenderedImage;
	protected Graphics2D prGraphics;
	protected boolean bufferReady = false;

	// components
	protected ArrayList<Component> components = new ArrayList<Component>();

	public Scene(ITRBackground bg, int stageWidth, int stageHeight) {
		this.bg = bg;
		this.stageSize = new Dimension(stageWidth, stageHeight);

		// rendering properties
		if (this.doubleBuffering) {
			this.preRenderedImage = new BufferedImage(stageWidth, stageHeight,
					BufferedImage.TYPE_INT_ARGB);
			if (this.preRenderedImage.getGraphics() != null) {
				this.prGraphics = (Graphics2D) this.preRenderedImage
						.getGraphics();
			} else {
				this.prGraphics = (Graphics2D) this.preRenderedImage
						.createGraphics();
			}
			bufferReady = false;
		}

		// component listener
		this.addComponentListener(this);
	}

	/**
	 * METHODS FOR PREPARE RENDERING AND RENDER SCENE Following methods are used
	 * for preparing the scene for rendering and draw it on a graphics object -
	 * void reorderComponents(); - void updateFOComponents(); - void
	 * paintScene(Graphics2D);
	 */
	public void reorderComponents() {
		// change component order depending on position
	}

	public void updateFOComponents() {
		// update fade-out components (set new transparency and position)
	}

	public void paintScene(Graphics2D g) {
		// if double buffered and buffer still full, return
		if (this.doubleBuffering && this.bufferReady) {
			return;
		}

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
			c.paint(g);
		}

		if (this.doubleBuffering) {
			this.bufferReady = true;
		}
	}

	/**
	 * METHODS INHERITED FROM JCOMPONENT AND OVERWRITEN Following methods
	 * overwrite JComponent functions. - Component add(Component); - void
	 * remove(Component); - void removeAll();
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
			if (this.bufferReady) {
				g2d.drawImage(preRenderedImage, 0, 0, this);
				this.bufferReady = false;
			}
		} else {
			paintScene(g2d);
		}
		super.paintComponent(g);
	}

	/**
	 * METHODS INHERITED FROM COMPONENTLISTENER Following methods implement
	 * functions from ComponentListener interface. - void
	 * componentHidden(ComponentEvent) {@unused} - void
	 * componentMoved(ComponentEvent) {@unused} - void
	 * componentShown(ComponentEvent) {@unused} - void
	 * componentResized(ComponentEvent)
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
		// rendering properties
		if (this.doubleBuffering) {
			this.preRenderedImage = new BufferedImage(this.getWidth(), this.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			if (this.preRenderedImage.getGraphics() != null) {
				this.prGraphics = (Graphics2D) this.preRenderedImage
						.getGraphics();
			} else {
				this.prGraphics = (Graphics2D) this.preRenderedImage
						.createGraphics();
			}
			bufferReady = false;
		}

		// TODO Auto-generated method stub
		((JComponent) bg).setBounds(0, 0, this.getWidth(), this.getHeight());

	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// unused
	}

}
