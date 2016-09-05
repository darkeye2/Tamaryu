package com.tr.engine.core;

import java.util.ArrayList;

import com.tr.engine.gameobject.AbstractGameObject;

public class TRGameLooper {
	private int targetUPS = 60;
	private float curUPS = 0;
	private long targetTime = 1000 / targetUPS;
	private long lastUPSUpdate = 0;
	private int upsCount = 0;
	private final long MIN_WAIT_TIME = 10;
	private final int UPS_UPDATE_INTERVAL = 500;
	

	private volatile long startTime = 0;
	private volatile long endTime = 0;
	private volatile long elapsed;
	private volatile long wait;

	private Thread loopThread;
	private volatile boolean running = false;
	private volatile boolean paused = false;
	
	private volatile ArrayList<AbstractGameObject> objects = new ArrayList<AbstractGameObject>();
	private volatile Object lock = new Object();
	
	public TRGameLooper() {
		start();
	}
	
	public void clear(){
		synchronized(lock){
			objects.clear();
		}
	}
	
	public void add(AbstractGameObject o){
		synchronized(lock){
			objects.add(o);
		}
	}
	
	public void remove(AbstractGameObject o){
		synchronized(lock){
			if(objects.contains(o))
				objects.add(o);
		}
	}
	
	public void setTargetUPS(int ups){
		if(ups > 0 && ups < 500){
			targetUPS = ups;
			targetTime = Math.round(1000f/targetUPS);
		}
	}
	
	public int getTargetUPS(){
		return this.targetUPS;
	}
	
	public float getUPS(){
		return this.curUPS;
	}

	public boolean isRunning() {
		return (this.running && !this.paused);
	}
	
	public boolean stop(){
		this.running = false;
		sleep(100);
		return true;
	}
	
	public boolean start(){
		if(running)
			return false;
		if(this.paused){
			this.paused = false;
		}else{
			this.running = true;
			loopThread = new Thread(new LoopRun());
			loopThread.start();
		}
		return true;
	}
	
	public boolean pause(){
		if(!running)
			return false;
		this.paused = true;
		return true;
	}
	
	private class LoopRun implements Runnable{
		private long ct = 0;

		@Override
		public void run() {
			while(running){
				if(paused){
					sleep(3*targetTime);
					continue;
				}
				startTime = System.currentTimeMillis();
				ct = System.currentTimeMillis();
				synchronized(lock){
					for(AbstractGameObject go : objects){
						go.update(ct);
					}
				}
				upsCount++;
				if((startTime - lastUPSUpdate) > UPS_UPDATE_INTERVAL){
					lastUPSUpdate = startTime;
					curUPS = upsCount/(float)(startTime - lastUPSUpdate);
					upsCount = 0;
				}
				endTime = System.currentTimeMillis();
				elapsed = endTime - startTime;
				
				wait = Math.max(MIN_WAIT_TIME, (targetTime - elapsed));
				sleep(wait);
			}
		}
		
	}

	public void sleep(long time) {
		try {
			if (time > 0) {
				Thread.sleep(time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
