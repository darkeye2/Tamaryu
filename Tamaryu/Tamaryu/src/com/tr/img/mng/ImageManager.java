package com.tr.img.mng;

import java.io.IOException;
import java.util.TimerTask;

import com.tr.img.gameobject.TRImage;
import com.tr.util.GraphicsUtility;
import com.tr.util.QueueHashMap;
import com.tr.util.TRImageReference;
import com.tr.util.TRImageReferenceMap;

public class ImageManager {
	//image map
	private TRImageReferenceMap imgMap = new TRImageReferenceMap();
	
	//image cache
	private QueueHashMap<String, TRImage> imgCache = new QueueHashMap<String, TRImage>();
	
	//image loader
	private ImageLoader il;
	
	private long lastRequest = 0;				//timestamp of the last cache update
	private long cacheResetTime = 120000;		//clear cache after some time of inactivity
	
	private TimerTask cacheResetTask;
	
	//private Object lock = new Object();
	
	public ImageManager(ImageLoader il){
		this.il = il;
		lastRequest = System.currentTimeMillis();
		createCacheResetTask();
	}
	
	public TRImage get(String id, boolean reload) throws IOException{
		TRImageReference bi = null;
		System.out.println("Loaded Images: "+imgMap.getSize()+"; Cached Images: "+imgCache.size());
		
		//if image not allready loaded, try to load it
		if(reload || (!imgMap.contains(id) && !imgCache.containsKey(id))){
			if(!load(id)){
				return null;
			}
		}
		
		//if already loaded, return it
		bi = imgMap.get(id);
		if(bi != null){
			return new TRImage(bi.getKey(), bi.getTRImage().getIndex(), bi.get());
		}
		
		//if image is cached, map it and return
		if(imgCache.containsKey(id)){
			lastRequest = System.currentTimeMillis();
			imgMap.put(id, imgCache.remove(id));
			bi = imgMap.get(id);
			return new TRImage(bi.getKey(), bi.getTRImage().getIndex(), bi.get());
		}
		
		return null;
	}
	
	private boolean load(String id) throws IOException{
		boolean found = false;
		System.out.println("Image "+id+" have to be loaded!");
		if(il.isGifAnimation(id) || il.isSpriteSheet(id)){
			TRImage[] imgs = il.loadAll(id);
			for(TRImage i : imgs){
				if(i.getName().equalsIgnoreCase(id)){
					imgMap.put(i.getName(), i);
					found = true;
				}else{
					imgCache.put(i.getName(), i);
					lastRequest = System.currentTimeMillis();
				}
			}
		}else{
			TRImage i = il.load(id);
			if(i != null){
				imgMap.put(i.getName(),i);
				found = true;
			}
		}
		
		return found;
	}
	
	
	public TRImage get(String id) throws IOException{
		return get(id, false);
	}
	
	private void createCacheResetTask(){
		cacheResetTask = new TimerTask(){
			public void run() {
				if((System.currentTimeMillis()-lastRequest) > cacheResetTime){
					imgCache.clear();
					lastRequest = System.currentTimeMillis();
					System.out.println("Clearing Cache! Loaded: "+imgMap.getSize()+"; Cached: "+imgCache.size());
				}
			}
		};
		
		//check every 20 sec, if last cache request is older then resetTime
		// and delete cached items
		GraphicsUtility.getTimer().schedule(cacheResetTask, 20000, 20000);
	}

}
