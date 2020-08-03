package engine

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*

class GameEngine(width: Int = 400,
                 height: Int = 400,
                 title: String = "Yak Game Engine",
                 val gameLogic: GameLogic) : Runnable {

    private val window = Window(width, height, title)

    private val targetFps = 75
    private val targetUps = 30

    private val interval = 1.0f / targetUps
    private val loopSlot = 1f / targetFps

    override fun run() {
        println("Hello LWJGL ${Version.getVersion()} !")

        gameLogic.init()
        gameLoop()

        clearAndTerminate()
    }

    private fun clearAndTerminate() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.windowHandle)
        glfwDestroyWindow(window.windowHandle)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun gameLoop() {
        var elapsed: Float
        var accumulator = 0.0

        while (!glfwWindowShouldClose(window.windowHandle)) {
            elapsed = Timer.elapsedTime
            accumulator += elapsed

            input()

            while (accumulator >= interval) {
                update()
                accumulator -= interval
            }

            render()
            sync(Timer.time)
        }
    }

    private fun update() {
        gameLogic.update(interval)
    }

    private fun sync(loopStartTime: Double) {
        val endTime = loopStartTime + loopSlot
        while (Timer.time < endTime) {
            Thread.sleep(1)
        }
    }

    private fun input() {
        gameLogic.input(window)
    }

    private fun render() {
        gameLogic.render(window)
        window.update()
    }
}