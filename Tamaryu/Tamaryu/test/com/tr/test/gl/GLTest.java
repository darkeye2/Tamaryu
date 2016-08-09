package com.tr.test.gl;

import com.tr.engine.grf.TRGameWindow;
import com.tr.engine.grf.TRRenderContext;
import com.tr.engine.grf.TRScene;
import com.tr.engine.grf.gl.TRGLRenderContext;

public class GLTest {
	private TRRenderContext rc;
	private TRGameWindow gw;
	private TRScene scene;
	
	public GLTest(){
		rc = new TRGLRenderContext(800, 600);			//create new RenderContext for OpenGL (GL4ES3 profile!)
		rc.setDebug(true);						//enable debug mode (debug console)
		rc.init();								//initialize the context
		
		/* get game window for the context
		 * 
		 * - window size
		 * - full screen
		 * - mouse visibility
		 * - window titel
		*/ 
		gw = rc.createWindow();
		
		/* get scene for this context
		 * 
		 * - get / set FPS
		 * - add renderable components
		 */
		scene = rc.getScene();
		
		//TestGLTama t1 = new TestGLTama();
		TestTama2 t1 = new TestTama2();
		//t1.setRotation(0, 180, 10);
		//t1.setPosition(0.5f, 0.5f, 5f);
		TestGLTamaEye t2 = new TestGLTamaEye();
		t1.addComponent(t2);
		scene.addComponent(t1);
		
		//start game window
		System.out.println("Starting Window!");
		gw.start();
	}
	
	
	
	public static void main(String[] args){
		new GLTest();
	}

}
