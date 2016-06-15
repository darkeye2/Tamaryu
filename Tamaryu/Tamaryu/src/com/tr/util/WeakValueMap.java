package com.tr.util;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;

public class WeakValueMap<K,V>{
	
	protected HashMap<K, WeakValue<K,V>> map = new HashMap<K, WeakValue<K,V>>();
	protected final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();


	public WeakValueMap() {
		
    }
	
	public void put(K key, V value){
		clearReferences();
		map.put(key, new WeakValue<K, V>(key, value, queue));
	}
	
	public V get(K key){
		clearReferences();
		if(map.containsKey(key)){
			return map.get(key).get();
		}else{
			return null;
		}
	}
	
	public boolean contains(K key){
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
	
	@SuppressWarnings("unchecked")
	protected void clearReferences(){
		WeakValue<K,V> wk;
		//int s = map.size();
		while((wk = (WeakValue<K,V>)queue.poll()) != null){
			//System.out.println("[WeakValueMap] Remove unreferenced Object: "+wk.getKey());
			map.remove(wk.getKey());
			wk = null;
		}
		//System.out.println("Remove unreferenced items! ("+s+" / "+map.size()+")");
	}
	
    
    


}
