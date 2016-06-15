package com.tr.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueueHashMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -9106461269354320411L;
	
	public static int DEFAULT_MAX_SIZE = 15;
	private int maxSize = DEFAULT_MAX_SIZE;
	
	public QueueHashMap(int maxSize){
		super();
		this.maxSize = maxSize;
	}
	
	public QueueHashMap(){
		this(DEFAULT_MAX_SIZE);
	}
	
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > maxSize;
    }

	public void setMaxSize(int m){
		if(m < 5){
			maxSize = DEFAULT_MAX_SIZE;
		}else{
			maxSize = m;
		}
	}

}
