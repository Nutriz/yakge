package engine.graphics

import org.joml.Vector3f

data class DirectionalLight(
    val color: Vector3f = Vector3f(1f, 1f, 1f),
    val direction: Vector3f = Vector3f(),
    var intensity: Float = 1f,
)
