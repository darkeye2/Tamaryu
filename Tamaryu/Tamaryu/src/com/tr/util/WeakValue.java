package com.tr.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakValue<K, V> extends WeakReference<V> {
	private K key;

	public WeakValue(K key, V reference, ReferenceQueue<Object> q) {
		super(reference, q);
		this.key = key;
	}
	
	public K getKey(){
		return key;
	}

}
