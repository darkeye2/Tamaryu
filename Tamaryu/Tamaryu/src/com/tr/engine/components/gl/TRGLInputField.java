package com.tr.engine.components.gl;

import com.jogamp.newt.event.KeyEvent;
import com.tr.engine.components.ITRInputField;
import com.tr.engine.components.TRComponentManager;
import com.tr.engine.grf.TRRenderPropertie;
import com.tr.engine.img.TRImage;
import com.tr.engine.input.TRKeyEvent;

public class TRGLInputField extends TRGLLabel implements ITRInputField{
	private String allowedChars = new String("abcdefghijklmnopqrstuvwxyz1234567890φόδ");
	
	public TRGLInputField(){
		super();
		//this.setRenderPropertie(new TRRenderPropertie(TRRenderPropertie.USE_TEXTURE, 0, 1, 0,0,1));
	}

	@Override
	public void setFocus(boolean focus) {
		if(!focus){
			if(TRComponentManager.getSelected().equals(this)){
				TRComponentManager.setSelected(null);
			}
		}
	}

	@Override
	public boolean hasFocus() {
		return TRComponentManager.getSelected().equals(this);
	}

	@Override
	public void requestFocus() {
		TRComponentManager.setSelected(this);
	}

	@Override
	public void keyPressed(TRKeyEvent e) {
		if(e.e.isAutoRepeat() && e.e.getKeyCode() != KeyEvent.VK_BACK_SPACE)
			return;
		if(e.e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
			if(this.getText().length() > 0){
				this.setText(this.getText().substring(0, this.getText().length()-1));
			}
			return;
		}
		String c = ""+e.e.getKeyChar();
		if(allowedChars.contains(c)){
			this.setText(this.getText()+c);
		}
	}

	@Override
	public void keyReleased(TRKeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBackground(TRImage i) {
		// TODO Auto-generated method stub
		
	}

}
