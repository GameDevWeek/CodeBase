#ifdef GL_ES
	precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;


void main()                                  
{         
	vec4 tintColor = vec4(1,0,0,1);
	gl_FragColor = tintColor * texture2D(u_texture, v_texCoords);
}