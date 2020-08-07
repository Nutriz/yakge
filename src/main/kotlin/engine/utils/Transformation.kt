package engine.utils

import com.curiouscreature.kotlin.math.radians
import game.PerspectiveConfig
import org.joml.Matrix4f
import org.joml.Vector3f

object Transformation {

    private val projectionMatrix: Matrix4f = Matrix4f()
    private val worldMatrix: Matrix4f = Matrix4f()

    fun getProjectionMatrix(fov: Float, width: Float, height: Float, zNear: Float, zFar: Float): Matrix4f {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar)
    }

    fun getProjectionMatrix(perspectiveConfig: PerspectiveConfig): Matrix4f {
        with(perspectiveConfig) {
            return projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar)
        }
    }

    fun getWorldMatrix(offset: Vector3f, rotation: Vector3f, scale: Float): Matrix4f {
        return worldMatrix.translation(offset)
                .rotateX(radians(rotation.x))
                .rotateY(radians(rotation.y))
                .rotateZ(radians(rotation.z))
                .scale(scale)
    }
}