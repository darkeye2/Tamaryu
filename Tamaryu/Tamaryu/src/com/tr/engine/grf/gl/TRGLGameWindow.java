package com.tr.engine.grf.gl;

import com.jogamp.nativewindow.util.Dimension;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.tr.engine.grf.TRGameWindow;
import com.tr.engine.grf.TRRenderContext;

public class TRGLGameWindow extends TRGameWindow{
	//window and animator
	public static GLWindow glWindow;
	public static Animator animator;
	
	public TRGLGameWindow(TRGLRenderContext rc, GLEventListener glel, int winWidth, int winHeight){
		super(rc);
		windowSize = new Dimension(winWidth, winHeight);
		Display display = NewtFactory.createDisplay(null);
		Screen screen = NewtFactory.createScreen(display, screenIdx);
		//GLProfile glProfile = GLProfile.get(GLProfile.GL4ES3);
		GLProfile glProfile;
		if(GLProfile.isAvailable("GL4ES3")){
			glProfile = GLProfile.get("GL4ES3");
		}else{
			glProfile = GLProfile.getDefault();
		}
		System.out.println("[GL Version] "+glProfile.getImplName());
		GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		glCapabilities.setAlphaBits(2);
		glCapabilities.setSampleBuffers(true);
		glCapabilities.setNumSamples(4);
		glWindow = GLWindow.create(screen, glCapabilities);

		glWindow.setSize((int) windowSize.getWidth(),
				(int) windowSize.getHeight());
		glWindow.setPosition(50, 50);
		glWindow.setUndecorated(undecorated);
		glWindow.setAlwaysOnTop(alwaysOnTop);
		glWindow.setFullscreen(fullscreen);
		glWindow.setPointerVisible(mouseVisible);
		glWindow.confinePointer(mouseConfined);
		glWindow.addGLEventListener(glel);
		//glWindow.addKeyListener(this);

		animator = new Animator(glWindow);
		//animator.start();
	}

	@Override
	public void setDecorated(boolean decoration) {
		this.undecorated = !decoration;
		glWindow.setUndecorated(this.undecorated);
	}

	@Override
	public void setAlwaysOnTop(boolean alwaysontop) {
		this.alwaysOnTop = alwaysontop;
		glWindow.setAlwaysOnTop(this.alwaysOnTop);
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMouseVisibility(boolean mouseVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMouseConfidation(boolean mouseConfined) {
		// TODO Auto-generated method stub
		
	}
	
	public void setTitel(String titel){
		super.setTitel(titel);
		glWindow.setTitle(TITEL);
	}
	
	public void setTempTitel(String titel){
		glWindow.setTitle(titel);
	}

	@Override
	public void start() {
		animator.setUpdateFPSFrames(30, null);
		animator.start();
		glWindow.setVisible(true);
	}

	@Override
	public void pause() {
		animator.pause();
	}

	@Override
	public void stop() {
		animator.pause();
		glWindow.setVisible(false);
	}

	@Override
	public void showFPSinTitel(boolean show) {
		this.showFPS = show;
		if(!show){
			this.setTitel(this.getTitel());
		}
	}
	
	public boolean showFPSinTitel(){
		return this.showFPS;
	}

	public Animator getAnimator() {
		return animator;
	}

	


}
