package game

import engine.GameItem
import engine.GameLogic
import engine.Window
import engine.graphics.Mesh
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30.*

class DummyGame : GameLogic {

    private lateinit var renderer: Renderer

    private lateinit var mesh: Mesh

    private lateinit var gameItem: GameItem

    override fun init(window: Window) {
        renderer = Renderer(window)

        val positions = floatArrayOf(
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
        )
        val colours = floatArrayOf(
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        )
        val indices = intArrayOf(0, 1, 3, 3, 1, 2)
        mesh = Mesh(positions, colours, indices)
        gameItem = GameItem(mesh)
        gameItem.position.set(0f, 0f, -2f)
    }

    override fun input(window: Window) {

        when {
            window.isKeyPressed(GLFW.GLFW_KEY_X) -> gameItem.scale += 0.01f
            window.isKeyPressed(GLFW.GLFW_KEY_C) -> gameItem.scale -= 0.01f
            else -> gameItem.scale
        }

        when {
            window.isKeyPressed(GLFW.GLFW_KEY_LEFT) -> gameItem.position.x -= 0.01f
            window.isKeyPressed(GLFW.GLFW_KEY_RIGHT) -> gameItem.position.x += 0.01f
            window.isKeyPressed(GLFW.GLFW_KEY_UP) -> gameItem.position.y += 0.01f
            window.isKeyPressed(GLFW.GLFW_KEY_DOWN) -> gameItem.position.y -= 0.01f
        }

        when {
            window.isKeyPressed(GLFW.GLFW_KEY_S) -> {
                var rotation = gameItem.rotation.z + 1.5f
                if (rotation > 360) {
                    rotation = 0f
                }
                gameItem.rotation.z = rotation
            }
        }
    }

    override fun update(delta: Float) {

    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        renderer.render(window, listOf(gameItem))
    }

    override fun cleanup() {
        renderer.cleanup()
        mesh.cleanup()
    }
}