package engine

import engine.utils.Log
import engine.utils.Timer
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.glViewport

class GameEngine(
    width: Int = 400,
    height: Int = 400,
    private val title: String = "Yak Game Engine",
    private val game: GameLifecycle
) {

    private val window = Window(width, height, title)

    private val targetFps = 60
    private val targetUps = 30

    private val frameInterval = 1.0f / targetFps //  ~0.01666
    private val updateInterval = 1.0f / targetUps // ~0.03333

    fun start() {
        Log.info("Hello LWJGL ${Version.getVersion()} !")

        try {
            game.init(window)
            gameLoop()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cleanup()
        }

        clearAndTerminate()
    }

    private fun gameLoop() {
        var elapsed: Float
        var accumulator: Double = 0.0

        while (!glfwWindowShouldClose(window.windowHandle)) {
            val loopStartTime = Timer.getTime()
            elapsed = Timer.getElapsedTime()
            accumulator += elapsed

            input()

            while (accumulator >= updateInterval) {
                update()
                accumulator -= updateInterval
            }

            render()
            if (window.vSync) sync(loopStartTime) // waiting here before next loop
        }
    }

    private fun update() {
        game.update(updateInterval)
    }

    private fun sync(loopStartTime: Double) {
        val endTime = loopStartTime + frameInterval
        while (Timer.getTime() < endTime) {
            Thread.sleep(1)
        }
    }

    private fun input() {
        game.input(window)
    }

    private fun render() {
        if (window.isResized) {
            glViewport(0, 0, window.width, window.height)
            window.isResized = false
        }
        game.render(window)
        window.update()
    }

    private fun cleanup() {
        game.cleanup()
    }

    private fun clearAndTerminate() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.windowHandle)
        glfwDestroyWindow(window.windowHandle)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }
}