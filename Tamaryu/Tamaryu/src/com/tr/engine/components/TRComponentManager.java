package com.tr.engine.components;

import com.tr.engine.components.gl.TRGLComponentSet;
import com.tr.engine.grf.IRenderable;

public final class TRComponentManager {
	private static TRAbstractComponentSet set = new TRGLComponentSet();
	private static IRenderable selected = null;
	
	public static void setComponentSet(TRAbstractComponentSet cs){
		set = cs;
	}
	
	public static void setSelected(IRenderable i){
		selected = i;
	}
	
	public static IRenderable getSelected(){
		return selected;
	}
	
	public static TRLabel getLabel() {
		return set.getLabel();
	}

	public static TRLabel getLabel(String txt) {
		return set.getLabel(txt);
	}

	public static TRTextButton getTxtButton() {
		return set.getTxtButton();
	}

	public static TRTextButton getTxtButton(String txt) {
		return set.getTxtButton(txt);
	}

	public static TRButton getButton() {
		return set.getButton();
	}

	public static TRButton getButton(String txt) {
		return set.getButton(txt);
	}
	
	public static ITRInputField getInputField() {
		return set.getInputField();
	}
	
	public static ITRInputField getInputField(String txt) {
		return set.getInputField(txt);
	}
}
