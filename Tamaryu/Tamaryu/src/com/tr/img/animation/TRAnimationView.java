package com.tr.img.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.TimerTask;

import com.tr.img.gameobject.TRImageView;
import com.tr.img.filter.Filter;
import com.tr.util.GraphicsUtility;

public class TRAnimationView extends TRImageView implements
		Comparable<TRAnimationView>{
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
	
	//transformation
	protected BufferedImage transformImage = null;
	protected boolean transform = false;
	protected boolean flipVert = false;
	protected boolean flipHor = false;

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
	
	public void setFlipVert(boolean b){
		this.flipVert = b;
		updateTransform();
	}
	
	public void setFlipHor(boolean b){
		this.flipHor = b;
		updateTransform();
	}
	
	protected void updateTransform(){
		if(this.flipHor || this.flipVert){
			this.transform = true;
			this.transformImage = new BufferedImage(Math.max(1, getWidth()), Math.max(1, getHeight()), BufferedImage.TYPE_INT_ARGB);
		}else if(!this.flipHor || !this.flipVert){
			this.transform = false;
			//transformImage = null;
		}
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

	public void paintComponent(Graphics g) {
		if(!this.transform){
			this.transformImage = null;
			super.paintComponent(g);
		}else{
			if(transformImage.getWidth() != getWidth() ||
					transformImage.getHeight() != getHeight()){
				this.updateTransform();
			}
			Graphics2D imgG = ((Graphics2D)transformImage.getGraphics());
			imgG.setBackground(new Color(255, 255, 255, 0));
			imgG.clearRect(0, 0, transformImage.getWidth(), transformImage.getHeight());
			super.paintComponent(transformImage.getGraphics());
			Graphics2D g2d = (Graphics2D) g.create();
			AffineTransform tx;
			if(this.flipHor && this.flipVert){
				tx = AffineTransform.getScaleInstance(-1, -1);
				tx.translate(getWidth(), getHeight());
			}else if(this.flipVert){
				tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-transformImage.getWidth(null), 0);
			}else{
				tx = AffineTransform.getScaleInstance(1, -1);
				tx.translate(0, -transformImage.getHeight(null));
			}
			g2d.setTransform(tx);
			g2d.drawImage(transformImage, 0, 0, this);
			g2d.dispose();
		}
	}

}
