package com.tr.engine.grf.gl;

import java.util.ArrayList;

import com.jogamp.opengl.math.Matrix4;
import com.tr.engine.grf.IRenderable;
import com.tr.engine.grf.TRRenderContext;
import com.tr.gl.core.GLCamera;
import com.tr.gl.core.GLProgramm;
import com.tr.gl.core.GLTexture;
import com.tr.gl.core.Point3D;
import com.tr.gl.core.Semantic;



public abstract class TRGLRenderable implements IRenderable {
	//id
	protected String name = "";
	protected int id = 0;
	
	//position
	protected Point3D pos = new Point3D(0f,0f,0f);				//position of the object in the 3D world
	protected Point3D offset = new Point3D(0,0,0);				//
	protected Point3D anchor = new Point3D(0,0,0);
	protected Point3D rot = new Point3D(0f,0f,0f);				//rotation of the object  (own zero point)
	protected float width = 1, height = 1;						//
	protected float scale = 1f;
	protected Matrix4 model_matrix = new Matrix4();				//model view matrix
	protected boolean normalized = true;						//if x and y are normalized
	protected boolean updateMatrix = false;						//
	protected int positionFix = -1;
	protected boolean callResize = false;
	
	//data and bindings
	protected float[] data;
	protected int[] objects = new int[Semantic.Object.SIZE];	//adresses (vbo, vba, ...)
	protected GLProgramm program = null;
	protected GLTexture texture = null;
	
	//components
	protected TRGLRenderable parent = null;
	protected volatile ArrayList<TRGLRenderable> components = new ArrayList<TRGLRenderable>();
	protected volatile ArrayList<TRGLRenderable> inComponents = new ArrayList<TRGLRenderable>();
	protected volatile ArrayList<TRGLRenderable> outComponents = new ArrayList<TRGLRenderable>();
	protected volatile Object inLock = new Object();
	protected volatile Object outLock = new Object();
	protected volatile Object lock = new Object();
	
	
	public void setFixedPosition(int posConstant){
		this.positionFix = posConstant;
		this.callResize = true;
	}
	
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
		this.pos.z = z;
		updateModelMatrix(null);
	}
	
	public void setScale(float f){
		this.scale = f;
	}
	
	public float getScale(){
		return this.scale;
	}
	
	public float getOverallScale(){
		return this.scale;
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
	
	public Point3D getAbsolutPosition(){
		Point3D pos = new Point3D(0,0,0);
		if(parent != null){
			pos.add(parent.getAbsolutPosition());
		}
		Point3D tmp = new Point3D(0,0,0);
		tmp.add(this.getPosition());
		tmp.mult(this.getAbsolutScale());
		pos.add(tmp);
		return pos;
	}
	
	public float getAbsolutScale(){
		if(parent != null){
			return this.getScale()*parent.getAbsolutScale();
		}
		return this.getScale();
	}
	
	protected void updateOffset(){
		offset.x = offset.y = offset.z = 0;
		float tmp = 0;
		if((tmp = (float) Math.cos(Math.toRadians(rot.y))) < 0){
			this.offset.x = -tmp*this.width;
		}
		if((tmp = (float) Math.cos(Math.toRadians(rot.x))) < 0){
			this.offset.y = -tmp*this.height;
		}
		
	}
	
	@Override
	public void setAnchor(float x, float y, float z) {
		this.anchor = new Point3D(x,y,z);
	}

	@Override
	public float getAnchorX() {
		return anchor.x;
	}

	@Override
	public float getAnchorY() {
		return anchor.y;
	}

	@Override
	public float getAnchorZ() {
		return anchor.z;
	}

	@Override
	public Point3D getAnchor() {
		return anchor;
	}
	
	protected void updateModelMatrix(GLCamera cam){
		if(!normalized && cam == null){
			updateMatrix = true;
			return;
		}
		
		updateOffset();
		
		float x = pos.x, y = pos.y, z = pos.z;
		if(!normalized){
			/*float x0 = - (cam.getRefWidth()/cam.getRefHeight());
			float xw = (cam.getRefWidth()/cam.getRefHeight());
			float y0 = 1;
			float yh = -1;*/
			x += offset.x;
			y += offset.y;
			z += offset.z;
			
			//x = x0 + x * (2 * xw / cam.getRefWidth()) + this.width/cam.getRefWidth();
			//y = y0 + y * (2 * yh / cam.getRefHeight()) - this.height/cam.getRefHeight();
			
			
			//System.out.println("Rendering not normalized Object!");
			//x = ((x)/cam.getRefWidth()/2) - 1;
			//y = (((y)/cam.getRefHeight()/2) - 1)*-1;
			/*x = ((x+this.width/2-cam.getRefWidth()/2)/cam.getRefWidth()/2);
			y = 1-((x+this.height/2-cam.getRefHeight()/2)/cam.getRefHeight()/2);*/
			//System.out.println("Normalized Pos: "+x+", "+y);
		}
		
		Matrix4 tmp = new Matrix4();
		tmp.loadIdentity();
		tmp.scale(scale, scale, scale);
		tmp.translate(x, y, z);
		tmp.translate(anchor.x, anchor.y, anchor.z);
		tmp.rotate((float)Math.toRadians(rot.x), 1, 0, 0);
		tmp.rotate((float)Math.toRadians(rot.y), 0, 1, 0);
		tmp.rotate((float)Math.toRadians(rot.z), 0, 0, 1);
		tmp.translate(-anchor.x, -anchor.y, -anchor.z);
		
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
			synchronized(inLock){
				this.inComponents.add((TRGLRenderable) c);
			}
			//Collections.sort(this.components);
		}
	}

	@Override
	public IRenderable removeComponent(IRenderable c) {
		if(this.components.contains(c)){
			((TRGLRenderable)c).parent = null;
			synchronized(outLock){
				this.outComponents.add((TRGLRenderable) c);
				//System.out.println("Add cc to remove!");
			}
			return c;
		}
		return null;
	}

	@Override
	public void removeAll() {
		synchronized(outLock){
			inComponents.clear();
			synchronized(lock){
				for(TRGLRenderable i : components){
					//System.out.println("Add cc to remove!");
					this.outComponents.add(i);
				}
			}
		}
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return this.id;
	}
	
	public IRenderable getComponentByName(String nn){
		if(nn.equals("this"))
			return this;
		String[] names = nn.split("\\.");
		if(names.length > 0){
			synchronized(lock){
				for(IRenderable r : this.components){
					if(r.getName().equals(names[0])){
						if(names.length > 1){
							return r.getComponentByName(join(names, ".", 1));
						}else{
							return r;
						}
					}
				}
			}
			for(IRenderable r : this.inComponents){
				if(r.getName().equals(names[0])){
					if(names.length > 1){
						return r.getComponentByName(join(names, ".", 1));
					}else{
						return r;
					}
				}
			}
		}
		
		return null;
	}
	
	public IRenderable getComponentByID(String idid){
		if(idid.equals("-1")){
			return this;
		}
		String[] ids = idid.split("\\.");
		if(ids.length > 0){
			synchronized(lock){
				for(IRenderable r : this.components){
					if(r.getID() == Integer.parseInt(ids[0])){
						if(ids.length > 1){
							return r.getComponentByID(join(ids, ".", 1));
						}else{
							return r;
						}
					}
				}
			}
			for(IRenderable r : this.inComponents){
				if(r.getID() == Integer.parseInt(ids[0])){
					if(ids.length > 1){
						return r.getComponentByID(join(ids, ".", 1));
					}else{
						return r;
					}
				}
			}
		}
		
		return null;
	}
	
	
	private String join(String[] arr, String sep, int offset){
		String ret = "";
		for(int i = offset; i<arr.length; i++){
			ret += arr[i];
			if(i+1 < arr.length){
				ret += sep;
			}
		}
		
		return ret;
	}
	
	public void resize(TRRenderContext rc, int w, int h){
		GLCamera cam = (GLCamera)rc.getScene().getCamera();
		//System.out.println("HERE");
		if(this.positionFix == FIXED_POS_NONE)
			return;
		
		if(getWidth()>0 && getHeight() > 0){
			//System.out.println("Component Size: "+getWidth()+" x "+getHeight());
			int xoff = (int) (cam.getWinWidth()*cam.getScale() - this.width*this.getScale());
			int yoff = (int) (cam.getWinHeigth()*cam.getScale() - this.height*this.getScale());
			
			//set y
			if(positionFix == FIXED_POS_CENTER || positionFix == FIXED_POS_LEFT || positionFix == FIXED_POS_RIGHT){
				yoff /= 2;
			}else if(positionFix == FIXED_POS_BOTTOM_LEFT || positionFix == FIXED_POS_BOTTOM || positionFix == FIXED_POS_BOTTOM_RIGHT){
				yoff = 0;
			}
			
			//set x
			if(positionFix == FIXED_POS_CENTER || positionFix == FIXED_POS_TOP || positionFix == FIXED_POS_BOTTOM){
				xoff /= 2;
			}else if(positionFix == FIXED_POS_TOP_LEFT || positionFix == FIXED_POS_LEFT || positionFix == FIXED_POS_BOTTOM_LEFT){
				xoff = 0;
			}
			
			this.setPosition(xoff/this.getScale(), yoff/this.getScale(), this.getPosition().z);
			//System.out.println("Component Postion: "+this.getPosition()+" Scale: "+cam.getScale());
			//System.out.println(cam.getScale());
		}
		
		this.callResize = false;
	}

}
