package com.tr.engine.img.ani;

import com.tr.engine.img.TRImage;

public class TRFrameAction {
	public String path = "this";
	public int rotX = 0;
	public boolean rotXFlag = false;
	public int rotY = 0;
	public boolean rotYFlag = false;
	public int rotZ = 0;
	public boolean rotZFlag = false;
	public TRImage img = null;
	public boolean imgFlag = false;
	public int posX = 0;
	public boolean posXFlag = false;
	public int posY = 0;
	public boolean posZFlag = false;
	public int posZ = 0;
	public boolean posYFlag = false;
	public String loadPath = "this";
	public String loadName = "default";
	public boolean loadFlag = false;
	public boolean restoreFlag = false;
	
	public void apply(ITRAnimationView view){
		ITRAnimationView v = (ITRAnimationView) view.getComponentByName(path);
		System.out.println("Found #"+path+" : "+(v != null));
		if(v == null){
			return;
		}
		
		if(imgFlag){
			System.out.println("Setting image: "+img.getFullFileName());
			v.setImage(img);
		}
		
		if(rotXFlag){
			v.setXRotation(rotX);
		}
		
		if(rotYFlag){
			v.setYRotation(rotY);
		}
		
		if(rotZFlag){
			v.setZRotation(rotZ);
		}
		
		if(posXFlag){
			v.setX(posX);
		}
		
		if(posYFlag){
			v.setY(posY);
		}
		
		if(posZFlag){
			v.setZ(posZ);
		}
		
		if(loadFlag){
			((ITRAnimationView)v.getComponentByName(loadPath)).loadAnimation(loadName);
		}else if(restoreFlag){
			((ITRAnimationView)v.getComponentByName(loadPath)).restore();
		}
	}
}
