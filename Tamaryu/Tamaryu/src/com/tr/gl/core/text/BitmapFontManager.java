package com.tr.gl.core.text;

public final class BitmapFontManager {
	protected static BMFMap fonts = new BMFMap();
	
	public static BitmapFont load(String fntName){
		if(fonts.contains(fntName+".fnt")){
			return fonts.get(fntName+".fnt");
		}else{
			BitmapFont bmf = new BitmapFont("/font/"+fntName+".fnt", "/font/"+fntName+".png", true);
			fonts.put(fntName+".fnt", bmf);
			return bmf;
		}
	}
	
	public static BitmapFont load(String fntPath, String pngPath, boolean resource){
		if(fonts.contains(fntPath)){
			return fonts.get(fntPath);
		}else{
			BitmapFont bmf = new BitmapFont(fntPath, pngPath, resource);
			fonts.put(fntPath, bmf);
			return bmf;
		}
	}
}
