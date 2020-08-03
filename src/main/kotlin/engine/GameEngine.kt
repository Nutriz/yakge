package engine

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

class GameEngine {

    private val window = Window()

    fun run() {

        println("Hello LWJGL ${Version.getVersion()} !")

        loop()

        clearAndTerminate()
    }

    private fun clearAndTerminate() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.windowHandle)
        glfwDestroyWindow(window.windowHandle)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun loop() {

        val secsPerUpdate = 1.0 / 30.0
        var previous = glfwGetTime()
        var steps = 0.0

        while (!glfwWindowShouldClose(window.windowHandle)) {
            val start = glfwGetTime()
            val elapsed = start - previous
            previous = start
            steps += elapsed

            handleInput()

            while (steps >= secsPerUpdate) {
                updateGameState()
                steps -= secsPerUpdate
            }

            render()
            sync(start)
        }
    }

    private fun updateGameState() {

    }

    private fun sync(loopStartTime: Double) {
        val loopSlot = 1f / 50
        val endTime = loopStartTime + loopSlot
        while (glfwGetTime() < endTime) {
            Thread.sleep(1)
        }
    }

    private fun handleInput() {
        // Poll for window events. The key callback above will only be invoked during this call.
        glfwPollEvents()
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer
        glfwSwapBuffers(window.windowHandle) // swap the color buffers
    }
}