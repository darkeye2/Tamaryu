package com.tr.engine.input;

import java.util.ArrayList;

import com.tr.engine.grf.IRenderable;


public class TRDragAndDropManager implements ITRGlobalMouseListener{
	protected ArrayList<TRDraggedObject> dragObjects = new ArrayList<TRDraggedObject>();
	
	//settings
	protected boolean enabled = true;
	protected boolean singleDrag = true;
	protected boolean dragOnOver = false;
	protected boolean dropAreaOnly = false;
	protected boolean returnOnDropFail = true;
	protected boolean paused = false;
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void setEnabled(boolean e){
		this.enabled = e;
	}
	
	public boolean isSingleDrag(){
		return singleDrag;
	}
	
	public void setSingleDrag(boolean b){
		this.singleDrag = b;
	}
	
	public boolean isDragOnOver(){
		return dragOnOver;
	}
	
	public void setDragOnOver(boolean b){
		this.dragOnOver = b;
	}
	
	public boolean isDropAreaOnly(){
		return  dropAreaOnly;
	}
	
	public void setDropAreaOnly(boolean b){
		this.dropAreaOnly = b;
	}
	
	public boolean isReturnOnDropFail(){
		return returnOnDropFail;
	}
	
	public void setReturnOnDropFail(boolean b){
		this.returnOnDropFail = b;
	}
	
	public boolean isDragging(IRenderable r){
		if(enabled){
			for(TRDraggedObject tdo : dragObjects){
				if(tdo.r.equals(r))
					return true;
			}
		}
		return false;
	}
	
	protected void startDrag(TRGlobalMouseEvent e, boolean over){
		//System.out.println("Drag src: "+e.getSource());
		if(!enabled || paused){
			return;
		}
		
		if(singleDrag && dragObjects.size() > 1){
			return;
		}
		
		if(!dragOnOver && over){
			return;
		}
		
		if(e.getSource() != null && e.getSource() instanceof TRDragable){
			((TRDragable) e.getSource()).onDrag();
			dragObjects.add(new TRDraggedObject(e));
		}
	}
	
	protected void drag(TRGlobalMouseEvent e){
		if(!enabled || paused){
			return;
		}
		
		//System.out.println("Last: "+e.lastPos+"; New: "+e.x()+", "+e.y());
		int xOff = e.x() - e.lastPos.getX();
		int yOff = e.y() - e.lastPos.getY();
		int i = 0;
		for(TRDraggedObject tdo : dragObjects){
			//System.out.println("Dragging ("+(i++)+"): ["+xOff+", "+yOff+"]");
			tdo.r.increasePos(xOff, yOff, 0);
		}
	}
	
	protected void drop(TRGlobalMouseEvent e){
		if(!enabled || paused){
			return;
		}
		if(!dropAreaOnly){
			drag(e);
			for(TRDraggedObject o : dragObjects){
				((TRDragable) o.r).onDrop();
			}
			dragObjects.clear();
		}else{
			drag(e);
			if(e.getSource() != null && e.getSource() instanceof TRDroparea){
				((TRDroparea) e.getSource()).drop(dragObjects.get(0).r);
			}else if(returnOnDropFail){
				drag(e);
				for(TRDraggedObject tdo : dragObjects){
					int xOff = e.x() - tdo.startPos.getX();
					int yOff = e.y() - tdo.startPos.getY();
					tdo.r.increasePos(xOff, yOff, 0);
				}
				dragObjects.clear();
			}
			for(TRDraggedObject o : dragObjects){
				((TRDragable) o.r).onDrop();
			}
			dragObjects.clear();
		}
	}
	
	
	@Override
	public void mouseEnter(TRGlobalMouseEvent e) {
		paused = false;
		drag(e);
	}
	@Override
	public void mouseLeave(TRGlobalMouseEvent e) {
		paused = true;
	}
	@Override
	public void mouseRelease(TRGlobalMouseEvent e) {
		drop(e);
	}
	@Override
	public void mousePress(TRGlobalMouseEvent e) {
		startDrag(e, false);
	}
	@Override
	public void mouseDragged(TRGlobalMouseEvent e) {
		startDrag(e, true);
		drag(e);
	}

	@Override
	public void mouseMoved(TRGlobalMouseEvent tre) {
		drag(tre);
	}

}
