package game

import engine.GameLogic
import engine.Window
import org.joml.Math
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*

class DummyGame : GameLogic {

    private var color: Float = 0f
    private var direction: Int = 0

    override fun init() {

    }

    override fun input(window: Window) {
        direction = when {
            window.isKeyPressed(GLFW.GLFW_KEY_UP) -> 1
            window.isKeyPressed(GLFW.GLFW_KEY_DOWN) -> -1
            else -> 0
        }
    }

    override fun update(delta: Float) {
        color += direction * 0.01f
    }

    override fun render(window: Window) {
        window.setBackgroundColor(color, color, color, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }
}