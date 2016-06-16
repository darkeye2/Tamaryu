package com.tr.util;

import java.lang.ref.ReferenceQueue;

import javax.swing.ImageIcon;

import com.tr.img.gameobject.TRImage;

public class TRImageReference extends WeakValue<String, ImageIcon> {
	private TRImage i;
	
	public TRImageReference(TRImage rc, ReferenceQueue<Object> q){
		super(rc.getName(), rc.getOriginalImage(), q);
		this.i = rc;
		this.i.setImage(null);
	}
	
	public TRImage getTRImage(){
		return i;
	}

}
