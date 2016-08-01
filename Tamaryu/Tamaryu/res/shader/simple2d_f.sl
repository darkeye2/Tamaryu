layout (binding = 2) uniform sampler2D tex_object;

// Input from vertex shader
in VS_OUT{
	vec4 color;
	vec2 tc;
	vec3 filterColor;
} fs_in;

// Output to framebuffer
out vec4 color;


//function prototypes
vec4 useHSLFilter(vec4);
vec3 rgbToHsl(vec3);
vec3 hslToRgb(vec3);

void main(void){
	vec4 fc = vec4(0, 0, 0.4, 1);
	//color = texture2D(tex_object, texCord).rgb;
	color = texture(tex_object, fs_in.tc);
	color = useHSLFilter(vec4(rgbToHsl(fs_in.filterColor), color.a));
	//color = useHSLFilter(vec4(287.0/360.0, 73.0/100.0, 50.0/100, color.a));
	color = vec4(1.0, 0.0, 0.0, 0.5);
	//gl_FragColor = texture2D(tex_object, gl_TexCoord[0].st);
	//color = texture2D(tex_object, gl_TexCoord[0].st);
	//color = texelFetch(tex_object, texCord, 0);
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







