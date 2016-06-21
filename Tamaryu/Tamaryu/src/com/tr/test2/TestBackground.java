package com.tr.test2;

import java.io.IOException;

import com.tr.engine.grf.ITRBackground;
import com.tr.img.gameobject.TRImage;
import com.tr.img.gameobject.TRImageView;
import com.tr.img.mng.ImageLoader;

public class TestBackground extends TRImageView implements ITRBackground {
	private static final long serialVersionUID = 1L;
	private final long dnCyclus = 48000;		//day - night cyclus in ms
	private final int[] imgTimes = new int[]{16000, 36000, 0};
	private TRImage[] imgs;
	

	private ImageLoader il = new ImageLoader();
	
	private int curTime = 0;

	public TestBackground() {
		
		il.addPath("img");
		try {
			imgs = new TRImage[]{il.load("savanna_bg_day.png"),il.load("savanna_bg_night.png"),
					il.load("savanna_bg_evening.png")};
		} catch (IOException e) {
			e.printStackTrace();
		}
		setImage(imgs[0]);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTime(int hh, int mm, int ss) {
		long time = System.currentTimeMillis();
		curTime = (int) (time%dnCyclus);
		//System.out.println(curTime);
		if(curTime > imgTimes[0] && curTime < imgTimes[1]){
			setImage(imgs[0]);
		}else if(curTime > imgTimes[1]){
			setImage(imgs[2]);
		}else{
			setImage(imgs[1]);
		}
	}


}
