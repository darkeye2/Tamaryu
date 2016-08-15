package com.tr.gl.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.TextureIO;

public final class GLTextureManager {
	// Texture map
	protected static GLTextureMap textures = new GLTextureMap();

	public static GLTexture getTexture(String name) {
		if (textures.containsKey(name)) {
			return textures.get(name);
		}
		return null;
	}

	public static void cleanup(GL2ES3 gl) {
		textures.clearReferences(gl);
	}

	public static GLTexture loadTexture(GL2ES3 gl, GLTexture tex) {
		GLTexture p = getTexture(tex.getName());
		if (p != null)
			return p;

		InputStream in = null;
		if(tex.resource){
			System.out.println("Loading: "+tex.fileName);
			if(tex.fileName.startsWith("/")){
				in = GLTextureManager.class.getResourceAsStream(tex.fileName);
				System.out.println("Texture Stream: "+in);
			}else{
				in = GLTextureManager.class.getResourceAsStream("/img/"+tex.fileName);
			}
		}else{
			try {
				in = new FileInputStream((new File(tex.fileName)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			tex.texture = TextureIO.newTexture(in, false, tex.textureFiletype);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		textures.put(tex.getName(), tex);

		return tex;
	}
}
