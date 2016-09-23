//setting constants
#define USE_DISTANCE_SCALE 1
#define USE_HSL_FILTER 2
#define USE_TEXTURE  3
#define USE_RGB_FILTER 4
#define USE_LIGHTNING 5
#define USE_COLOR_OVER_TEXTURE 6


//position data
layout (location = 1) in vec4 position;
layout (location = 2) in vec2 texpos;
layout (location = 3) in float materialID;

uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 proj_matrix;
uniform vec4 raw_color;
uniform vec4 filter_color;
uniform float settings_array[40];

out VS_OUT{
	vec4 color;
	vec4 filterColor;
	vec2 tc;
	float materialID;
} vs_out;

void main(void){
	//calculate position (mvp*pos)
	gl_Position = proj_matrix * view_matrix * model_matrix * position;
	
	//set distance scale
	if(bool(settings_array[USE_DISTANCE_SCALE])){
		gl_Position = vec4(gl_Position.xy / (-1*gl_Position.z), gl_Position.zw);
	}
	
	//pass data to fragment shader
	vs_out.tc = texpos;		
  	vs_out.color = raw_color;
  	vs_out.filterColor = filter_color;
  	vs_out.materialID = materialID;
  	//vs_out.settings_array = settings_array;
}