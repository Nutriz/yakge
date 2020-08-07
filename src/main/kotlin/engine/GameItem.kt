package engine

import engine.graphics.Mesh
import org.joml.Vector3f

data class GameItem(
        val mesh: Mesh,
        val position: Vector3f = Vector3f(),
        val scale: Float = 1f,
        val rotation: Vector3f = Vector3f(),
)