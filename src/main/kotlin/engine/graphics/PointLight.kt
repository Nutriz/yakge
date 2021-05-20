package engine.graphics

import org.joml.Vector3f

data class PointLight(
    val color: Vector3f = Vector3f(1f, 1f, 1f),
    val position: Vector3f = Vector3f(),
    var intensity: Float = 1f,
    val attenuation: Attenuation = Attenuation()
)

data class Attenuation(
    val constant: Float = 1f,
    val linear: Float = 0f,
    val exponent: Float = 0.5f,
)

