package com.tr.engine.img.ani;

import com.tr.engine.img.ITRImageView;

public interface ITRAnimationView extends ITRImageView {
	public TRAnimation get(String name);
	public void start();
	public void pause();
	public void loadAnimation(String name, int fps);
	public void loadAnimation(String name);
	public void addAnimation(String name, TRAnimation ani);
	public void restore();
	public void loadDefault();
}
