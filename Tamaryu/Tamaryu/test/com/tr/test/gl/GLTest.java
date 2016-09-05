package com.tr.test.gl;

import com.tr.engine.components.gl.TRGLLabel;
import com.tr.engine.grf.TRGameWindow;
import com.tr.engine.grf.TRRenderContext;
import com.tr.engine.grf.TRScene;
import com.tr.engine.grf.gl.TRGLImageView;
import com.tr.engine.grf.gl.TRGLRenderContext;
import com.tr.engine.img.TRImage;

public class GLTest {
	private TRRenderContext rc;
	private TRGameWindow gw;
	private TRScene scene;
	
	//private TestTama2 t1= null;
	private TRGLImageView t1 = null;
	private TRGLImageView container = new TRGLImageView();
	
	public GLTest(){
		rc = new TRGLRenderContext(800, 600);	//create new RenderContext for OpenGL (GL4ES3 profile!)
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
		TRImage img = new TRImage("dancing", "dancing1", "png", "/img", 0, 0, 0, 600, 600, 600, 600);
		System.out.println("Image path: "+img.getFullFilePath());
		t1 = new TRGLImageView(img);
		t1.setSize(300, 300);
		//t1 = new TestTama2();
		t1.setRotation(0, 180, 0);
		//t1.setPosition(0.5f, 0.5f, 5f);
		TestGLTamaEye t2 = new TestGLTamaEye();
		t2.setName("test");
		t1.addComponent(t2);
		
		TRGLLabel l = new TRGLLabel("TEST");
		
		container.setSize(300, 300);
		container.addComponent(t1);
		//container.setScale(0.5f);
		
		scene.addComponent(container);
		scene.addComponent(l);
		System.out.println(t1.getComponentByName("test"));
		
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				int i = 0;
				int r = 0;
				boolean forward = true;
				boolean fr = true;
				while(true){
					//move
					container.setPosition(i, 0, 100);
					if(forward){
						i+=4;
					}else{
						i-=4;
					}
					if(i>=500f/container.getScale()){
						forward = false;
					}else if(i<=0){
						forward = true;
					}
					
					//rotate
					t1.setXRotation(r);
					if(fr){
						r+=2;
					}else{
						r-=2;
					}
					if(r>=20){
						fr = false;
					}else if(r<=0){
						fr = true;
					}
					
					try {
						Thread.sleep(33);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}});
		t.start();
		
		//start game window
		System.out.println("Starting Window!");
		gw.start();
	}
	
	
	
	public static void main(String[] args){
		new GLTest();
	}

}
