#version 320 es
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

struct DirectionalLight
{
    vec3 color;
    vec3 direction;
    float intensity;
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
uniform PointLight pointLight;
uniform DirectionalLight directionalLight;
uniform float specularPower;
uniform Material material;

vec4 ambientColor;
vec4 diffuseColor;
vec4 specularColor;

vec4 calcLightColor(vec3 lightColor, float lightIntensity, vec3 position, vec3 toLightDir, vec3 normal) {

    float diffuseFactor = max(dot(normal, toLightDir), 0.0);
    diffuseColor = diffuseColor * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;

    // Specular: Cspec = (Sspec ⊗ Mspec ) (v · r) Mgls
    // specular color = (light.color * material.specular) * (fromPosToview · lightReflection) * material.reflectance
    vec3 toCamDirection = normalize(-position);
    vec3 fromLightSource = -toLightDir;
    vec3 reflectedLight = normalize(reflect(fromLightSource, normal));
    float specularFactor = max(dot(toCamDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularColor = specularColor * specularFactor * material.reflectance * vec4(lightColor, 1);

    return diffuseColor + specularColor;
}

vec4 calcPointLight(PointLight light) {
    vec3 lightDirection = light.position - mvVertexPos;
    vec3 toLightDir  = normalize(lightDirection);
    vec4 lightColor = calcLightColor(light.color, light.intensity, mvVertexPos, toLightDir, mvVertexNormal);

    float distance = length(lightDirection);
    float attenuationInv = light.attenuation.constant + light.attenuation.linear * distance + light.attenuation.exponent * distance * distance;

    return lightColor / attenuationInv;
}

vec4 calcDirectionalLight(DirectionalLight light) {
    return calcLightColor(light.color, light.intensity, mvVertexPos, normalize(light.direction), mvVertexNormal);
}

void setupColor() {
    if (material.hasTexture == 1) {
        ambientColor = texture(textureSampler, outTexCoord);
        diffuseColor = ambientColor;
        specularColor = ambientColor;
    } else {
        ambientColor = material.ambient;
        diffuseColor = material.diffuse;
        specularColor = material.specular;
    }
}

void main()
{
    setupColor();

    vec4 diffuseSpecularComp = vec4(0);

    diffuseSpecularComp += calcDirectionalLight(directionalLight);
    diffuseSpecularComp += calcPointLight(pointLight);

//    diffuseSpecularComp = dir + point;

    if (material.unshaded == 1) {
        fragColor = ambientColor;
    } else {
        fragColor = ambientColor * pointLight.intensity * vec4(ambientLight, 1) + diffuseSpecularComp;
//        fragColor = ambientColor * vec4(ambientLight, 1) + diffuseSpecularComp;
    }
}