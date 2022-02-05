package game.littletown

import engine.Camera
import engine.Window
import engine.utils.MouseInput
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

class MyCamera {

    val camera = Camera()
    private val cameraInc = Vector3f()

    fun cameraInput(window: Window) {
        cameraInc.zero()
        when {
            window.isKeyPressed(GLFW.GLFW_KEY_W) -> cameraInc.z = -1f
            window.isKeyPressed(GLFW.GLFW_KEY_S) -> cameraInc.z = 1f
        }
        when {
            window.isKeyPressed(GLFW.GLFW_KEY_A) -> cameraInc.x = -1f
            window.isKeyPressed(GLFW.GLFW_KEY_D) -> cameraInc.x = 1f
        }
        when {
            window.isKeyPressed(GLFW.GLFW_KEY_Z) -> cameraInc.y = -1f
            window.isKeyPressed(GLFW.GLFW_KEY_X) -> cameraInc.y = 1f
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            resetPosition()
        }
    }

    private fun resetPosition() {
        camera.position.zero()
        camera.rotation.zero()
    }

    fun move() {
        camera.movePosition(
            cameraInc.x * CAMERA_POS_STEP,
            cameraInc.y * CAMERA_POS_STEP,
            cameraInc.z * CAMERA_POS_STEP
        )
    }

    fun rotate(mouseInput: MouseInput) {
        val relativeDiff = mouseInput.relativeVec
        // X mouse movement must rotate Y axis, Y mouse movement must rotate X axis
        camera.moveRotation(
            relativeDiff.y * MOUSE_SENSITIVITY,
            relativeDiff.x * MOUSE_SENSITIVITY,
            0f
        )
    }

    companion object {
        const val CAMERA_POS_STEP = 0.1f
        const val MOUSE_SENSITIVITY = 0.4f
    }
}