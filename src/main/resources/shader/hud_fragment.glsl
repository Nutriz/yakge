#version 320 es
precision mediump float;

in vec2 outTexCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform int hasTexture;
uniform vec4 color;

void main()
{
   if (hasTexture == 1) {
      fragColor = color * texture(textureSampler, outTexCoord);
   } else {
      fragColor = color;
   }
}