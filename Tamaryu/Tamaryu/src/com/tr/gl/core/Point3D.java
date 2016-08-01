package com.tr.gl.core;

public class Point3D extends Object{
	public float x = 0;
	public float y = 0;
	public float z = 0;
	
	public Point3D(float x,float y,float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public boolean isInRadius(Point3D target, float r){
		return getDistance(target) <= Math.pow(r, 2);
	}
	
	public float getDistance(Point3D p){
		return (float) Math.sqrt(Math.pow(this.x-p.x, 2) + Math.pow(this.y-p.y, 2) + Math.pow(this.z-p.z, 2));
	}
	
	public float[] toArray(){
		return new float[]{x,y,z};
	}
	
	public void normalize(){
		float l = getLength();
		if(l <= 0)
			return;
		x = x/l;
		y = y/l;
		z = z/l;
	}
	
	public static Point3D sub(Point3D p, Point3D p2){
		return new Point3D(p.x-p2.x,p.y-p2.y,p.z-p2.z);
	}
	
	public static Point3D add(Point3D p, Point3D p2){
		return new Point3D(p.x+p2.x,p.y+p2.y,p.z+p2.z);
	}
	
	public static Point3D cross(Point3D p, Point3D p2){
		return new Point3D(p.y*p2.z - p.z*p2.y,
						   p.z*p2.x - p.x*p2.z,
						   p.x*p2.y - p.y*p2.x);
	}
	
	public static Point3D mult(Point3D p, float s){
		return new Point3D(p.x*s, p.y*s, p.z*s);
	}
	
	public void mult(float s){
		x*=s;
		y*=s;
		z*=s;
	}
	
	public void sub(Point3D p){
		x-=p.x;
		y-=p.y;
		z-=p.z;
	}
	
	public void add(Point3D p){
		x+=p.x;
		y+=p.y;
		z+=p.z;
	}
	
	public float getLength(){
		return (float) (Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2)));
	}
	
	public float[] toVectorArray(){
		return new float[]{x,y,z,0};
	}
	
	public float[] toHomogeniousArray(){
		return new float[]{x,y,z,1};
	}
	
	public String toString(){
		return "( "+x+", "+y+", "+z+" )";
	}
	
	public static Point3D getIdentity(){
		return new Point3D(1,1,1);
	}
	
	public static Point3D getXAxis(){
		return new Point3D(1,0,0);
	}
	
	public static Point3D getYAxis(){
		return new Point3D(0,1,0);
	}
	
	public static Point3D getZAxis(){
		return new Point3D(0,0,1);
	}
}
