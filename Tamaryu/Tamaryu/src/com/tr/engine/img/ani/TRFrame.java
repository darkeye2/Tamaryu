package com.tr.engine.img.ani;

import java.util.ArrayList;

public class TRFrame {
	public int duration = 0;
	protected ArrayList<TRFrameAction> actions = new ArrayList<TRFrameAction>();
	
	public void addAction(TRFrameAction f){
		actions.add(f);
	}
	
	public void apply(ITRAnimationView v){
		for(TRFrameAction a : actions){
			a.apply(v);
		}
	}
}
