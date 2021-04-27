package engine

import engine.utils.toRadians
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin
class Camera(val position: Vector3f = Vector3f(), val rotation: Vector3f = Vector3f()) {

    fun setPosition(x: Float, y: Float, z: Float) = position.set(x, y, z)

    fun movePosition(offsetX: Float, offsetY: Float, offsetZ: Float) {
        if (offsetX != 0f) {
            position.x += sin((rotation.y - 90).toRadians()) * -1.0f * offsetX
            position.z += cos((rotation.y - 90).toRadians()) * offsetX
        }
        if (offsetZ != 0f) {
            position.x += sin(rotation.y.toRadians()) * -1.0f * offsetZ
            position.z += cos(rotation.y.toRadians()) * offsetZ
        }
        position.y += offsetY
    }

    fun setRotation(x: Float, y: Float, z: Float): Vector3f = rotation.set(x, y, z)

    fun moveRotation(offsetX: Float, offsetY: Float, offsetZ: Float): Vector3f = rotation.add(offsetX, offsetY, offsetZ)
}

