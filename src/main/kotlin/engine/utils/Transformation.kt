package engine.utils

import engine.Camera
import engine.GameItem
import engine.Window
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f


data class PerspectiveConfig(
    val fov: Float = 60.0f.toRadians(),
    var aspectRatio: Float = 1f,
    val zNear: Float = 0.1f,
    val zFar: Float = 1000f
) {
    fun updateRatio(window: Window) {
        aspectRatio = window.width.toFloat() / window.height.toFloat()
    }
}

object Transformation {

    private val projectionMatrix: Matrix4f = Matrix4f()
    private val modelViewMatrix: Matrix4f = Matrix4f()
    private val viewMatrix: Matrix4f = Matrix4f()

    fun getProjectionMatrix(fov: Float, width: Float, height: Float, zNear: Float, zFar: Float): Matrix4f {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar)
    }

    fun getProjectionMatrix(perspectiveConfig: PerspectiveConfig): Matrix4f {
        with(perspectiveConfig) {
            return projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar)
        }
    }

    fun getModelViewMatrix(gameItem: GameItem, viewMatrix: Matrix4f): Matrix4f {
        val rotation = gameItem.rotation
        modelViewMatrix.set(viewMatrix)
            .translate(gameItem.position)
            .rotateX(-rotation.x.toRadians())
            .rotateY(-rotation.y.toRadians())
            .rotateZ(-rotation.z.toRadians())
            .scale(gameItem.scale)
        return modelViewMatrix
    }

    fun getViewMatrix(camera: Camera): Matrix4f {
        val cameraPos = camera.position
        val cameraRot = camera.rotation
        viewMatrix.identity()
        // First do the rotation so camera rotates over its position
        viewMatrix
            .rotate(cameraRot.x.toRadians(), Vector3f(1f, 0f, 0f))
            .rotate(cameraRot.y.toRadians(), Vector3f(0f, 1f, 0f))
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)
        return viewMatrix
    }

    fun worldToView(position: Vector3f): Vector3f {
        val viewPos = Vector4f(position, 1f).mul(viewMatrix)
        return Vector3f(viewPos.x, viewPos.y, viewPos.z)
    }
}