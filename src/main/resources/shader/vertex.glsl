#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;

out vec2 outTexCoord;
out vec3 mvVertexPos;
out vec3 mvVertexNormal;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 modelMatrix;

void main()
{
    mvVertexPos = (modelMatrix * vec4(position, 1.0)).xyz;
    mvVertexNormal = (modelMatrix * vec4(vertexNormal, 0.0)).xyz;
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);

    outTexCoord = texCoord;
}