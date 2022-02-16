#version 400

in  vec2 outTexCoord;
in float outSelected;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 tint;

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
    vec4 selectedTint = vec4(2, 0, 0, 1);
    if (outSelected > 0) {
        if (material.hasTexture == 1) {
            fragColor = texture(texture_sampler, outTexCoord) * tint * selectedTint;
        } else {
            fragColor = material.ambient * selectedTint;
        }
    } else {
        if (material.hasTexture == 1) {
            fragColor = texture(texture_sampler, outTexCoord) * tint;
        } else {
            fragColor = material.ambient;
        }
    }
}