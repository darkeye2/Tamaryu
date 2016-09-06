package com.tr.engine.grf.gl;

import java.util.HashMap;

import com.tr.engine.grf.TRRenderContext;
import com.tr.engine.img.ani.ITRAnimationView;
import com.tr.engine.img.ani.TRAnimation;
import com.tr.engine.img.ani.TRFrame;

public class TRGLAnimationView extends TRGLImageView implements ITRAnimationView {
	
	protected HashMap<String, TRAnimation> anis = new HashMap<String, TRAnimation>();
	protected String lastAni = "default";
	protected String curAniName = "default";
	protected TRAnimation ani = null;
	protected boolean running = false;

	@Override
	public TRAnimation get(String name) {
		if(anis.containsKey(name))
			return anis.get(name);
		return null;
	}
	
	public void render(TRRenderContext context) {
		if(running){
			if(ani != null && ani.frameReady()){
				long t = System.currentTimeMillis();
				long t2 = t+ani.getNextFrameDuration();
				TRFrame f = ani.getNextFrame();
				if(f != null)
					ani.getNextFrame().apply(this);
				ani.setFrameTimes(t, t2);
			}
		}
		super.render(context);
	}

	@Override
	public void start() {
		this.running = true;
	}

	@Override
	public void pause() {
		this.running = false;
	}
	
	public void unloadAnimation(){
		if(this.ani != null && ani.getCloseFrame() != null){
			ani.getCloseFrame().apply(this);
		}
	}

	@Override
	public void loadAnimation(String name, int fps) {
		if(this.anis.containsKey(name)){
			unloadAnimation();
			lastAni = curAniName;
			curAniName = name;
			ani = this.get(name);
			ani.setFixedFPS(fps);
			if(ani.getInitFrame() != null)
				ani.getInitFrame().apply(this);
		}
	}

	@Override
	public void loadAnimation(String name) {
		if(this.anis.containsKey(name)){
			unloadAnimation();
			lastAni = curAniName;
			curAniName = name;
			ani = this.get(name);
			if(ani.getInitFrame() != null)
				ani.getInitFrame().apply(this);
		}
	}

	@Override
	public void addAnimation(String name, TRAnimation ani) {
		this.anis.put(name, ani);
	}

	@Override
	public void restore() {
		this.loadAnimation(lastAni);
	}

	@Override
	public void loadDefault() {
		this.loadAnimation("default");
	}

}
