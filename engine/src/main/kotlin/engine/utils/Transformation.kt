package engine.utils

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

    private val orthoMatrix: Matrix4f = Matrix4f()
    private val modelViewMatrix: Matrix4f = Matrix4f()

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

    fun updateGenericViewMatrix(position: Vector3f, rotation: Vector3f, viewMatrix: Matrix4f): Matrix4f {
        viewMatrix.identity()
        // First do the rotation so camera rotates over its position
        viewMatrix
            .rotate(rotation.x.toRadians(), Vector3f(1f, 0f, 0f))
            .rotate(rotation.y.toRadians(), Vector3f(0f, 1f, 0f))
        // Then do the translation
        viewMatrix.translate(-position.x, -position.y, -position.z)
        return viewMatrix
    }

    fun worldToView(position: Vector3f, viewMatrix: Matrix4f): Vector3f {
        val viewPos = Vector4f(position, 1f).mul(viewMatrix)
        return Vector3f(viewPos.x, viewPos.y, viewPos.z)
    }

    fun getOrthoProjectionMatrix(left: Float, right: Float, bottom: Float, top: Float): Matrix4f {
        orthoMatrix.identity()
        orthoMatrix.setOrtho2D(left, right, bottom, top)
        return orthoMatrix
    }

    fun getOrtoProjModelMatrix(textItem: GameItem, orthoMatrix: Matrix4f?): Matrix4f {
        val rotation = textItem.rotation
        val modelMatrix = Matrix4f()
        modelMatrix.identity()
            .translate(textItem.position)
            .rotateX(-rotation.x.toRadians())
            .rotateY(-rotation.y.toRadians())
            .rotateZ(-rotation.z.toRadians())
            .scale(textItem.scale)
        val orthoMatrixCurr = Matrix4f(orthoMatrix)
        orthoMatrixCurr.mul(modelMatrix)
        return orthoMatrixCurr
    }
}