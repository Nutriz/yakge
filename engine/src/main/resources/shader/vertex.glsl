#version 400

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;

out vec2 outTexCoord;
out float outSelected;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform float selected;

void main()
{
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
    outTexCoord = texCoord;
    outSelected = selected;
}