package com.tr.engine.grf.gl;

import java.util.Comparator;

import com.tr.engine.grf.IRenderable;

public class RenderableComparator implements Comparator<IRenderable> {

	@Override
	public int compare(IRenderable arg0, IRenderable arg1) {
		return Math.round(arg0.getPosition().z - arg1.getPosition().z);
	}

}
