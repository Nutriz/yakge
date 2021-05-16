package engine.graphics

import org.joml.Vector3f

data class PointLight(
    val color: Vector3f = Vector3f(1f, 1f, 1f),
    val position: Vector3f = Vector3f(),
    var intensity: Float = 1f
)