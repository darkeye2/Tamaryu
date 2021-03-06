//setting constants
#define USE_DISTANCE_SCALE 1	//vertex
#define USE_HSL_FILTER 2		//fragment	(color filter rgb | hsl)	V
#define USE_TEXTURE  3			//fragment	(if 0, use color)			V
#define USE_RGB_FILTER 4		//fragment	(color filter rgb | hsl)	V
#define USE_LIGHTNING 5			//fragment	(not implemented)
#define USE_COLOR_OVER_TEXTURE 6	//fragment	(take alpha value from texture and use color)
#define USE_8BIT_COLOR 7			//fragment	(transform color to 8 bit color)	V
#define USE_PIXELATION 8			//fragment	(only if use texture)
#define USE_GRAYSCALE 9				//fragment	(transform color to grayscale)		V
#define USE_TEXTURE_OVER_TEXTURE 10	//fragment	(not implemented, draw texture 2 over txture 1)
#define USE_OUTLINE 11

#define OUTLINE_COLOR_RED 24
#define OUTLINE_COLOR_GREEN 25
#define OUTLINE_COLOR_BLUE 26
#define COLOR_ALPHA 27
#define COLOR_BLUE 28
#define COLOR_GREEN 29
#define COLOR_RED 30
#define COLOR_FILTER_RED 31
#define COLOR_FILTER_GREEN 32
#define COLOR_FILTER_BLUE 33
#define OVERLAY_COLOR_RED 34
#define OVERLAY_COLOR_GREEN 35
#define OVERLAY_COLOR_BLUE 36
#define OVERLAY_COLOR_ALPHA 37
#define DISTANCE_SCALE_VALUE 38
#define PIXELATION_VALUE 39

layout (binding = 4) uniform sampler2D tex_object;
uniform float settings_array[40];

// Input from vertex shader
in VS_OUT{
	vec4 color;
	vec4 filterColor;
	vec2 tc;
	float materialID;
} fs_in;

// Output to framebuffer
out vec4 color;


//function prototypes
vec4 useHSLFilter(vec4);
vec3 rgbToHsl(vec3);
float hue2rgb(float f1, float f2, float hue);
vec3 hsl2rgb(vec3 hsl);
vec3 hslToRgb(vec3);
vec4 to8bitColor();
vec4 toGrayscale();
vec4 outline(vec4);

void main(void){
	//define base color (texture or default color)
	if(bool(settings_array[USE_TEXTURE])){
		//get color from texture
		if(bool(settings_array[USE_PIXELATION])){
		
		}else {
			color = texture(tex_object, fs_in.tc);
			if(bool(settings_array[USE_OUTLINE])){
				color = outline(vec4(settings_array[OUTLINE_COLOR_RED], 
							settings_array[OUTLINE_COLOR_GREEN],
							settings_array[OUTLINE_COLOR_BLUE], 
							color.a));
			}
		}
		
		//set overlay color
		if(bool(settings_array[USE_COLOR_OVER_TEXTURE])){
			color = vec4(settings_array[OVERLAY_COLOR_RED], 
							settings_array[OVERLAY_COLOR_GREEN],
							settings_array[OVERLAY_COLOR_BLUE], 
							color.a);
		}
	}else {
		color = vec4(settings_array[COLOR_RED],
					settings_array[COLOR_GREEN],
					settings_array[COLOR_BLUE],
					settings_array[COLOR_ALPHA]);
	}
	
	//use hsl filter
	if(bool(settings_array[USE_HSL_FILTER])){
		color = useHSLFilter(vec4(rgbToHsl(vec3(settings_array[COLOR_FILTER_RED], settings_array[COLOR_FILTER_GREEN],
											settings_array[COLOR_FILTER_BLUE])), color.a));
		//color = useHSLFilter(vec4(0.33, 0.5, 0.5, color.a));
	}else if(bool(settings_array[USE_RGB_FILTER])){
		/*color = useHSLFilter(vec4(rgbToHsl(settings_array[COLOR_FILTER_RED], settings_array[COLOR_FILTER_GREEN],
											settings_array[COLOR_FILTER_BLUE]), color.a));*/
	}
	
	//convert to 8bit
	if(bool(settings_array[USE_8BIT_COLOR])){
		to8bitColor();
	}
	
	//convert to grayscale
	if(bool(settings_array[USE_GRAYSCALE])){
		toGrayscale();
	}
}

vec4 outline(vec4 outlineColor){
	ivec2 texSize = textureSize(tex_object, 0);
	float xStep = settings_array[USE_OUTLINE]/texSize.x;
	float yStep = settings_array[USE_OUTLINE]/texSize.y;
	
	float alpha = 4*texture(tex_object, fs_in.tc).a;
	alpha -= texture(tex_object, fs_in.tc + vec2( xStep, 0.0f ) ).a;
	alpha -= texture(tex_object, fs_in.tc + vec2( -xStep, 0.0f ) ).a;
	alpha -= texture(tex_object, fs_in.tc + vec2( 0.0f, yStep ) ).a;
	alpha -= texture(tex_object, fs_in.tc + vec2( 0.0f, -yStep ) ).a;
	
	if(alpha < 0.3){
		return texture(tex_object, fs_in.tc);
	}else{
		outlineColor.a = alpha;
		return outlineColor;
	}
}

vec4 toGrayscale(){
	//get value
	float y = 0.21*color.r + 0.72*color.g + 0.07*color.b;
	
	//set new color
	color.rgb = vec3(y,y,y);
	
	return color;
}

vec4 to8bitColor(){
	//to int rgb
	float r = round(color.r*255);
	float g = round(color.g*255);
	float b = round(color.b*255);
	
	r = round(r*7/255);
	g = round(g*7/255);
	b = round(b*3/255);
	
	r = round(r*255/7);
	g = round(g*255/7);
	b = round(b*255/3);
	
	color.rgb = vec3(r/255,g/255,b/255);
	
	return color;
}

vec4 useHSLFilter(vec4 filterColor){
	//convert color to hsl
	vec3 hsl = rgbToHsl(color.rgb);
	
	//apply filter
	hsl.r = filterColor.r;		//set h to filter color h
	if(hsl.g > 0.1){
		if(hsl.b < 0.9){
			hsl.b -= 0.5 * (0.5 - filterColor.b);
			//hsl.b = clamp(hsl.g, 0.0, 1.0);
		}
		hsl.g = filterColor.g;
	}
	
	//return color
	return vec4(hsl2rgb(hsl), color.a);
}

vec3 hslToRgb(vec3 hsl){
   float r = abs(hsl.x * 6.0 - 3.0) - 1.0;
   float g = 2.0 - abs(hsl.x * 6.0 - 2.0);
   float b = 2.0 - abs(hsl.x * 6.0 - 4.0);
   vec3 hrgb = clamp(vec3(r,g,b), 0.0, 1.0);
   float c = (1.0 - abs(2.0 * hsl.z - 1.0)) * hsl.y;
   
  	return  (hrgb - 0.5) * c + hsl.z;
}

float hue2rgb(float f1, float f2, float hue) {
    if (hue < 0.0)
        hue += 1.0;
    else if (hue > 1.0)
        hue -= 1.0;
    float res;
    if ((6.0 * hue) < 1.0)
        res = f1 + (f2 - f1) * 6.0 * hue;
    else if ((2.0 * hue) < 1.0)
        res = f2;
    else if ((3.0 * hue) < 2.0)
        res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;
    else
        res = f1;
    return res;
}

vec3 hsl2rgb(vec3 hsl) {
    vec3 rgb;
    
    if (hsl.y == 0.0) {
        rgb = vec3(hsl.z); // Luminance
    } else {
        float f2;
        
        if (hsl.z < 0.5)
            f2 = hsl.z * (1.0 + hsl.y);
        else
            f2 = hsl.z + hsl.y - hsl.y * hsl.z;
            
        float f1 = 2.0 * hsl.z - f2;
        
        rgb.r = hue2rgb(f1, f2, hsl.x + (1.0/3.0));
        rgb.g = hue2rgb(f1, f2, hsl.x);
        rgb.b = hue2rgb(f1, f2, hsl.x - (1.0/3.0));
    }   
    return rgb;
}

vec3 rgbToHsl(vec3 col){
    float red   = col.r;
    float green = col.g;
    float blue  = col.b;

    float minc  = min( min(col.r, col.g), col.b );
    float maxc  = max( max(col.r, col.g), col.b );
    float delta = maxc - minc;

    float lum = (minc + maxc) * 0.5;
    float sat = 0.0;
    float hue = 0.0;

    if (lum > 0.0 && lum < 1.0) {
        float mul = (lum < 0.5)  ?  (lum)  :  (1.0-lum);
        sat = delta / (mul * 2.0);
    }

    vec3 masks = vec3(
        (maxc == red   && maxc != green) ? 1.0 : 0.0,
        (maxc == green && maxc != blue)  ? 1.0 : 0.0,
        (maxc == blue  && maxc != red)   ? 1.0 : 0.0
    );

    vec3 adds = vec3(
              ((green - blue ) / delta),
        2.0 + ((blue  - red  ) / delta),
        4.0 + ((red   - green) / delta)
    );

    float deltaGtz = (delta > 0.0) ? 1.0 : 0.0;

    hue += dot( adds, masks );
    hue *= deltaGtz;
    hue /= 6.0;

    if (hue < 0.0)
        hue += 1.0;

    return vec3( hue, sat, lum);
}







