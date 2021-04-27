package engine

import com.curiouscreature.kotlin.math.radians
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin
class Camera(val position: Vector3f = Vector3f(), val rotation: Vector3f = Vector3f()) {

    fun setPosition(x: Float, y: Float, z: Float) = position.set(x, y, z)

    fun movePosition(offsetX: Float, offsetY: Float, offsetZ: Float) {
        if (offsetX != 0f) {
            position.x += sin(radians((rotation.y - 90))) * -1.0f * offsetX
            position.z += cos(radians((rotation.y - 90))) * offsetX
        }
        if (offsetZ != 0f) {
            position.x += sin(radians(rotation.y)) * -1.0f * offsetZ
            position.z += cos(radians(rotation.y)) * offsetZ
        }
        position.y += offsetY
    }

    fun setRotation(x: Float, y: Float, z: Float): Vector3f = rotation.set(x, y, z)

    fun moveRotation(offsetX: Float, offsetY: Float, offsetZ: Float): Vector3f = rotation.add(offsetX, offsetY, offsetZ)
}

