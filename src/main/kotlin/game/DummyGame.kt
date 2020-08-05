package game

import com.curiouscreature.kotlin.math.Float3
import engine.GameLogic
import engine.Window
import engine.graphic.ShaderProgram
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*
import java.io.File

class DummyGame : GameLogic {

    private var color = Float3(0.0f)
    private var direction: Int = 0

    lateinit var shader: ShaderProgram

    override fun init() {
        shader = ShaderProgram(
            File("/shader/vertex.glsl").readText(),
            File("/shader/fragment.glsl").readText()
        )
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
        window.setBackgroundColor(color)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        shader.bind()

        // draw triangles

        shader.unbind()
    }

    override fun cleanup() {
        shader.cleanup()
    }
}