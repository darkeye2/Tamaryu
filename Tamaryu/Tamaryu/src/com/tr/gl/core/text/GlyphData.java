package com.tr.gl.core.text;

public class GlyphData {
	public final String character;
	public final int id;
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	public final int xoffset;
	public final int yoffset;
	public final int xadvance;

	public GlyphData(String[] glyphDataFragments) {
		String id = glyphDataFragments[1];
		String x = glyphDataFragments[2];
		String y = glyphDataFragments[3];
		String width = glyphDataFragments[4];
		String height = glyphDataFragments[5];
		String xoffset = glyphDataFragments[6];
		String yoffset = glyphDataFragments[7];
		String xadvance = glyphDataFragments[8];

		this.id = Integer.parseInt(id.replace("id=", ""));
		this.x = Integer.parseInt(x.replace("x=", ""));
		this.y = Integer.parseInt(y.replace("y=", ""));
		this.width = Integer.parseInt(width.replace("width=", ""));
		this.height = Integer.parseInt(height.replace("height=", ""));
		this.xoffset = Integer.parseInt(xoffset.replace("xoffset=", ""));
		this.yoffset = Integer.parseInt(yoffset.replace("yoffset=", ""));
		this.xadvance = Integer.parseInt(xadvance.replace("xadvance=", ""));
		this.character = ""+((char)this.id);
	}
}
