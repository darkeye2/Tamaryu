package com.tr.util;

import java.util.HashMap;

import com.tr.img.gameobject.TRImage;

public class TRImageReferenceMap extends WeakValueMap<String, TRImageReference> {
	protected HashMap<String, TRImageReference> map = new HashMap<String, TRImageReference>();


	public TRImageReferenceMap() {
		
    }
	
	public TRImageReference createReference(TRImage img){
		return new TRImageReference(img, queue);
	}
	
	public void put(String key, TRImage value){
		int l = map.size();
		clearReferences();
		System.out.println("Adding to map! ("+l+" / "+map.size()+" / "+(1+map.size())+")");
		map.put(key, new TRImageReference(value, queue));
	}
	
	public TRImageReference get(String key){
		int l = map.size();
		clearReferences();
		System.out.println("Get Image! ("+l+" / "+map.size()+")");
		if(map.containsKey(key)){
			return map.get(key);
		}else{
			return null;
		}
	}
	
	public boolean contains(String key){
		clearReferences();
		return map.containsKey(key);
	}
	
	public int getSize(){
		clearReferences();
		return map.size();
	}
	
	public void clear(){
		map.clear();
		clearReferences();
	}
	
	protected void clearReferences(){
		TRImageReference wk;
		int c = 0;
		//int s = map.size();
		while((wk = ((TRImageReference) queue.poll())) != null){
			c++;
			//System.out.println("[WeakValueMap] Remove unreferenced Object: "+wk.getKey());
			map.remove(wk.getKey());
			wk = null;
		}
		if(c > 0){
			System.out.println(c+" unused references removed from map!");
		}
		//System.out.println("Remove unreferenced items! ("+s+" / "+map.size()+")");
	}
}
