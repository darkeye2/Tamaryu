package com.tr.engine.components.gl;

import com.jogamp.opengl.util.packrect.Rect;
import com.tr.engine.components.TRTextButton;
import com.tr.engine.grf.IRenderable;
import com.tr.engine.input.TRMouseEvent;

public class TRGLTextButton extends TRGLLabel implements TRTextButton {
	protected Runnable[] stateAction = new Runnable[TRTextButton.STATE_COUNT];
	
	public TRGLTextButton(){
		super();
	}
	
	public TRGLTextButton(String s){
		super(s);
	}

	@Override
	public void addClickAction(Runnable r) {
		stateAction[TRTextButton.MOUSE_UP_ACTION] = r;
	}

	@Override
	public void addStateChangeAction(int state, Runnable r) {
		if(state >= 0 && state < TRTextButton.STATE_COUNT){
			stateAction[state] = r;
		}
	}
	
	@Override
	public void mouseEnter(TRMouseEvent e) {
		stateAction[TRTextButton.MOUSE_ENTER_ACTION].run();
	}

	@Override
	public void mouseLeave(TRMouseEvent e) {
		stateAction[TRTextButton.MOUSE_LEAVE_ACTION].run();
	}

	@Override
	public void mouseRelease(TRMouseEvent e) {
		stateAction[TRTextButton.MOUSE_UP_ACTION].run();
	}

	@Override
	public void mousePress(TRMouseEvent e) {
		stateAction[TRTextButton.MOUSE_DOWN_ACTION].run();
		
	}

	@Override
	public Rect getHitbox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHitbox(Rect hitbox) {
		this.hitbox = hitbox;
	}

	@Override
	public int getZ() {
		return Math.round(this.getPosition().z);
	}
	
	@Override
	public void mouseDragged(TRMouseEvent tre) {
		int  xoff = tre.x()-tre.lastPos.getX();
		int yoff = tre.y()-tre.lastPos.getY();
		this.setPosition((int)this.getPosition().x+xoff,(int) this.getPosition().y+yoff);
	}

	@Override
	public boolean isHit(int x, int y) {
		if(x >= getAbsolutPosition().x+hitbox.x() && x <= getAbsolutPosition().x+hitbox.x()+hitbox.maxX()){
			if(y >= getAbsolutPosition().y+hitbox.y() && y <= getAbsolutPosition().y+hitbox.y()+hitbox.maxY()){
				//System.out.println("Hit a Component!");
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseMoved(TRMouseEvent tre) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IRenderable getSrc() {
		return this;
	}

}
