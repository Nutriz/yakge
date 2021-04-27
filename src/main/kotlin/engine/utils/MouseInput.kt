package engine.utils

import engine.Window
import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*


class MouseInput(window: Window) {

    val previousPos = Vector2d()
    val currentPos = Vector2d()
    /**
     * The mouse position relative to the previous position (position at the last frame)
     */
    val relativeVec = Vector2f()

    var inWindow = true
    var isLeftButtonPressed = false
    var isRightButtonPressed = false

    init {
        glfwSetCursorPosCallback(window.windowHandle) { _, x, y ->
            currentPos.set(x, y)
        }
        glfwSetCursorEnterCallback(window.windowHandle) { _, entered ->
            inWindow = entered
        }
        glfwSetMouseButtonCallback(window.windowHandle) { _, button, action, _ ->
            isLeftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            isRightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
        }
    }

    fun input() {
        relativeVec.zero()
        if (inWindow) {
            val deltaX = currentPos.x - previousPos.x
            val deltaY = currentPos.y - previousPos.y
            if (deltaX != 0.0)
                relativeVec.x = deltaX.toFloat()
            if (deltaY != 0.0)
                relativeVec.y = deltaY.toFloat()
        }
        previousPos.set(currentPos)
    }
}
