#version 330
precision mediump float;

in vec3 mvVertexPos;
in vec3 mvVertexNormal;
in vec2 outTexCoord;

out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    vec3 position; // Light position is assumed to be in view coordinates
    float intensity;
    Attenuation attenuation;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float reflectance;
    int hasTexture;
    int unshaded;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform PointLight light;
uniform float specularPower;
uniform Material material;

void main()
{
    // Setup color if material textured or not
    vec4 ambientColor;
    vec4 diffuseColor;
    vec4 specularColor;
    if (material.hasTexture == 1) {
        ambientColor = texture(textureSampler, outTexCoord);
        diffuseColor = ambientColor;
        specularColor = ambientColor;
    } else {
        ambientColor = material.ambient;
        diffuseColor = material.diffuse;
        specularColor = material.specular;
    }

    // Diffuse
    vec3 lightDirection = light.position - mvVertexPos;
    vec3 toLightSource  = normalize(lightDirection);

    float diffuseFactor = max(dot(mvVertexNormal, toLightSource), 0.0);
    diffuseColor = diffuseColor * vec4(light.color, 1.0) * light.intensity * diffuseFactor;

    // Specular: Cspec = (Sspec ⊗ Mspec ) (v · r) Mgls
    // specular color = (light.color * material.specular) * (fromPosToview · lightReflection) * material.reflectance
    vec3 toCamDirection = normalize(-mvVertexPos);
    vec3 fromLightSource = -toLightSource;
    vec3 reflectedLight = normalize(reflect(fromLightSource, mvVertexNormal));
    float specularFactor = max(dot(toCamDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularColor = specularColor * specularFactor * material.reflectance * vec4(light.color, 1);

    float distance = length(lightDirection);
    float attenuationInv = light.attenuation.constant + light.attenuation.linear * distance + light.attenuation.exponent * distance * distance;

    if (material.unshaded == 1) {
        fragColor = ambientColor;
    } else {
        vec4 diffuseSpecularComp = (diffuseColor + specularColor) / attenuationInv;

        fragColor = ambientColor * light.intensity * vec4(ambientLight, 1) + diffuseSpecularComp;
    }
}