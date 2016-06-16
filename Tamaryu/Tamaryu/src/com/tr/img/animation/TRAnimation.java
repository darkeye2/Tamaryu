package com.tr.img.animation;

import com.tr.img.gameobject.TRImage;
import com.tr.img.filter.Filter;

public class TRAnimation {

	private TRImage[] i;
	private int fps = 0;

	// current values
	private int curIndex = 0;
	private boolean endless = true;
	private boolean finished = false;

	public TRAnimation(TRImage[] imgs, int framerate, boolean cycle) {
		i = imgs;
		fps = framerate;
		endless = cycle;
	}

	public TRAnimation(TRImage[] imgs, int framerate) {
		this(imgs, framerate, true);
	}

	public TRImage getImage(int c) {
		System.out.println("Get Image: "+c+"; ("+i.length+")");
		if (c >= 0 && c < i.length) {
			return i[c];
		}
		return null;
	}

	public TRImage nextImage() {
		// return null if no images there
		if (i == null) {
			return null;
		}

		// update image counter
		if (!endless && (curIndex + 1) >= i.length) {
			finished = true;
			return i[0];
		}
		curIndex = (curIndex + 1) % i.length;

		// return image
		return i[Math.max(0, curIndex)];
	}

	public int getFrameRate() {
		return fps;
	}

	public void setFrameRate(int fps) {
		this.fps = fps;
	}

	public int getLength() {
		return i.length;
	}

	public boolean isFinished() {
		return finished;
	}

	public void reset() {
		finished = false;
		curIndex = 0;
	}

	public void setLoop(boolean b) {
		this.endless = b;
	}

	public void resetFilter() {
		for (TRImage img : i) {
			img.resetFilter();
		}
	}

	public void applyFilter() {
		for (TRImage img : i) {
			img.applyFilter();
		}
	}

	public void addFilter(Filter f) {
		for (TRImage img : i) {
			img.addFilter(f);
		}
	}

	public void removeFilter(Filter f) {
		for (TRImage img : i) {
			img.removeFilter(f);
		}
	}

	public void removeAllFilter() {
		for (TRImage img : i) {
			img.removeAllFilter();
		}
	}

	public void removeFilterType(Filter f) {
		for (TRImage img : i) {
			img.removeFilterType(f);
		}
	}

}
