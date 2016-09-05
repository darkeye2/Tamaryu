package com.tr.engine.components;

public abstract class TRAbstractComponentSet {
	public abstract TRLabel getLabel();
	public abstract TRLabel getLabel(String txt);
	public abstract TRTextButton getTxtButton();
	public abstract TRTextButton getTxtButton(String txt);
	public abstract TRButton getButton();
	public abstract TRButton getButton(String txt);
}
