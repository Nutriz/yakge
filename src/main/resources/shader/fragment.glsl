#version 400

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float reflectance;
    int hasTexture;
    int unshaded;
};
uniform Material material;



void main()
{
    if (material.hasTexture == 1) {
        fragColor = texture(texture_sampler, outTexCoord);
    } else {
        fragColor = material.diffuse;
    }
}