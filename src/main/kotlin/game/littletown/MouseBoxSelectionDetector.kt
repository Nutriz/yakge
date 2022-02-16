package game.littletown

import engine.Camera
import engine.GameItem
import engine.Window
import org.joml.Matrix4f
import org.joml.Vector2d
import org.joml.Vector3f
import org.joml.Vector4f


class MouseBoxSelectionDetector : CameraBoxSelectionDetector() {
    private val invProjectionMatrix: Matrix4f = Matrix4f()
    private val invViewMatrix: Matrix4f = Matrix4f()
    private val mouseDir: Vector3f = Vector3f()
    private val tmpVec: Vector4f = Vector4f()

    fun selectGameItem(gameItems: MutableList<GameItem>, window: Window, mousePos: Vector2d, camera: Camera) {
        // Transform mouse coordinates into normalized space [-1, 1]
        val wdwWidth: Int = window.width
        val wdwHeight: Int = window.height
        val x = (2 * mousePos.x).toFloat() / wdwWidth.toFloat() - 1.0f
        val y = 1.0f - (2 * mousePos.y).toFloat() / wdwHeight.toFloat()
        val z = -1.0f
        invProjectionMatrix.set(window.projectionMatrix)
        invProjectionMatrix.invert()
        tmpVec[x, y, z] = 1.0f
        tmpVec.mul(invProjectionMatrix)
        tmpVec.z = -1.0f
        tmpVec.w = 0.0f
        val viewMatrix = camera.viewMatrix
        invViewMatrix.set(viewMatrix)
        invViewMatrix.invert()
        tmpVec.mul(invViewMatrix)
        mouseDir[tmpVec.x, tmpVec.y] = tmpVec.z
        selectGameItem(gameItems, camera.position, mouseDir)
    }
}