package com.tr.engine.obj;

import com.tr.img.animation.TRAnimationView;

public abstract class TRGameObject {
	protected int posX = 0, posY = 0;
	protected TRAnimationView view = null;
	
	public TRGameObject(){
		
	}
	
	public abstract void update();
	
	public int getX(){
		return posX;
	}
	
	public int getY(){
		return posY;
	}
	
	public void setX(int x){
		this.posX = x;
		//TODO update view
	}
	
	public void setY(int y){
		this.posY = y;
		//TODO update view
	}

	public void setPos(int x, int y){
		this.posX = x;
		this.posY = y;
		//TODO update view
	}
	
	public TRAnimationView getView(){
		return view;
	}
}
