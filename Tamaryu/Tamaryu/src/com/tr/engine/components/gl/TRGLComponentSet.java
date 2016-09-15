package com.tr.engine.components.gl;

import com.tr.engine.components.ITRInputField;
import com.tr.engine.components.TRAbstractComponentSet;
import com.tr.engine.components.TRButton;
import com.tr.engine.components.TRLabel;
import com.tr.engine.components.TRTextButton;

public class TRGLComponentSet extends TRAbstractComponentSet {

	@Override
	public TRLabel getLabel() {
		return new TRGLLabel();
	}

	@Override
	public TRLabel getLabel(String txt) {
		return new TRGLLabel(txt);
	}

	@Override
	public TRTextButton getTxtButton() {
		return new TRGLTextButton();
	}

	@Override
	public TRTextButton getTxtButton(String txt) {
		return new TRGLTextButton(txt);
	}

	@Override
	public TRButton getButton() {
		return null;
	}

	@Override
	public TRButton getButton(String txt) {
		return null;
	}

	@Override
	public ITRInputField getInputField() {
		return new TRGLInputField();
	}

	@Override
	public ITRInputField getInputField(String txt) {
		return null;
	}

}
