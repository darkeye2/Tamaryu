package com.tr.engine.grf.gl;

import java.util.ArrayList;
import java.util.Collections;

import com.jogamp.opengl.math.Matrix4;
import com.tr.engine.grf.IRenderable;
import com.tr.gl.core.GLCamera;
import com.tr.gl.core.GLProgramm;
import com.tr.gl.core.GLTexture;
import com.tr.gl.core.Point3D;
import com.tr.gl.core.Semantic;



public abstract class TRGLRenderable implements IRenderable {
	//position
	protected Point3D pos = new Point3D(0f,0f,0f);				//position of the object in the 3D world
	protected Point3D rot = new Point3D(0f,0f,0f);				//rotation of the object  (own zero point)
	protected float width = 1, height = 1;						//
	protected Matrix4 model_matrix = new Matrix4();				//model view matrix
	protected boolean normalized = true;						//if x and y are normalized
	protected boolean updateMatrix = false;						//
	
	//data and bindings
	protected float[] data;
	protected int[] objects = new int[Semantic.Object.SIZE];	//adresses (vbo, vba, ...)
	protected GLProgramm program = null;
	protected GLTexture texture = null;
	
	//components
	protected TRGLRenderable parent = null;
	protected ArrayList<TRGLRenderable> components = new ArrayList<TRGLRenderable>();
	
	
	public void setNormalized(boolean normalized){
		this.normalized = normalized;
	}
	
	public void setPosition(float x, float y, float z) {
		this.pos.x = x;
		this.pos.y = y;
		this.pos.z = z;
		updateModelMatrix(null);
	}

	@Override
	public void setX(float x) {
		this.pos.x = x;
		updateModelMatrix(null);
	}

	@Override
	public void setY(float y) {
		this.pos.y = y;
		updateModelMatrix(null);
	}

	@Override
	public void setZ(float z) {
		this.pos.z = 1;
		updateModelMatrix(null);
	}
	
	public void increasePos(float x, float  y, float z){
		this.pos.x += x;
		this.pos.y += y;
		this.pos.z += z;
		updateModelMatrix(null);
	}
	
	public void setRotation(float xAng, float yAng, float zAng){
		this.rot.x = xAng;
		this.rot.y = yAng;
		this.rot.z = zAng;
		updateModelMatrix(null);
	}
	
	public void setXRotation(float ang){
		this.rot.x = ang;
		updateModelMatrix(null);
	}
	
	public void setYRotation(float ang){
		this.rot.y = ang;
		updateModelMatrix(null);
	}
	
	public void setZRotation(float ang){
		this.rot.z = ang;
		updateModelMatrix(null);
	}
	
	public void increaseRot(float x, float  y, float z){
		this.rot.x += x;
		this.rot.y += y;
		this.rot.z += z;
		updateModelMatrix(null);
	}

	@Override
	public Point3D getPosition() {
		return pos;
	}
	
	protected void updateModelMatrix(GLCamera cam){
		if(!normalized && cam == null){
			updateMatrix = true;
			return;
		}
		
		float x = pos.x, y = pos.y, z = pos.z;
		if(!normalized){
			float x0 = - (cam.getRefWidth()/cam.getRefHeight());
			float xw = (cam.getRefWidth()/cam.getRefHeight());
			float y0 = 1;
			float yh = -1;
			
			x = x0 + x * (2 * xw / cam.getRefWidth()) + this.width/cam.getRefWidth();
			y = y0 + y * (2 * yh / cam.getRefHeight()) - this.height/cam.getRefHeight();
			
			//System.out.println("Rendering not normalized Object!");
			//x = ((x)/cam.getRefWidth()/2) - 1;
			//y = (((y)/cam.getRefHeight()/2) - 1)*-1;
			/*x = ((x+this.width/2-cam.getRefWidth()/2)/cam.getRefWidth()/2);
			y = 1-((x+this.height/2-cam.getRefHeight()/2)/cam.getRefHeight()/2);*/
			//System.out.println("Normalized Pos: "+x+", "+y);
		}
		
		Matrix4 tmp = new Matrix4();
		tmp.loadIdentity();
		tmp.translate(x, y, z);
		tmp.rotate((float)Math.toRadians(rot.x), 1, 0, 0);
		tmp.rotate((float)Math.toRadians(rot.y), 0, 1, 0);
		tmp.rotate((float)Math.toRadians(rot.z), 0, 0, 1);
		
		//tmp.rotate(rot.x, 1, 0, 0);
		//tmp.rotate(rot.y, 0, 1, 0);
		//tmp.rotate(rot.z, 0, 0, 1);
		//GLCamera.printFloatMatrix(tmp.getMatrix(), 4, 4);
		updateMatrix = false;
		model_matrix = tmp;
	}
	
	public Matrix4 getModelMatrix(){
		if(this.parent == null){
			return this.model_matrix;
		}else{
			Matrix4 tmp = new Matrix4();
			tmp.multMatrix(parent.getModelMatrix());
			tmp.multMatrix(this.model_matrix);
			return tmp;
		}
	}

	@Override
	public int compareTo(IRenderable arg0) {
		return (int) ((this.pos.z*-1) - (((IRenderable)arg0).getPosition().z*-1));
	}



	@Override
	public void addComponent(IRenderable c) {
		if(c instanceof TRGLRenderable){
			((TRGLRenderable) c).parent = this;
			this.components.add((TRGLRenderable) c);
			Collections.sort(this.components);
		}
	}

	@Override
	public IRenderable removeComponent(IRenderable c) {
		if(this.components.contains(c)){
			((TRGLRenderable)c).parent = null;
			this.components.remove(c);
			return c;
		}
		return null;
	}

	@Override
	public void removeAll() {
		this.components.clear();
	}

}
