package com.tr.engine.img.ani;

import java.util.ArrayList;

public class TRAnimation {
	protected int  fixedFPS = 0;
	protected long frameStart = 0;
	protected long frameEnd = 0;
	
	protected boolean loop = true;
	protected boolean ended = false;
	
	protected TRFrame initFrame = null;
	protected TRFrame closeFrame = null;
	protected ArrayList<TRFrame> frames = new ArrayList<TRFrame>();
	
	protected int framePointer = 0;
	
	public void setInitFram(TRFrame f){
		this.initFrame = f;
	}
	
	public void setCloseFrame(TRFrame f){
		this.closeFrame = f;
	}
	
	public void setFrameTimes(long start, long stop){
		this.frameStart = start;
		this.frameEnd = stop;
	}
	
	public boolean frameReady(){
		if(frameEnd <= 0)
			return true;
		return System.currentTimeMillis()>=frameEnd;
	}
	
	public void stop(){
		framePointer = 0;
		fixedFPS = 0;
	}
	
	public void addFrame(TRFrame f){
		this.frames.add(f);
	}
	
	public TRFrame getInitFrame(){
		return this.initFrame;
	}
	
	public void setLoop(boolean b){
		this.loop = b;
		if(b)
			this.ended = false;
	}
	
	public void reset(){
		this.framePointer = 0;
		this.ended = false;
	}
	
	public int getFixedFPS(){
		return this.fixedFPS;
	}
	
	public void setFixedFPS(int  fps){
		this.fixedFPS = fps;
	}
	
	public TRFrame getCloseFrame(){
		return this.closeFrame;
	}
	
	public int getNextFrameDuration(){
		if(fixedFPS > 0){
			return Math.round(1000f/fixedFPS);
		}
		if(frames.size() > 0 || framePointer < frames.size()){
			return frames.get(framePointer).duration;
		}
		
		return 100;
	}
	
	public TRFrame getNextFrame(){
		if(!loop && ended)
			return null;
		
		TRFrame f = null;
		//System.out.println("Playing frame: "+framePointer);
		if(frames.size() > 0 || framePointer < frames.size()){
			f = frames.get(framePointer);
			updatePointerPos();
			return f;
		}
		return null;
	}
	
	protected void updatePointerPos(){
		if(loop){
			framePointer++;
			framePointer %= frames.size();
		}else{
			if(framePointer < frames.size()-1){
				framePointer++;
			}else{
				ended = true;
			}
		}
	}
}
