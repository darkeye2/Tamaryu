package com.tr.engine.components.gl;

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
		addStateChangeAction(MOUSE_CLICK_ACTION, r);
	}

	@Override
	public void addStateChangeAction(int state, Runnable r) {
		if(state >= 0 && state < TRTextButton.STATE_COUNT){
			stateAction[state] = r;
		}
	}
	
	@Override
	public void mouseEnter(TRMouseEvent e) {
		if(stateAction[TRTextButton.MOUSE_ENTER_ACTION] != null)
		stateAction[TRTextButton.MOUSE_ENTER_ACTION].run();
	}

	@Override
	public void mouseLeave(TRMouseEvent e) {
		if(stateAction[TRTextButton.MOUSE_LEAVE_ACTION] != null)
		stateAction[TRTextButton.MOUSE_LEAVE_ACTION].run();
	}

	@Override
	public void mouseRelease(TRMouseEvent e) {
		if(stateAction[TRTextButton.MOUSE_UP_ACTION] != null)
		stateAction[TRTextButton.MOUSE_UP_ACTION].run();
	}

	@Override
	public void mousePress(TRMouseEvent e) {
		if(stateAction[TRTextButton.MOUSE_DOWN_ACTION] != null)
		stateAction[TRTextButton.MOUSE_DOWN_ACTION].run();
		
	}
	
	@Override
	public void mouseClicked(TRMouseEvent e) {
		if(stateAction[TRTextButton.MOUSE_CLICK_ACTION] != null)
			stateAction[TRTextButton.MOUSE_CLICK_ACTION].run();
	}

	
	@Override
	public void mouseDragged(TRMouseEvent tre) {
		/*int  xoff = tre.x()-tre.lastPos.getX();
		int yoff = tre.y()-tre.lastPos.getY();
		this.setPosition((int)this.getPosition().x+xoff,(int) this.getPosition().y+yoff);*/
	}

	@Override
	public void mouseMoved(TRMouseEvent tre) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getZ() {
		return (int) this.getPosition().z;
	}

	@Override
	public IRenderable getSrc() {
		return this;
	}




}
