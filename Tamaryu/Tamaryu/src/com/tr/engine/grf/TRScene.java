package com.tr.engine.grf;

import java.util.ArrayList;
import java.util.Collections;

import com.jogamp.nativewindow.util.Dimension;
import com.tr.engine.input.EventComparator;
import com.tr.engine.input.ITRGlobalMouseListener;
import com.tr.engine.input.ITRKeyListener;
import com.tr.engine.input.ITRMouseListener;
import com.tr.engine.input.TRDragAndDropManager;

public abstract class TRScene {
	protected TRRenderContext rc;
	
	//component list
	protected ArrayList<IRenderable> components = new ArrayList<IRenderable>();
	protected Object lock = new Object();
	
	protected ArrayList<ITRMouseListener> mlisteners = new ArrayList<ITRMouseListener>();
	protected ArrayList<ITRGlobalMouseListener> gmllisteners = new ArrayList<ITRGlobalMouseListener>();
	
	protected ArrayList<ITRKeyListener> gkllisteners = new ArrayList<ITRKeyListener>();
	protected Object gklLock = new Object();
	
	protected TRDragAndDropManager dndManager = null;
	protected EventComparator ecomp = new EventComparator();
	protected IRenderable bg = null;
	protected ICamera cam = null;
	
	public TRScene(TRRenderContext context){
		this.rc = context;
	}
	
	public abstract void addComponent(IRenderable ra);
	public abstract boolean removeComponent(IRenderable ra);
	public abstract void clearScene();
	public abstract void setBackground(IRenderable bg);
	public abstract float getFPS();
	public abstract void setTargetFPS(int fps);
	
	public ICamera getCamera(){
		return cam;
	}

	public void setSceneSize(float sceneWidth, float sceneHeight) {
		cam.setReferenceSize(sceneWidth, sceneHeight);
	}
	
	public Dimension getSceneSize(){
		return cam.getReferenceSize();
	}
	
	public void setDnDManager(TRDragAndDropManager  dndm){
		if(this.dndManager != null){
			this.removeGlobalMouseListener(dndManager);
		}
		this.dndManager = dndm;
		if(this.dndManager != null){
			this.addGlobalMouseListener(dndManager);
		}
	}
	
	public void addMouseListener(ITRMouseListener ml){
		this.mlisteners.add(ml);
		Collections.sort(mlisteners, ecomp);
	}
	
	public void addGlobalMouseListener(ITRGlobalMouseListener ml){
		this.gmllisteners.add(ml);
	}
	
	public void removeMouseListener(ITRMouseListener ml){
		this.mlisteners.remove(ml);
		Collections.sort(mlisteners, ecomp);
	}
	
	public void removeGlobalMouseListener(ITRGlobalMouseListener ml){
		this.gmllisteners.remove(ml);
	}
	
	public void addGlobalKeyListener(ITRKeyListener kl){
		synchronized(gklLock){
			this.gkllisteners.add(kl);
		}
	}
	
	public void removeGlobalKeyListener(ITRKeyListener kl){
		synchronized(gklLock){
			this.gkllisteners.remove(kl);
		}
	}
}
