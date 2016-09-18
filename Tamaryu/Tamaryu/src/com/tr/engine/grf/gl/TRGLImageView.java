package com.tr.engine.grf.gl;

import com.jogamp.nativewindow.util.Dimension;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.util.packrect.Rect;
import com.tr.engine.img.ITRImageView;
import com.tr.engine.img.TRImage;
import com.tr.gl.core.GLCamera;
import com.tr.gl.core.GLProgramm;
import com.tr.gl.core.GLTexture;
import com.tr.gl.core.Point3D;

public class TRGLImageView extends TRGL2DRenderable implements ITRImageView {
	
	protected 	float[] data = new float[]{
			//	   X	   Y	 Z			U	V
	         1f,  1f, 0f, 	   1f, 1f,
	        -1f,  1f, 0f,	   0f, 1f,
	         1f, -1f, 0f,	   1f, 0f,
	         1f, -1f, 0f, 	   1f, 0f,
	        -1f,  1f, 0f,	   0f, 1f,
	        -1f, -1f, 0f,	   0f, 0f}; 
	
	
	public TRGLImageView(){
		this.setNormalized(false);
		this.setProgram(new GLProgramm("/shader/", new String[]{"default_f", "default_v"},
				new int[]{GL2ES3.GL_FRAGMENT_SHADER, GL2ES3.GL_VERTEX_SHADER}, "default"));
		this.setData(data, DATA_FORMAT_XYZUV);
	}
	
	public TRGLImageView(TRImage img){
		this();
		this.setImage(img);
	}
	
	protected void setTexturePos(TRImage img){
		float x1, y1, x2, y2;
		x1 = img.getNormalizedX();
		y1 = img.getNormalizedY();
		x2 = x1 + img.getNormalizedWidth();
		y2 = y1 + img.getNormalizedHeight();
		
		data[3+5*0 + 0] = x2;
		data[3+5*0 + 1] = y2;
		data[3+5*1 + 0] = x1;
		data[3+5*1 + 1] = y2;
		data[3+5*2 + 0] = x2;
		data[3+5*2 + 1] = y1;
		data[3+5*3 + 0] = x2;
		data[3+5*3 + 1] = y1;
		data[3+5*4 + 0] = x1;
		data[3+5*4 + 1] = y2;
		data[3+5*5 + 0] = x1;
		data[3+5*5 + 1] = y1;
		
		this.setData(data, DATA_FORMAT_XYZUV);
	}

	@Override
	public void setImage(TRImage i) {
		this.setSize((int)i.getWidth(), (int)i.getHeight());
		//System.out.println("Try to set Texture: "+i.getFullFileName()+" => "+new GLTexture(i.getFullFilePath(), true, i.getName()));
		this.setTexture(new GLTexture(i.getFullFilePath(), true, i.getName()));
		setTexturePos(i);
		this.setData(data, DATA_FORMAT_XYZUV);
	}

	@Override
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		this.setHitbox(new Rect(0, 0, (int)width, (int)height, null));
		this.updateModelMatrix(null);
	}

	@Override
	public Dimension getSize(Dimension d) {
		return new Dimension((int)this.width, (int)this.height);
	}

	@Override
	public void setAnchor(int x, int y, int z) {
		super.setAnchor(x, y, z);
	}

}
