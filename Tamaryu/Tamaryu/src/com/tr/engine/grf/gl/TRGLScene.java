package com.tr.engine.grf.gl;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;

import com.jogamp.nativewindow.util.Point;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.packrect.Rect;
import com.tr.engine.components.ISelectable;
import com.tr.engine.components.TRComponentManager;
import com.tr.engine.grf.IRenderable;
import com.tr.engine.grf.TRGameWindow;
import com.tr.engine.grf.TRScene;
import com.tr.engine.input.ITRGlobalMouseListener;
import com.tr.engine.input.ITRKeyListener;
import com.tr.engine.input.ITRMouseListener;
import com.tr.engine.input.TRDroparea;
import com.tr.engine.input.TRGlobalMouseEvent;
import com.tr.engine.input.TRKeyEvent;
import com.tr.engine.input.TRMouseEvent;
import com.tr.gl.core.GLCamera;
import com.tr.gl.core.GLProgramManager;
import com.tr.gl.core.GLProgramm;
import com.tr.gl.core.Semantic;

public class TRGLScene extends TRScene implements GLEventListener, KeyListener, MouseListener {

	protected TRGLRenderContext glRc;

	// rendering (ogl)
	protected int[] objects = new int[Semantic.Object.SIZE];
	protected GLProgramm programm = null;

	// rendering properties
	protected boolean rpChanged = true;
	protected boolean rpWireMode = false;
	protected boolean rpAntiAlising = true;

	protected ArrayList<IRenderable> unInitObjects = new ArrayList<IRenderable>();
	protected ArrayList<IRenderable> outObjects = new ArrayList<IRenderable>();
	protected ArrayList<IRenderable> temp = new ArrayList<IRenderable>();
	protected Object outLock = new Object();

	// input
	protected Point lastMousePos = new Point(0, 0);
	protected TRMouseEvent lastMouseEvent = new TRMouseEvent(null, null);

	private Rect viewPort = new Rect(0, 0, 0, 0, null);

	public TRGLScene(TRGLRenderContext context) {
		super(context);
		glRc = context;
		glRc.setGLEventListener(this);
		glRc.setGLKeyListener(this);
		glRc.setGLMouseListener(this);
		this.cam = new GLCamera(800, 600, 0, 0, 0);
		this.programm = new GLProgramm("/shader/", new String[] { "default_pp_v", "default_pp_f" },
				new int[] { GL2ES3.GL_VERTEX_SHADER, GL2ES3.GL_FRAGMENT_SHADER }, "default_pp");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addComponent(IRenderable ra) {
		if (ra == null)
			return;
		unInitObjects.add(ra);
		if (ra instanceof ITRMouseListener) {
			this.addMouseListener((ITRMouseListener) ra);
		}
		// TODO add to key listener
	}

	@Override
	public boolean removeComponent(IRenderable ra) {
		if (ra.getParent() != null) {
			ra.getParent().removeComponent(ra);
			return true;
		}
		if (ra instanceof ITRMouseListener) {
			this.removeMouseListener((ITRMouseListener) ra);
		}
		if (unInitObjects.contains(ra)) {
			return unInitObjects.remove(ra);
		} else {
			synchronized (outLock) {
				ra.removeAll();
				return this.outObjects.add(ra);
			}
		}
	}

	@Override
	public void clearScene() {
		mlisteners.clear();
		unInitObjects.clear();
		synchronized (lock) {
			components.clear();
		}
	}

	@Override
	public void setBackground(IRenderable bg) {
		this.bg = bg;
	}

	@Override
	public float getFPS() {
		return ((TRGLGameWindow) glRc.createWindow()).getAnimator().getLastFPS();
	}

	@Override
	public void setTargetFPS(int fps) {
		// TODO Auto-generated method stub

	}

	public void setAntialising(boolean b) {
		this.rpAntiAlising = b;
		this.rpChanged = true;
	}

	public void setWireMode(boolean b) {
		this.rpWireMode = b;
		this.rpChanged = true;
	}

	public void updateRenderingProperties(GL2ES2 gl) {
		if (rpAntiAlising) {
			System.out.println("Enable AnitAlising!");
			gl.glEnable(GL2.GL_POLYGON_SMOOTH);
			gl.glEnable(GL2.GL_LINE_SMOOTH);
			gl.glEnable(GL2.GL_BLEND);
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL2.GL_NICEST);
			gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
			gl.glEnable(GL2.GL_MULTISAMPLE);
			gl.glEnable(GL2.GL_SAMPLE_SHADING);
		} else {
			System.out.println("Disable AnitAlising!");
			gl.glDisable(GL2.GL_LINE_SMOOTH);
			gl.glDisable(GL2.GL_BLEND);
			gl.glDisable(GL2.GL_POLYGON_SMOOTH);
			gl.glDisable(GL2.GL_MULTISAMPLE);
		}

		if (!gl.isGLES()) {
			if (rpWireMode) {
				System.out.println("Enable WireMode!");
				((GL2GL3) gl).glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
			} else {
				System.out.println("Disable WireMode!");
				((GL2GL3) gl).glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			}
		}

		rpChanged = false;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2ES2 gl = (GL2ES2) drawable.getGL();

		gl.glFrontFace(GL.GL_CW);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);

		// init fbo
		gl.glGenFramebuffers(1, objects, Semantic.Object.FBO);
		gl.glBindFramebuffer(GL2ES2.GL_FRAMEBUFFER, objects[Semantic.Object.FBO]);

		// generate texture for fbo
		gl.glGenTextures(1, objects, Semantic.Object.TEXTURE);
		gl.glBindTexture(GL2ES2.GL_TEXTURE_2D, objects[Semantic.Object.TEXTURE]);
		gl.glTexImage2D(GL2ES2.GL_TEXTURE_2D, 0, GL2ES2.GL_RGB, 800, 600, 0, GL2ES2.GL_RGB, GL2ES2.GL_UNSIGNED_BYTE,
				null);
		gl.glTexParameteri(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_MIN_FILTER, GL2ES2.GL_LINEAR);
		gl.glTexParameteri(GL2ES2.GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_MAG_FILTER, GL2ES2.GL_LINEAR);

		// add texture to fbo
		gl.glFramebufferTexture2D(GL2ES2.GL_FRAMEBUFFER, GL2ES2.GL_COLOR_ATTACHMENT0, GL2ES2.GL_TEXTURE_2D,
				objects[Semantic.Object.TEXTURE], 0);

		// generate depth and stencil buffer for fbo
		gl.glGenRenderbuffers(1, objects, Semantic.Object.RBO);
		gl.glBindRenderbuffer(GL2ES2.GL_RENDERBUFFER, objects[Semantic.Object.RBO]);
		gl.glRenderbufferStorage(GL2ES2.GL_RENDERBUFFER, GL2ES2.GL_DEPTH24_STENCIL8, 800, 600);

		// add render buffer to fbo
		gl.glFramebufferRenderbuffer(GL2ES2.GL_FRAMEBUFFER, GL2ES3.GL_DEPTH_STENCIL_ATTACHMENT, GL2ES2.GL_RENDERBUFFER,
				objects[Semantic.Object.RBO]);

		// unbind fbo
		gl.glBindFramebuffer(GL2ES2.GL_FRAMEBUFFER, 0);

		// load pp programm
		programm = GLProgramManager.loadProgram((GL2ES3) gl, programm);

		// init vertex buffer object
		gl.glGenBuffers(1, objects, Semantic.Object.VBO);

		// init vertex array object
		((GL2ES3) gl).glGenVertexArrays(1, objects, Semantic.Object.VAO);

		// bind VAO
		((GL2ES3) gl).glBindVertexArray(objects[Semantic.Object.VAO]);

		// bind VBO
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[Semantic.Object.VBO]);

		// define vertex data in XYZ format
		float[] data = new float[] { 1f, 1f, 0f, -1f, 1f, 0f, 1f, -1f, 0f, 1f, -1f, 0f, -1f, 1f, 0f, -1f, -1f, 0f };

		// load vertex data into the buffer
		gl.glBufferData(GL.GL_ARRAY_BUFFER, data.length * 4, FloatBuffer.wrap(data), GL.GL_STATIC_DRAW);

		// bind attrribute
		gl.glVertexAttribPointer(gl.glGetAttribLocation(programm.getID(), "position"), 3, GL.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(1);

		// unbind vao
		((GL2ES3) gl).glBindVertexArray(0);

		gl.glViewport(0, 0, 800, 600);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL2ES2 gl = (GL2ES2) drawable.getGL();

		// delete fbo, fbo texture and fbo renderbuffer
		gl.glDeleteFramebuffers(1, objects, Semantic.Object.FBO);
		gl.glDeleteTextures(1, objects, Semantic.Object.TEXTURE);
		gl.glDeleteRenderbuffers(1, objects, Semantic.Object.RBO);

		// delete vao, vbo
		((GL2ES3) gl).glDeleteVertexArrays(1, objects, Semantic.Object.VAO);
		gl.glDeleteBuffers(1, objects, Semantic.Object.VBO);

		glRc.destruct();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// set gl
		glRc.setGL(drawable.getGL());

		// update window titel if set fps in titel
		TRGameWindow gw = glRc.createWindow();
		if (gw.showFPSinTitel()) {
			gw.setTempTitel(gw.getTitel() + "    ( " + getFPS() + " )");
		}

		// update render properties
		GL2ES2 gl = (GL2ES2) drawable.getGL();
		if (this.rpChanged) {
			this.updateRenderingProperties((GL2ES3) drawable.getGL());
		}

		// set framebuffer
		gl.glBindFramebuffer(GL2ES2.GL_FRAMEBUFFER, objects[Semantic.Object.FBO]);

		// clear screen
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glClearDepthf(1f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		// render background
		if (this.bg != null) {
			bg.render(glRc);
		}

		// remove unused programs
		GLProgramManager.cleanup((GL2ES3) gl);

		// render objects
		synchronized (lock) {
			for (IRenderable r : this.components) {
				r.render(glRc);
			}
		}

		// remove objects
		if (outObjects.size() > 0) {
			synchronized (outLock) {
				for (IRenderable i : outObjects) {
					synchronized (lock) {
						components.remove(i);
					}
				}
				outObjects.clear();
			}
		}

		// init new added objects
		if (unInitObjects.size() > 0) {
			synchronized (lock) {
				for (IRenderable r : unInitObjects) {
					r.init(glRc);
					this.components.add(r);
					this.temp.add(r);
				}
				for (IRenderable r : temp) {
					unInitObjects.remove(r);
				}
				temp.clear();
			}
		}
		synchronized (lock) {
			Collections.sort(components, new RenderableComparator());
		}

		// perform post-processing and draw on screen
		gl.glBindFramebuffer(GL2ES2.GL_FRAMEBUFFER, 0);
		gl.glUseProgram(this.programm.getID());
		((GL2ES3) gl).glBindVertexArray(objects[Semantic.Object.VAO]);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_TEXTURE_2D);

		gl.glActiveTexture(GL2ES2.GL_TEXTURE0);
		gl.glBindTexture(GL2ES2.GL_TEXTURE_2D, objects[Semantic.Object.TEXTURE]);

		gl.glDrawArrays(GL2ES2.GL_TRIANGLES, 0, 6);
		((GL2ES3) gl).glBindVertexArray(0);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		glRc.setGL(drawable.getGL());
		GL2ES2 gl = (GL2ES2) drawable.getGL();

		gl.glBindTexture(GL2ES2.GL_TEXTURE_2D, objects[Semantic.Object.TEXTURE]);
		gl.glTexImage2D(GL2ES2.GL_TEXTURE_2D, 0, GL2ES2.GL_RGB, width, height, 0, GL2ES2.GL_RGB,
				GL2ES2.GL_UNSIGNED_BYTE, null);
		gl.glBindTexture(GL2ES2.GL_TEXTURE_2D, 0);
		gl.glBindRenderbuffer(GL2ES2.GL_RENDERBUFFER, objects[Semantic.Object.RBO]);
		gl.glRenderbufferStorage(GL2ES2.GL_RENDERBUFFER, GL2ES2.GL_DEPTH24_STENCIL8, width, height);
		gl.glBindRenderbuffer(GL2ES2.GL_RENDERBUFFER, 0);

		viewPort = new Rect(x, y, width, height, null);
		gl.glViewport(x, y, width, height);
		cam.setWinSize(width, height);

		synchronized (lock) {
			for (IRenderable r : this.components) {
				r.resize(glRc, width, height);
			}
		}
	}

	public void resetViewport(GL2ES2 gl) {
		// System.out.println("org vp: "+viewPort);
		gl.glViewport(viewPort.x(), viewPort.y(), viewPort.w(), viewPort.h());
	}

	private TRMouseEvent toTREvent(MouseEvent e) {
		TRMouseEvent tre = new TRMouseEvent(e, lastMousePos);
		tre.lastPos.set(lastMousePos.getX(), lastMousePos.getY());
		GLCamera cam = (GLCamera) this.cam;
		int x = Math.round(e.getX() * cam.getScale());
		int y = Math.round((cam.getWinHeigth() - e.getY()) * cam.getScale());

		tre.setTranslatedPos(x, y);
		lastMousePos = new Point(x, y);

		return getSelected(tre);
	}

	private TRMouseEvent getSelected(TRMouseEvent e) {
		IRenderable r = null;
		for (int i = (this.components.size() - 1); i >= 0; i--) {
			r = components.get(i);
			// System.out.println("Check component ("+i+"): "+r.isHit(e.x(),
			// e.y()));
			if (r.isHit(e.x(), e.y())) {
				if (e.src == null) {
					e.src = r;
					if (r instanceof ITRMouseListener) {
						e.listening = true;
					}
				}
				if (e.dra == null) {
					if (r instanceof TRDroparea) {
						e.dra = (TRDroparea) r;
					}
				}
			}
			if (e.src != null && e.dra != null) {
				break;
			}
		}

		return e;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		TRMouseEvent tre = toTREvent(e);
		if (tre.listening) {
			((ITRMouseListener) tre.src).mouseClicked(tre);
		}
		for (ITRGlobalMouseListener gl : this.gmllisteners) {
			gl.mouseClicked(new TRGlobalMouseEvent(tre));
		}
		this.lastMouseEvent = tre;

		if (tre.src != null && tre.src instanceof ISelectable) {
			((ISelectable) tre.src).requestFocus();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		TRMouseEvent tre = toTREvent(e);

		TRGlobalMouseEvent gme = new TRGlobalMouseEvent(tre);
		gme.setSource(null);
		for (ITRGlobalMouseListener gml : this.gmllisteners) {
			gml.mouseEnter(gme);
		}
		this.lastMouseEvent = tre;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		TRMouseEvent tre = toTREvent(e);

		TRGlobalMouseEvent gme = new TRGlobalMouseEvent(tre);
		gme.setSource(null);
		for (ITRGlobalMouseListener gml : this.gmllisteners) {
			gml.mouseLeave(gme);
		}
		this.lastMouseEvent = tre;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		TRMouseEvent tre = toTREvent(e);
		if (tre.listening) {
			((ITRMouseListener) tre.src).mousePress(tre);
		}
		for (ITRGlobalMouseListener gl : this.gmllisteners) {
			gl.mousePress(new TRGlobalMouseEvent(tre));
		}
		this.lastMouseEvent = tre;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		TRMouseEvent tre = toTREvent(e);
		if (tre.listening) {
			((ITRMouseListener) tre.src).mouseRelease(tre);
		}
		for (ITRGlobalMouseListener gl : this.gmllisteners) {
			gl.mouseRelease(new TRGlobalMouseEvent(tre));
		}
		this.lastMouseEvent = tre;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		TRMouseEvent tre = toTREvent(e);

		for (ITRGlobalMouseListener gml : this.gmllisteners) {
			gml.mouseMoved(new TRGlobalMouseEvent(tre));
		}

		ITRMouseListener lo = (lastMouseEvent.src != null && lastMouseEvent.listening)
				? (ITRMouseListener) lastMouseEvent.src : null;
		ITRMouseListener l = (tre.src != null && tre.listening) ? (ITRMouseListener) tre.src : null;
		// System.out.println("LO: "+lo+"; L: "+l);

		if (lo != null && lo.equals(l)) {
			l.mouseMoved(tre);
		} else {
			// System.out.println("Mouse Event! "+lo+l);
			if (lo != null) {
				// System.out.println("Mouse Leave!");
				lo.mouseLeave(tre);
			}
			if (l != null) {
				// System.out.println("Mouse Enter!");
				l.mouseEnter(tre);
			}
		}
		this.lastMouseEvent = tre;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		TRMouseEvent tre = toTREvent(e);
		if (tre.listening) {
			((ITRMouseListener) tre.src).mouseDragged(tre);
		}
		for (ITRGlobalMouseListener gl : this.gmllisteners) {
			gl.mouseDragged(new TRGlobalMouseEvent(tre));
		}
		this.lastMouseEvent = tre;
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private ITRKeyListener getKeyListener() {
		IRenderable r = TRComponentManager.getSelected();
		if (r == null) {
			return null;
		}
		if (r instanceof ITRKeyListener) {
			return (ITRKeyListener) r;
		}
		return null;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println("KeyPressed");
		ITRKeyListener r = getKeyListener();
		TRKeyEvent ke = new TRKeyEvent(e);
		for (ITRKeyListener l : this.gkllisteners) {
			l.keyPressed(ke);
		}
		if (r != null) {
			r.keyPressed(ke);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// System.out.println("KeyReleased");
		ITRKeyListener r = getKeyListener();
		TRKeyEvent ke = new TRKeyEvent(e);
		synchronized (gklLock) {
			for (ITRKeyListener l : this.gkllisteners) {
				l.keyReleased(ke);
			}
		}
		if (r != null) {
			r.keyReleased(ke);
		}
	}

}
