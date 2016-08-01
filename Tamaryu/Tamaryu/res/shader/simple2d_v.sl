uniform mat4 mv_matrix;
uniform mat4 proj_matrix;
uniform vec3 color;

layout (location = 1) in vec4 position;
layout (location = 2) in vec2 texpos;

out VS_OUT{
	vec4 color;
	vec2 tc;
	vec3 filterColor;
} vs_out;

void main(void){
	gl_Position = proj_matrix * mv_matrix * position;
	gl_Position = vec4(gl_Position.xy / (-1*gl_Position.z), gl_Position.zw);
	vs_out.tc = texpos;
  	vs_out.color = position * 2.0 + vec4(0.5, 0.5, 0.5, 0.0);
  	vs_out.filterColor = color;
}