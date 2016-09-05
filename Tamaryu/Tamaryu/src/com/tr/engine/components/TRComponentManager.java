package com.tr.engine.components;

import com.tr.engine.components.gl.TRGLComponentSet;

public final class TRComponentManager {
	private static TRAbstractComponentSet set = new TRGLComponentSet();
	
	public static void setComponentSet(TRAbstractComponentSet cs){
		set = cs;
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
}
