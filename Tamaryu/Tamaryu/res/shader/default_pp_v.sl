//position data
layout (location=1) in vec4 position;

const vec2 madd=vec2(0.5,0.5);

out vec2 texCord;

void main(void){
	//pass through vertex
	gl_Position = position;
	
	//calculate texture position
	texCord = position.xy*madd+madd;
}