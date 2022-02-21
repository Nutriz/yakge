package game.littletown.util

import engine.Camera
import engine.Window
import game.littletown.GameSettings
import org.joml.*

class GridSelectionDetector : CameraBoxSelectionDetector() {
    private val invProjectionMatrix: Matrix4f = Matrix4f()
    private val invViewMatrix: Matrix4f = Matrix4f()
    private val mouseDir: Vector3f = Vector3f()
    private val tmpVec: Vector4f = Vector4f()

    fun mouseToWorldPosition(window: Window, mousePos: Vector2d, camera: Camera): Pair<Int, Int> {
        val wdwWidth: Int = window.width
        val wdwHeight: Int = window.height
        val x = (2 * mousePos.x).toFloat() / wdwWidth.toFloat() - 1.0f
        val y = 1.0f - (2 * mousePos.y).toFloat() / wdwHeight.toFloat()
        val z = -1.0f
        window.projectionMatrix.invert(invProjectionMatrix)
        tmpVec[x, y, z] = 1.0f
        tmpVec.mul(invProjectionMatrix)
        tmpVec.z = -1.0f
        tmpVec.w = 0.0f
        val viewMatrix = camera.viewMatrix
        viewMatrix.invert(invViewMatrix)
        tmpVec.mul(invViewMatrix)
        mouseDir[tmpVec.x, tmpVec.y] = tmpVec.z

        return getTilePosition(camera.position, mouseDir)
    }

    private fun getTilePosition(center: Vector3f, mouseDir: Vector3f): Pair<Int, Int> {
        var closestDistance = Float.POSITIVE_INFINITY

        val nearFar = Vector2f()

        var closestPoint = 0 to 0

        val size = GameSettings.GridSize
        for (y in -size..size) {
            for (x in -size..size) {
                val max = Vector3f(x.toFloat(), -1f, y.toFloat())
                val min = Vector3f(x.toFloat(), -1f, y.toFloat())
                min.add(-1f, -1f, -1f)
                max.add(1f, 1f, 1f)
                if (Intersectionf.intersectRayAab(center, mouseDir, min, max, nearFar) && nearFar.x < closestDistance) {
                    closestDistance = nearFar.x
                    closestPoint = x to y
                }
            }
        }
        return closestPoint
    }
}