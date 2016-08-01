package com.tr.engine.grf;

public class TRRenderPropertie {

	// setting constants for glsl shaders
	public static final int USE_DISTANCE_SCALE = 1;
	public static final int USE_HSL_FILTER = 2;
	public static final int USE_TEXTURE = 3;
	public static final int USE_RGB_FILTER = 4;
	public static final int USE_LIGHTNING = 5;
	public static final int USE_COLOR_OVER_TEXTURE = 6;
	public static final int USE_8BIT_COLOR = 7;
	public static final int USE_PIXELATION = 8;
	public static final int USE_GRAYSCALE = 9;
	public static final int USE_TEXTURE_OVER_TEXTURE = 10;

	// setting value indexes
	public static final int COLOR_FILTER_RED = 31; // used for HSL and RGB
														// Filter
	public static final int COLOR_FILTER_GREEN = 32; // used for HSL and RGB
														// Filter
	public static final int COLOR_FILTER_BLUE = 33; // used for HSL and RGB
														// Filter
	public static final int OVERLAY_COLOR_RED = 34; // used for color over
														// texture
	public static final int OVERLAY_COLOR_GREEN = 35; // used for color over
														// texture
	public static final int OVERLAY_COLOR_BLUE = 36; // used for color over
														// texture
	public static final int OVERLAY_COLOR_ALPHA = 37; // used for color over
														// texture
	public static final int DISTANCE_SCALE_VALUE = 38; // used for distance
															// scale
	public static final int PIXELATION_VALUE = 39; // used for pixelation
	
	//
	public static final int AMOUNT_OF_PROPERTIES = 40;
	
	//propertie values
	private int id = 0;
	private float[] values = new float[5];
	
	public TRRenderPropertie(int id, float[] values){
		this.id = id;
		for(int i = 0; i < values.length && i < 5; i++){
			this.values[i] = values[i];
		}
	}
	
	public TRRenderPropertie(int id, int value, float r, float g, float b, float a){
		this.id = id;
		this.values[0] = value;
		this.values[1] = r;
		this.values[2] = g;
		this.values[3] = b;
		this.values[4] = a;
	}
	
	public TRRenderPropertie(int id, float r, float g, float b, float a){
		this.id = id;
		this.values[1] = r;
		this.values[2] = g;
		this.values[3] = b;
		this.values[4] = a;
	}
	
	public TRRenderPropertie(int id, float r, float g, float b){
		this.id = id;
		this.values[1] = r;
		this.values[2] = g;
		this.values[3] = b;
	}
	
	public TRRenderPropertie(int id, float value){
		this.id = id;
		this.values[0] = value;
	}
	
	public int getID(){
		return this.id;
	}
	
	public float[] getValues(){
		return this.values;
	}
	
	public float getR(){
		return this.values[1];
	}
	
	public float getG(){
		return this.values[2];
	}
	
	public float getB(){
		return this.values[3];
	}
	
	public float getA(){
		return this.values[4];
	}
	
	public float getValue(){
		return this.values[0];
	}

}
