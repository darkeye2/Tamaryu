package com.tr.engine.components;

import com.tr.engine.img.TRImage;
import com.tr.engine.input.ITRKeyListener;

public interface ITRInputField extends TRLabel, ISelectable, ITRKeyListener{
	
	public void setBackground(TRImage i);
	
}
