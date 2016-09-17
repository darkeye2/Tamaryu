package com.tr.gl.core;

import com.jogamp.nativewindow.util.Dimension;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.math.Matrix4;
import com.tr.engine.grf.ICamera;

public class GLCamera implements ICamera{
	//constants
	public static final short DISP_MODE_3D = 0x03;
	public static final short DISP_MODE_2D = 0x02;
	public static final short DISP_MODE_BOTH = 0x00;
	public static final float NO_ZOOM = 1.0f;
	
	// window size
	protected float winWidth = 1;
	protected float winHeight = 1;
	
	// ref size
	protected float refWidth = -1;
	protected float refHeight = -1;

	// near, far, fovy
	protected float zNear = 0.1f;
	protected float zFar = 50.0f;
	protected float fovy = 45.0f;
	
	// mode (if mode is 2D or 3D, ortho and proj matrix are the same)
	protected short disp_mode = 0;

	protected Matrix4 tmpMat = new Matrix4();
	protected float[] view_matrix = new float[16];
	protected float[] proj_matrix = new float[16];
	protected float[] ortho_matrix = new float[16];

	protected final Point3D FRONT = new Point3D(0f, 0f, -1f); // world front
	protected final Point3D RIGHT = Point3D.getXAxis(); // world right
	protected final Point3D UP = Point3D.getYAxis(); // world up

	protected Point3D front = new Point3D(0f, 0f, -1f); // current camera front
	protected Point3D up = new Point3D(0f, 1f, 0f); // current camera up
	protected Point3D right = new Point3D(1f, 0f, 0f); // current camera right

	protected Point3D pos = new Point3D(0f, 0f, 0f); // position of the camera
														// in the 3D world
	protected Point3D rot = new Point3D(0f, 0f, 0f); // rotation of the camera

	protected float zoom = 1.0f; // zoom factor
	protected float aspectScale = 1.0f;
	
	protected boolean normalized = true;

	public GLCamera(int ww, int wh) {
		this(ww, wh, 0, 0, 0);
	}

	public GLCamera(int ww, int wh, float x, float y, float z) {
		this(ww, wh, x, y, z, 0, 0, 0);
	}

	public GLCamera(int ww, int wh, float x, float y, float z, float xAng,
			float yAng, float zAng) {
		winWidth = ww;
		winHeight = wh;
		pos.x = x;
		pos.y = y;
		pos.z = z;
		rot.x = xAng;
		rot.y = yAng;
		rot.z = zAng;

		// create projection matrix
		updateProjectionMatrix();

		// create orthogonal matrix
		updateOrthoMatrix();

		// create view Matrix
		updateViewMatrix();
		
		this.setNormalized(false);
	}
	
	public float normalizeWidth(float w){
		if(this.refWidth > 0){
			return w/refWidth;
		}else{
			return w/winWidth;
		}
	}
	
	public float normalizeHeight(float h){
		if(this.refHeight > 0){
			return h/refHeight;
		}else{
			return h/winHeight;
		}
	}
	
	public float normalizeX(float x){
		if(this.refWidth > 0){
			return x/refWidth;
		}else{
			return x/winWidth;
		}
	}
	
	public float normalizeY(float y){
		if(this.refHeight > 0){
			return y/refHeight;
		}else{
			return y/winHeight;
		}
	}
	
	public void setReferenceSize(float refW, float refH){
		this.refWidth = refW;
		this.refHeight = refH;
		calculateScale();
	}
	
	public Dimension getReferenceSize(){
		return new Dimension((int) this.refWidth, (int) this.refHeight);
	}
	
	public float getRefWidth(){
		return this.refWidth;
	}
	
	public float getRefHeight(){
		return this.refHeight;
	}

	private void updateProjectionMatrix() {
		/*System.out.println("near: " + zNear + "; far: " + zFar + "; fovy: "
				+ fovy);*/
		float aspect = winWidth / winHeight;
		proj_matrix = FloatUtil.makePerspective(proj_matrix, 0, true, (float) Math.toRadians(fovy), aspect, zNear,
				zFar);
	}

	private void updateOrthoMatrix() {
		float aspect = winWidth / winHeight;
		/*if(winWidth >= winHeight){
			aspect = winWidth / winHeight;
		}else{
			aspect = winHeight / winWidth;
		}*/
		if(this.normalized){
			ortho_matrix = FloatUtil.makeOrtho(ortho_matrix, 0, false, -aspect, aspect, -1, 1,
					zNear, zFar);
		}else{
			float m = Math.max(this.winHeight, this.winWidth);
			ortho_matrix = FloatUtil.makeOrtho(ortho_matrix, 0, false, 0, this.winWidth*zoom*aspectScale, 0, 
					this.winHeight*zoom*aspectScale, -m, m);
		}
		
		ortho_matrix[14] = 0;
		//GLCamera.printFloatMatrix(ortho_matrix, 4, 4);
		 
	}

	private void updateViewMatrix() {
		float[] tmp = new float[16];
		FloatUtil.makeLookAt(view_matrix, 0, pos.toVectorArray(), 0, Point3D
						.add(pos, front).toVectorArray(), 0,
						up.toVectorArray(), 0, tmp);
	}

	public void setWinSize(int w, int h) {
		this.winWidth = w;
		this.winHeight = h;

		// create projection matrix
		updateProjectionMatrix();

		// create orthogonal matrix
		updateOrthoMatrix();
		
		calculateScale();
	}
	
	public float getWinWidth(){
		return this.winWidth;
	}
	
	public float getWinHeigth(){
		return this.winHeight;
	}
	
	private void calculateScale(){
		float s = Math.min(this.refWidth/this.winWidth, this.refHeight/this.winHeight);
		if(this.refWidth > 0 && this.refHeight > 0){
			this.aspectScale = s;
		}
		
		this.updateOrthoMatrix();
		this.updateProjectionMatrix();
	}
	
	public float getAspectScale(){
		return this.aspectScale;
	}
	
	public float getScale(){
		return this.aspectScale*this.zoom;
	}
	
	public float getZoom(){
		return this.zoom;
	}
	
	public Dimension getWinSize(){
		return new Dimension((int)this.winWidth, (int)this.winHeight);
	}

	public void setPosition(float x, float y, float z) {
		this.pos.x = x;
		this.pos.y = y;
		this.pos.z = z;

		// update view Matrix
		updateViewMatrix();
	}

	public void setRotation(float xAng, float yAng, float zAng) {
		this.rot.x = (float) Math.toRadians(xAng);
		this.rot.y = (float) Math.toRadians(yAng);
		this.rot.z = (float) Math.toRadians(zAng);

		// update view Matrix
		updateViewMatrix();
	}

	public void increasePos(float x, float y, float z) {
		if (x == 0 && y == 0 && z == 0)
			return;
		/*pos.add(Point3D.mult(front, z));
		pos.add(Point3D.mult(right, x));
		pos.add(Point3D.mult(up, y));*/
		pos.x += x;
		pos.y += y;
		pos.z += z;

		// update view Matrix
		updateViewMatrix();
	}

	public void increaseRot(float x, float y, float z) {
		if (x == 0 && y == 0 && z == 0)
			return;
		// pos.add(Point3D.mult(front, z));
		// pos.add(Point3D.mult(right, x));
		// pos.add(Point3D.mult(up, y));

		// update view Matrix
		// TODO
	}

	public void increaseZNear() {
		zNear += 1f;
		if(zNear > 10.0f)
			zNear = 10.0f;

		// create projection matrix
		updateProjectionMatrix();

		// create orthogonal matrix
		updateOrthoMatrix();
	}

	public void decreaseZNear() {
		zNear -= 1f;
		if(zNear < 0.1f)
			zNear = 0.1f;

		// create projection matrix
		updateProjectionMatrix();

		// create orthogonal matrix
		updateOrthoMatrix();
	}

	public void increaseZFar() {
		zFar += 1f;
		if(zFar > 100.0f)
			zFar = 100.0f;

		// create projection matrix
		updateProjectionMatrix();

		// create orthogonal matrix
		updateOrthoMatrix();
	}

	public void decreaseZFar() {
		zFar -= 1f;
		if(zFar < 10.1f)
			zFar = 10.1f;

		// create projection matrix
		updateProjectionMatrix();

		// create orthogonal matrix
		updateOrthoMatrix();
	}

	public void increaseFovy() {
		fovy += 0.01f;

		// create projection matrix
		updateProjectionMatrix();

		// create orthogonal matrix
		updateOrthoMatrix();
	}

	public void decreaseFovy() {
		fovy -= 0.01f;
		if(fovy < 0.5f)
			fovy = 0.5f;

		// create projection matrix
		updateProjectionMatrix();

		// create orthogonal matrix
		updateOrthoMatrix();
	}
	
	public void setDispMode(short modeConstant){
		this.disp_mode = modeConstant;
	}

	public float[] getViewMatrix() {
		return view_matrix;
	}

	public float[] getProjectionMatrix() {
		if(this.disp_mode == GLCamera.DISP_MODE_2D){
			return ortho_matrix;
		}
		if(this.disp_mode == GLCamera.DISP_MODE_3D){
			return proj_matrix;
		}
		return proj_matrix;
	}

	public float[] getOrthoMatrix() {
		//this.increasePos(0.001f, 0, 0);
		if(this.disp_mode == GLCamera.DISP_MODE_2D){
			return ortho_matrix;
		}
		if(this.disp_mode == GLCamera.DISP_MODE_3D){
			return proj_matrix;
		}
	
		return ortho_matrix;
	}
	
	public static void printFloatMatrix(float[] m, int x, int y){
		//System.out.println("Matrix "+x+"x"+y+" {");
		StringBuilder sb = new StringBuilder();
		sb.append("\r\nMatrix "+x+"x"+y+" {\r\n");
		for(int i = 0; i< y; i++){
			for(int j = 0; j< x; j++){
				sb.append(String.format("%.2f,\t", m[j*x+i]));
				//System.out.format("%f,\t", m[j*x+i]);
			}
			sb.append("\r\n");
			//System.out.println();
		}
		sb.append("}");
		System.out.println(sb.toString());
	}
	
	public static void printFloatMatrix(float[] m, int x, int y, boolean col){
		if(!col){
			printFloatMatrix(m,x,y);
			return;
		}
		//System.out.println("Matrix "+x+"x"+y+" {");
		StringBuilder sb = new StringBuilder();
		sb.append("\r\nMatrix "+x+"x"+y+" {\r\n");
		for(int i = 0; i< y; i++){
			for(int j = 0; j< x; j++){
				sb.append(String.format("%.2f,\t", m[j+i*x]));
				//System.out.format("%f,\t", m[j*x+i]);
			}
			sb.append("\r\n");
			//System.out.println();
		}
		sb.append("}");
		System.out.println(sb.toString());
	}

	@Override
	public void setNormalized(boolean norm) {
		this.normalized = norm;
	}

	@Override
	public boolean isNormalized() {
		return normalized;
	}

}
