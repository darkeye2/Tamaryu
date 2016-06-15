package com.tr.test;

import java.util.ArrayList;

import com.tr.util.WeakValueMap;

public class WeakValueMapTest {
	WeakValueMap<String, Object> map = new WeakValueMap<String, Object>();
	ArrayList<Object> refList = new ArrayList<Object>();
	int kc = 0;
	
	public WeakValueMapTest(){
		addReferencedObjects(2);
		addUnreferencedObjects(3);
		sleep(4000);
		addReferencedObjects(2);
		addUnreferencedObjects(3);
		sleep(4000);
		System.out.println("Calling GC");
		System.gc();
		sleep(4000);
		System.out.println("Map size (total / expected): "+map.getSize()+" / "+"4");
	}
	
	private void addUnreferencedObjects(int  x){
		System.out.println("[WeekValueMapTest] addUnreferencedObjects("+x+")");
		while(x > 0){
			x--;
			map.put("NewKey_"+kc, new Object());
			kc ++;
		}
	}
	
	private void addReferencedObjects(int x){
		System.out.println("[WeekValueMapTest] addReferencedObjects("+x+")");
		while(x > 0){
			x--;
			Object o = new Object();
			refList.add(o);
			map.put("NewKey_"+kc, o);
			kc ++;
		}
	}
	
	private void sleep(long l){
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WeakValueMapTest();
	}

}
