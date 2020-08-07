package game

import com.curiouscreature.kotlin.math.Float3
import engine.GameLogic
import engine.Window
import engine.graphics.Mesh
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30.*

class DummyGame : GameLogic {

    private var color = Float3(0.0f)
    private var direction: Int = 0

    private lateinit var renderer: Renderer

    lateinit var mesh: Mesh

    override fun init(window: Window) {
        renderer = Renderer(window)

        val positions = floatArrayOf(
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,
        )
        val colours = floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
        )
        val indices = intArrayOf(0, 1, 3, 3, 1, 2)
        mesh = Mesh(positions, colours, indices)
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

        renderer.draw(listOf(mesh))
    }

    override fun cleanup() {
        renderer.cleanup()
        mesh.cleanup()
    }
}