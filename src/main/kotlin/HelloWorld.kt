import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

class HelloWorld {

    var windowHandle: Long = 0

    fun run() {

        println("Hello LWJGL ${Version.getVersion()} !")

        init()
        loop()

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle)
        glfwDestroyWindow(windowHandle)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun init() {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        windowHandle = glfwCreateWindow(400, 400, "Hello World!", NULL, NULL)
        if (windowHandle == NULL)
            throw RuntimeException("Failed to create the GLFW window")

        glfwSetKeyCallback(windowHandle) { window, key, _, action, _ ->
            if (action == GLFW_RELEASE) {
                when (key) {
                    GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose(window, true)
                }
            }
        }

        glfwMakeContextCurrent(windowHandle)
        glfwSwapInterval(1)
        glfwShowWindow(windowHandle)
    }

    private fun loop() {
        GL.createCapabilities()

        glClearColor(0.2f, 0.2f, 0.2f, 0.0f)

        while (!glfwWindowShouldClose(windowHandle)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer
            glfwSwapBuffers(windowHandle) // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }
}