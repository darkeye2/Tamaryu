package com.tr.engine.grf;

public class Color {
	public static final Color CLEAR = new Color(0, 0, 0, 0);
	public static final Color BLACK = new Color(0, 0, 0, 1);

	public static final Color WHITE = new Color(0xffffffff);
	public static final Color LIGHT_GRAY = new Color(0xbfbfbfff);
	public static final Color GRAY = new Color(0x7f7f7fff);
	public static final Color DARK_GRAY = new Color(0x3f3f3fff);

	public static final Color BLUE = new Color(0, 0, 1, 1);
	public static final Color NAVY = new Color(0, 0, 0.5f, 1);
	public static final Color ROYAL = new Color(0x4169e1ff);
	public static final Color SLATE = new Color(0x708090ff);
	public static final Color SKY = new Color(0x87ceebff);
	public static final Color CYAN = new Color(0, 1, 1, 1);
	public static final Color TEAL = new Color(0, 0.5f, 0.5f, 1);

	public static final Color GREEN = new Color(0x00ff00ff);
	public static final Color CHARTREUSE = new Color(0x7fff00ff);
	public static final Color LIME = new Color(0x32cd32ff);
	public static final Color FOREST = new Color(0x228b22ff);
	public static final Color OLIVE = new Color(0x6b8e23ff);

	public static final Color YELLOW = new Color(0xffff00ff);
	public static final Color GOLD = new Color(0xffd700ff);
	public static final Color GOLDENROD = new Color(0xdaa520ff);
	public static final Color ORANGE = new Color(0xffa500ff);

	public static final Color BROWN = new Color(0x8b4513ff);
	public static final Color TAN = new Color(0xd2b48cff);
	public static final Color FIREBRICK = new Color(0xb22222ff);

	public static final Color RED = new Color(0xff0000ff);
	public static final Color SCARLET = new Color(0xff341cff);
	public static final Color CORAL = new Color(0xff7f50ff);
	public static final Color SALMON = new Color(0xfa8072ff);
	public static final Color PINK = new Color(0xff69b4ff);
	public static final Color MAGENTA = new Color(1, 0, 1, 1);

	public static final Color PURPLE = new Color(0xa020f0ff);
	public static final Color VIOLET = new Color(0xee82eeff);
	public static final Color MAROON = new Color(0xb03060ff);

	/** the red, green, blue and alpha components **/
	public float r, g, b, a;

	/** Constructs a new Color with all components set to 0. */
	public Color () {
	}

	/** @see #rgba8888ToColor(Color, int) */
	public Color (int rgba8888) {
		rgba8888ToColor(this, rgba8888);
	}

	/** Constructor, sets the components of the color
	 * 
	 * @param r the red component
	 * @param g the green component
	 * @param b the blue component
	 * @param a the alpha component */
	public Color (float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		clamp();
	}

	/** Constructs a new color using the given color
	 * 
	 * @param color the color */
	public Color (Color color) {
		set(color);
	}

	/** Sets this color to the given color.
	 * 
	 * @param color the Color */
	public Color set (Color color) {
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
		return this;
	}

	/** Multiplies the this color and the given color
	 * 
	 * @param color the color
	 * @return this color. */
	public Color mul (Color color) {
		this.r *= color.r;
		this.g *= color.g;
		this.b *= color.b;
		this.a *= color.a;
		return clamp();
	}

	/** Multiplies all components of this Color with the given value.
	 * 
	 * @param value the value
	 * @return this color */
	public Color mul (float value) {
		this.r *= value;
		this.g *= value;
		this.b *= value;
		this.a *= value;
		return clamp();
	}

	/** Adds the given color to this color.
	 * 
	 * @param color the color
	 * @return this color */
	public Color add (Color color) {
		this.r += color.r;
		this.g += color.g;
		this.b += color.b;
		this.a += color.a;
		return clamp();
	}

	/** Subtracts the given color from this color
	 * 
	 * @param color the color
	 * @return this color */
	public Color sub (Color color) {
		this.r -= color.r;
		this.g -= color.g;
		this.b -= color.b;
		this.a -= color.a;
		return clamp();
	}

	/** Clamps this Color's components to a valid range [0 - 1]
	 * @return this Color for chaining */
	public Color clamp () {
		if(r>1 || g>1 || b>1 || a>1){
			r /= 255f;
			g /= 255f;
			b /= 255f;
			a /= 255f;
		}
		
		if (r < 0)
			r = 0;
		else if (r > 1) r = 1;

		if (g < 0)
			g = 0;
		else if (g > 1) g = 1;

		if (b < 0)
			b = 0;
		else if (b > 1) b = 1;

		if (a < 0)
			a = 0;
		else if (a > 1) a = 1;
		return this;
	}
	
	public float rgbR(){
		return 255*r;
	}
	
	public float rgbG(){
		return 255*g;
	}
	
	public float rgbB(){
		return 255*b;
	}
	
	public float rgbA(){
		return 255*a;
	}

	/** Sets this Color's component values.
	 * 
	 * @param r Red component
	 * @param g Green component
	 * @param b Blue component
	 * @param a Alpha component
	 * 
	 * @return this Color for chaining */
	public Color set (float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return clamp();
	}

	/** Sets this color's component values through an integer representation.
	 * 
	 * @return this Color for chaining
	 * @see #rgba8888ToColor(Color, int) */
	public Color set (int rgba) {
		rgba8888ToColor(this, rgba);
		return this;
	}

	/** Adds the given color component values to this Color's values.
	 * 
	 * @param r Red component
	 * @param g Green component
	 * @param b Blue component
	 * @param a Alpha component
	 * 
	 * @return this Color for chaining */
	public Color add (float r, float g, float b, float a) {
		this.r += r;
		this.g += g;
		this.b += b;
		this.a += a;
		return clamp();
	}

	/** Subtracts the given values from this Color's component values.
	 * 
	 * @param r Red component
	 * @param g Green component
	 * @param b Blue component
	 * @param a Alpha component
	 * 
	 * @return this Color for chaining */
	public Color sub (float r, float g, float b, float a) {
		this.r -= r;
		this.g -= g;
		this.b -= b;
		this.a -= a;
		return clamp();
	}

	/** Multiplies this Color's color components by the given ones.
	 * 
	 * @param r Red component
	 * @param g Green component
	 * @param b Blue component
	 * @param a Alpha component
	 * 
	 * @return this Color for chaining */
	public Color mul (float r, float g, float b, float a) {
		this.r *= r;
		this.g *= g;
		this.b *= b;
		this.a *= a;
		return clamp();
	}

	/** Linearly interpolates between this color and the target color by t which is in the range [0,1]. The result is stored in
	 * this color.
	 * @param target The target color
	 * @param t The interpolation coefficient
	 * @return This color for chaining. */
	public Color lerp (final Color target, final float t) {
		this.r += t * (target.r - this.r);
		this.g += t * (target.g - this.g);
		this.b += t * (target.b - this.b);
		this.a += t * (target.a - this.a);
		return clamp();
	}

	/** Linearly interpolates between this color and the target color by t which is in the range [0,1]. The result is stored in
	 * this color.
	 * @param r The red component of the target color
	 * @param g The green component of the target color
	 * @param b The blue component of the target color
	 * @param a The alpha component of the target color
	 * @param t The interpolation coefficient
	 * @return This color for chaining. */
	public Color lerp (final float r, final float g, final float b, final float a, final float t) {
		this.r += t * (r - this.r);
		this.g += t * (g - this.g);
		this.b += t * (b - this.b);
		this.a += t * (a - this.a);
		return clamp();
	}

	/** Multiplies the RGB values by the alpha. */
	public Color premultiplyAlpha () {
		r *= a;
		g *= a;
		b *= a;
		return this;
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Color color = (Color)o;
		return toIntBits() == color.toIntBits();
	}




	/** Packs the color components into a 32-bit integer with the format ABGR.
	 * @return the packed color as a 32-bit int. */
	public int toIntBits () {
		int color = ((int)(255 * a) << 24) | ((int)(255 * b) << 16) | ((int)(255 * g) << 8) | ((int)(255 * r));
		return color;
	}

	/** Returns the color encoded as hex string with the format RRGGBBAA. */
	public String toString () {
		String value = Integer
			.toHexString(((int)(255 * r) << 24) | ((int)(255 * g) << 16) | ((int)(255 * b) << 8) | ((int)(255 * a)));
		while (value.length() < 8)
			value = "0" + value;
		return value;
	}

	/** Returns a new color from a hex string with the format RRGGBBAA.
	 * @see #toString() */
	public static Color valueOf (String hex) {
		hex = hex.charAt(0) == '#' ? hex.substring(1) : hex;
		int r = Integer.valueOf(hex.substring(0, 2), 16);
		int g = Integer.valueOf(hex.substring(2, 4), 16);
		int b = Integer.valueOf(hex.substring(4, 6), 16);
		int a = hex.length() != 8 ? 255 : Integer.valueOf(hex.substring(6, 8), 16);
		return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
	}


	/** Packs the color components into a 32-bit integer with the format ABGR. Note that no range checking is performed for higher
	 * performance.
	 * @param r the red component, 0 - 255
	 * @param g the green component, 0 - 255
	 * @param b the blue component, 0 - 255
	 * @param a the alpha component, 0 - 255
	 * @return the packed color as a 32-bit int */
	public static int toIntBits (int r, int g, int b, int a) {
		return (a << 24) | (b << 16) | (g << 8) | r;
	}

	public static int alpha (float alpha) {
		return (int)(alpha * 255.0f);
	}

	public static int luminanceAlpha (float luminance, float alpha) {
		return ((int)(luminance * 255.0f) << 8) | (int)(alpha * 255);
	}

	public static int rgb565 (float r, float g, float b) {
		return ((int)(r * 31) << 11) | ((int)(g * 63) << 5) | (int)(b * 31);
	}

	public static int rgba4444 (float r, float g, float b, float a) {
		return ((int)(r * 15) << 12) | ((int)(g * 15) << 8) | ((int)(b * 15) << 4) | (int)(a * 15);
	}

	public static int rgb888 (float r, float g, float b) {
		return ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
	}

	public static int rgba8888 (float r, float g, float b, float a) {
		return ((int)(r * 255) << 24) | ((int)(g * 255) << 16) | ((int)(b * 255) << 8) | (int)(a * 255);
	}

	public static int argb8888 (float a, float r, float g, float b) {
		return ((int)(a * 255) << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
	}

	public static int rgb565 (Color color) {
		return ((int)(color.r * 31) << 11) | ((int)(color.g * 63) << 5) | (int)(color.b * 31);
	}

	public static int rgba4444 (Color color) {
		return ((int)(color.r * 15) << 12) | ((int)(color.g * 15) << 8) | ((int)(color.b * 15) << 4) | (int)(color.a * 15);
	}

	public static int rgb888 (Color color) {
		return ((int)(color.r * 255) << 16) | ((int)(color.g * 255) << 8) | (int)(color.b * 255);
	}

	public static int rgba8888 (Color color) {
		return ((int)(color.r * 255) << 24) | ((int)(color.g * 255) << 16) | ((int)(color.b * 255) << 8) | (int)(color.a * 255);
	}

	public static int argb8888 (Color color) {
		return ((int)(color.a * 255) << 24) | ((int)(color.r * 255) << 16) | ((int)(color.g * 255) << 8) | (int)(color.b * 255);
	}

	/** Sets the Color components using the specified integer value in the format RGB565. This is inverse to the rgb565(r, g, b)
	 * method.
	 * 
	 * @param color The Color to be modified.
	 * @param value An integer color value in RGB565 format. */
	public static void rgb565ToColor (Color color, int value) {
		color.r = ((value & 0x0000F800) >>> 11) / 31f;
		color.g = ((value & 0x000007E0) >>> 5) / 63f;
		color.b = ((value & 0x0000001F) >>> 0) / 31f;
	}

	/** Sets the Color components using the specified integer value in the format RGBA4444. This is inverse to the rgba4444(r, g,
	 * b, a) method.
	 * 
	 * @param color The Color to be modified.
	 * @param value An integer color value in RGBA4444 format. */
	public static void rgba4444ToColor (Color color, int value) {
		color.r = ((value & 0x0000f000) >>> 12) / 15f;
		color.g = ((value & 0x00000f00) >>> 8) / 15f;
		color.b = ((value & 0x000000f0) >>> 4) / 15f;
		color.a = ((value & 0x0000000f)) / 15f;
	}

	/** Sets the Color components using the specified integer value in the format RGB888. This is inverse to the rgb888(r, g, b)
	 * method.
	 * 
	 * @param color The Color to be modified.
	 * @param value An integer color value in RGB888 format. */
	public static void rgb888ToColor (Color color, int value) {
		color.r = ((value & 0x00ff0000) >>> 16) / 255f;
		color.g = ((value & 0x0000ff00) >>> 8) / 255f;
		color.b = ((value & 0x000000ff)) / 255f;
	}

	/** Sets the Color components using the specified integer value in the format RGBA8888. This is inverse to the rgba8888(r, g,
	 * b, a) method.
	 * 
	 * @param color The Color to be modified.
	 * @param value An integer color value in RGBA8888 format. */
	public static void rgba8888ToColor (Color color, int value) {
		color.r = ((value & 0xff000000) >>> 24) / 255f;
		color.g = ((value & 0x00ff0000) >>> 16) / 255f;
		color.b = ((value & 0x0000ff00) >>> 8) / 255f;
		color.a = ((value & 0x000000ff)) / 255f;
	}

	/** Sets the Color components using the specified integer value in the format ARGB8888. This is the inverse to the argb8888(a,
	 * r, g, b) method
	 *
	 * @param color The Color to be modified.
	 * @param value An integer color value in ARGB8888 format. */
	public static void argb8888ToColor (Color color, int value) {
		color.a = ((value & 0xff000000) >>> 24) / 255f;
		color.r = ((value & 0x00ff0000) >>> 16) / 255f;
		color.g = ((value & 0x0000ff00) >>> 8) / 255f;
		color.b = ((value & 0x000000ff)) / 255f;
	}

	/** @return a copy of this color */
	public Color cpy () {
		return new Color(this);
	}
}
