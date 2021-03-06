package com.tr.engine.img.ani;

import com.tr.engine.img.TRImage;

public class TRFrameAction {
	public String path = "this";
	public ITRAnimationView incRef = null;
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
	public int h = 0;
	public boolean hFlag = false;
	public int incX = 0;
	public boolean incXFlag = false;
	public int incY = 0;
	public boolean incYFlag = false;
	public int incZ = 0;
	public boolean incZFlag = false;
	public String loadPath = "this";
	public String loadName = "default";
	public boolean loadFlag = false;
	public boolean restoreFlag = false;
	
	public void apply(ITRAnimationView view){
		ITRAnimationView v = (ITRAnimationView) view.getComponentByName(path);
		if(v == null){
			return;
		}
		
		if(imgFlag){
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
			//System.out.println("Change z ("+v.getName()+") to: "+posZ);
			v.setZ(posZ);
		}
		
		if(incXFlag){
			if(incRef != null){
				v.setX(incRef.getPosition().x+incX);
			}else{
				v.setX(v.getPosition().x+incX);
			}
		}
		
		if(incYFlag){
			if(incRef != null){
				v.setY(incRef.getPosition().y+incY);
			}else{
				v.setY(v.getPosition().y+incY);
			}
		}
		
		if(incZFlag){
			if(incRef != null){
				v.setZ(incRef.getPosition().z+incZ);
			}else{
				v.setZ(v.getPosition().z+incZ);
			}
		}
		
		if(hFlag){
			v.setSize(v.getWidth(), h);
		}
		
		if(loadFlag){
			((ITRAnimationView)v.getComponentByName(loadPath)).loadAnimation(loadName);
		}else if(restoreFlag){
			((ITRAnimationView)v.getComponentByName(loadPath)).restore();
		}
	}
}
