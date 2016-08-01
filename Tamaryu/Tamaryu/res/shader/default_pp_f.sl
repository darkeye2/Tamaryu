//setting constants
#define TARGET_WIDTH 0
#define TARGET_HEIGHT 1
#define USE_HSL_FILTER 2		//fragment	(color filter rgb | hsl)	V
#define USE_RGB_FILTER 3		//fragment	(color filter rgb | hsl)	V
#define USE_LIGHTNING 4			//fragment	(not implemented)
#define USE_8BIT_COLOR 5			//fragment	(transform color to 8 bit color)	V
#define USE_PIXELATION 6			//fragment	(only if use texture)				V
#define USE_GRAYSCALE 7				//fragment	(transform color to grayscale)		V

#define COLOR_FILTER_RED 31
#define COLOR_FILTER_GREEN 32
#define COLOR_FILTER_BLUE 33
#define OVERLAY_COLOR_RED 34
#define OVERLAY_COLOR_GREEN 35
#define OVERLAY_COLOR_BLUE 36
#define OVERLAY_COLOR_ALPHA 37
#define PIXELATION_VALUE 38
#define LIGHT_INTENSITY 39
#define LIGHT_COLOR_RED = 40
#define LIGHT_COLOR_GREEN = 41
#define LIGHT_COLOR_BLUE = 42


uniform float settings_array[60];
uniform sampler2D pp_texture;

in vec2 texCord;
out vec4 color;

//function prototypes
vec4 useHSLFilter(vec4);
vec3 rgbToHsl(vec3);
vec3 hslToRgb(vec3);
vec4 to8bitColor();
vec4 toGrayscale();
vec4 crossHatch();
vec4 pixelate();

void main() {
	color = texture(pp_texture, texCord);
}

vec4 crossHatch(){
	float lum_threshold_1 = 1.0;
	float lum_threshold_2 = 0.7;
	float lum_threshold_3 = 0.5;
	float lum_threshold_4 = 0.3;
	vec3 tc = vec3(1.0, 0.0, 0.0);
	vec2 uv = texCord;
	
	float lum = length(texture2D(pp_texture, uv).rgb);
    tc = vec3(1.0, 1.0, 1.0);
  
    if (lum < lum_threshold_1) 
    {
      if (mod(gl_FragCoord.x + gl_FragCoord.y, 10.0) == 0.0) 
        tc = vec3(0.0, 0.0, 0.0);
    }  
  
    if (lum < lum_threshold_2) 
    {
      if (mod(gl_FragCoord.x - gl_FragCoord.y, 10.0) == 0.0) 
        tc = vec3(0.0, 0.0, 0.0);
    }  
  
    if (lum < lum_threshold_3) 
    {
      if (mod(gl_FragCoord.x + gl_FragCoord.y - 1, 10.0) == 0.0) 
        tc = vec3(0.0, 0.0, 0.0);
    }  
  
    if (lum < lum_threshold_4) 
    {
      if (mod(gl_FragCoord.x - gl_FragCoord.y - 1, 10.0) == 0.0) 
        tc = vec3(0.0, 0.0, 0.0);
    }
    color = vec4(tc, 1.0);
    
    return color;
}

vec4 toGrayscale(){
	//get value
	float y = 0.21*color.r + 0.72*color.g + 0.07*color.b;
	
	//set new color
	color.rgb = vec3(y,y,y);
	
	return color;
}

vec4 pixelate(){
	vec2 uv = texCord;
	float rt_w = settings_array[TARGET_WIDTH];
	float rt_h = settings_array[TARGET_HEIGHT];
	float pixel_w = settings_array[PIXELATION_VALUE];
	float pixel_h = pixel_w;
	
	float dx = pixel_w*(1./rt_w);
    float dy = pixel_h*(1./rt_h);
    vec2 coord = vec2(dx*floor(uv.x/dx),
                      dy*floor(uv.y/dy));
    vec3 tc = texture(pp_texture, coord).rgb;
    color = vec4(tc, 1.0);
    
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
	return vec4(hslToRgb(hsl), color.a);
}

vec3 hslToRgb(vec3 hsl){
   float r = abs(hsl.x * 6.0 - 3.0) - 1.0;
   float g = 2.0 - abs(hsl.x * 6.0 - 2.0);
   float b = 2.0 - abs(hsl.x * 6.0 - 4.0);
   vec3 rgb = clamp(vec3(r,g,b), 0.0, 1.0);
   float c = (1.0 - abs(2.0 * hsl.z - 1.0)) * hsl.y;
   
   return (rgb - 0.5) * c + hsl.z;
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