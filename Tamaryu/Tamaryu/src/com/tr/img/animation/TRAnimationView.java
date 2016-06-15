package com.tr.img.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.BorderFactory;

import com.tr.img.TRImageView;
import com.tr.img.filter.Filter;
import com.tr.util.GraphicsUtility;

public class TRAnimationView extends TRImageView implements
		Comparable<TRAnimationView> {
	private static final long serialVersionUID = 1L;

	// map available animations
	protected HashMap<String, TRAnimation> animationMap = new HashMap<String, TRAnimation>();

	// active animation
	protected TRAnimation curAni = null;
	protected String curAniKey = "";

	// task for updating the animation
	protected TimerTask updateTask = null;
	
	//
	protected float scale = 0;

	// call repaint automatically on image update
	protected boolean autorepaint = true;

	protected int index = 0;

	public TRAnimationView(String name) {
		super();
		this.setLayout(null);
		this.setName(name);
		//this.setBorder(BorderFactory.createLineBorder(Color.blue));
	}

	public TRAnimationView() {
		this(null);
	}

	@Override
	public int compareTo(TRAnimationView arg0) {
		return this.index - arg0.index;
	}

	public TRAnimation get(String name) {
		if (animationMap.containsKey(name)) {
			return animationMap.get(name);
		}

		return null;
	}

	public void startAnimation() {
		if (updateTask != null) {
			updateTask.cancel();
		}
		createUpdateTask();
		GraphicsUtility.getTimer().schedule(updateTask, 0,
				1000 / curAni.getFrameRate());
	}

	public void pauseAnimation() {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}

	public void stopAnimation() {
		pauseAnimation();
		if (curAni != null) {
			curAni.reset();
			update();
		}
	}

	public boolean loadAnimation(String aniKey, boolean start) {
		// return if key is unknown
		if (!animationMap.containsKey(aniKey)) {
			return false;
		}

		// stop running animation
		stopAnimation();

		// set new Animation
		curAni = animationMap.get(aniKey);
		curAni.reset();
		curAniKey = aniKey;

		// start animation if requested
		if (start) {
			startAnimation();
		}

		return true;
	}

	public boolean addAnimation(String key, TRAnimation ani, boolean start) {
		// return false if key is already used.
		if (animationMap.containsKey(key)) {
			return false;
		}

		// add animation to map
		animationMap.put(key, ani);

		if (start) {
			loadAnimation(key, true);
		}

		return true;
	}

	public boolean addAnimation(String key, TRAnimation ani) {
		return addAnimation(key, ani, false);
	}

	// create a TimerTask for updating the displayed image
	private void createUpdateTask() {
		updateTask = new TimerTask() {
			@Override
			public void run() {
				update();
			}
		};
	}

	public void update() {
		// if image not null, set next image
		if (curAni != null) {
			setImage(curAni.nextImage());
		}

		// if autorepaint is set, call paint
		if (autorepaint) {
			this.repaint();
		}
	}

	public void setAutoRepaint(boolean b) {
		this.autorepaint = b;
	}

	public void resetFilter() {
		for (TRAnimation ani : animationMap.values()) {
			ani.resetFilter();
		}
	}

	public void applyFilter() {
		for (TRAnimation ani : animationMap.values()) {
			ani.applyFilter();
		}
	}

	public void addFilter(Filter f) {
		for (TRAnimation ani : animationMap.values()) {
			ani.addFilter(f);
		}
	}

	public void removeFilter(Filter f) {
		for (TRAnimation ani : animationMap.values()) {
			ani.removeFilter(f);
		}
	}

	public void removeAllFilter() {
		for (TRAnimation ani : animationMap.values()) {
			ani.removeAllFilter();
		}
	}

	public void removeFilterType(Filter f) {
		for (TRAnimation ani : animationMap.values()) {
			ani.removeFilterType(f);
		}
	}

	public String getAnimationKey() {
		return curAniKey;
	}

	/*public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*if (deg == 0) {
			super.paintComponent(g);
			g.setColor(Color.blue);
			g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		} else {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(Color.blue);
			g2d.fillOval(getAx(), getAy(), 5, 5);
			System.out.println("Rotation: "+getAx()+", "+getAy());
			//g2d.rotate(Math.toRadians(deg), 240, 80);
			g2d.rotate(Math.toRadians(deg), getAx(), getAy());
			super.paintComponent(g2d);
			g2d.setColor(Color.blue);
			g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
			g2d.dispose();
		}*/
	//}*/

}
