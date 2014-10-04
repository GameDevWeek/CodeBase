#ifdef GL_ES
	precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_effectFactor;


void main()                                  
{         
    float factor = 1.0-u_effectFactor; 

	//vec4 tintColor = vec4(u_effectFactor,1.0-u_effectFactor,1.0-u_effectFactor,1);
	gl_FragColor = factor * texture2D(u_texture, v_texCoords);
}