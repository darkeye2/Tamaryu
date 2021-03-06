package com.tr.engine.grf.gl;

import java.util.ArrayList;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLEventListener;
import com.tr.engine.debug.TRGLDebugger;
import com.tr.engine.grf.TRGameWindow;
import com.tr.engine.grf.TRRenderContext;
import com.tr.engine.grf.TRRenderPropertie;
import com.tr.engine.grf.TRScene;

public class TRGLRenderContext extends TRRenderContext {
	protected GL gl = null;
	protected TRGLDebugger debugConsole = null;
	protected Runnable onCloseAction = null;
	protected GLEventListener glel = null;
	protected KeyListener glkl = null;
	protected MouseListener glml = null;
	
	protected ArrayList<TRRenderPropertie> globalRenderProperties = new ArrayList<TRRenderPropertie>();
	
	public TRGLRenderContext(int sceneWidth, int sceneHeight){
		super(sceneWidth, sceneHeight);
		setDebug(this.debug);
	}
	
	public TRGLRenderContext(GL gl, int sceneWidth, int sceneHeight){
		this(sceneWidth, sceneHeight);
		this.gl = gl;
	}
	
	public void setDebug(boolean enable){
		debug = enable;
		if(enable){
			if(debugConsole == null){
				debugConsole = new TRGLDebugger(this);
			}
		}else{
			if(debugConsole != null){
				debugConsole.stop();
				debugConsole = null;
			}
		}
	}
	
	public void setGL(GL gl){
		this.gl = gl;
	}
	
	public GL getGL(){
		return this.gl;
	}
	
	public void setGLEventListener(GLEventListener glel){
		this.glel = glel;
	}
	
	public void setGLKeyListener(KeyListener glkl){
		this.glkl = glkl;
	}
	
	public void setGLMouseListener(MouseListener glml){
		this.glml = glml;
	}
	
	public boolean checkError(GL gl, String title) {
		int error = gl.glGetError();
		if (error != GL.GL_NO_ERROR) {
			String errorString;
			switch (error) {
			case GL.GL_INVALID_ENUM:
				errorString = "GL_INVALID_ENUM";
				break;
			case GL.GL_INVALID_VALUE:
				errorString = "GL_INVALID_VALUE";
				break;
			case GL.GL_INVALID_OPERATION:
				errorString = "GL_INVALID_OPERATION";
				break;
			case GL.GL_INVALID_FRAMEBUFFER_OPERATION:
				errorString = "GL_INVALID_FRAMEBUFFER_OPERATION";
				break;
			case GL.GL_OUT_OF_MEMORY:
				errorString = "GL_OUT_OF_MEMORY";
				break;
			default:
				errorString = "UNKNOWN";
				break;
			}
			System.err.println("OpenGL Error(" + errorString + "): " + title);
			throw new Error();
		}
		return error == GL.GL_NO_ERROR;
	}

	@Override
	public void init() {
		//create scene
		this.scene = new TRGLScene(this);
		this.scene.setSceneSize(this.sceneWidth, this.sceneHeight);
		
		//create window
		this.gameWindow = createWindow();
		
		
		// TODO Auto-generated method stub

	}

	@Override
	public void destruct() {
		this.gameWindow.stop();
		this.debugConsole.stop();
		this.scene.clearScene();
		if(onCloseAction != null)
			onCloseAction.run();
		System.exit(0);
	}
	
	public void addOnClose(Runnable close){
		onCloseAction = close;
	}

	@Override
	public void refresh() {
		System.out.println("[INFO] no refresh available for GLRenderContext!");
	}

	@Override
	public TRGameWindow createWindow() {
		if(this.gameWindow == null){
			this.gameWindow = new TRGLGameWindow(this, glel, glkl, glml, 800, 600);
		}

		return this.gameWindow;
	}

	@Override
	public TRScene getScene() {
		return this.scene;
	}

}
