#version 330
precision mediump float;

in vec3 mvVertexPos;
in vec3 mvVertexNormal;
out vec4 fragColor;

struct Material
{
    vec4 ambient;
    int hasTexture;
    int unshaded;
};

struct PointLight
{
    vec3 color;
    vec3 position; // Light position is assumed to be in view coordinates
    float intensity;
};

uniform sampler2D textureSampler;
uniform PointLight light;
uniform Material material;

void main()
{

    // Calculate the ambient color as a percentage of the material color
    float ambientPercent = 0.5;
    vec3 ambientColor = material.ambient.rgb * light.intensity * ambientPercent;

    // Calculate a vector from the fragment location to the light source
    vec3 to_light = light.position - mvVertexPos;
    to_light = normalize(to_light);

    // The vertex's normal vector is being interpolated across the primitive
    // which can make it un-normalized. So normalize the vertex's normal vector.
    vec3 vertex_normal = normalize(mvVertexNormal);

    // Calculate the cosine of the angle between the vertex's normal vector and the vector going to the light.
    float cos_angle = dot(vertex_normal, to_light);
    cos_angle = clamp(cos_angle, 0.0, 1.0);

    // Scale the color of this fragment based on its angle to the light.
    vec3 diffuseColor = material.ambient.rgb * cos_angle * light.intensity;

    if (material.unshaded == 1) {
        fragColor = vec4(material.ambient.rgb, 1);
    } else {
        fragColor = vec4(ambientColor + diffuseColor, 1);
    }
}