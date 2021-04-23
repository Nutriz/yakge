package engine

import com.curiouscreature.kotlin.math.Float3
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_DEPTH_TEST
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.system.MemoryUtil


class Window(
    var width: Int = 400,
    var height: Int = 400,
    var title: String = "Yak Game Engine",
    val vSync: Boolean = true
) {

    var windowHandle: Long = 0
    var isResized = false

    init {
        setupPrintErrorCallback()

        initGlfw()
        createGlfwWindow()

        setupKeyCallback()
        setupResizeCallback()

        centerTheWindow()

        glfwMakeContextCurrent(windowHandle)
        if (vSync) glfwSwapInterval(1)
        glfwShowWindow(windowHandle)

        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST);
    }

    private fun initGlfw() {
        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")
    }

    private fun createGlfwWindow() {
        windowHandle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (windowHandle == MemoryUtil.NULL)
            throw RuntimeException("Failed to create the GLFW window")
    }

    private fun setupPrintErrorCallback() {
        GLFWErrorCallback.createPrint(System.err).set()
    }

    private fun setupKeyCallback() {
        glfwSetKeyCallback(windowHandle) { window, key, _, action, _ ->
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE)
                    glfwSetWindowShouldClose(window, true)
            }
        }
    }

    private fun setupResizeCallback() {
        glfwSetFramebufferSizeCallback(windowHandle) { _: Long, width: Int, height: Int ->
            this.width = width
            this.height = height
            isResized = true
        }
    }

    private fun centerTheWindow() {
        val primaryMonitor = glfwGetPrimaryMonitor()
        val videoMode = glfwGetVideoMode(primaryMonitor)
        if (videoMode != null) {
            glfwSetWindowPos(windowHandle, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2)
        }
    }

    fun setBackgroundColor(
        r: Float = 0.0f,
        g: Float = 0.0f,
        b: Float = 0.0f,
        a: Float = 0.0f
    ) = GL11.glClearColor(r, g, b, a)

    fun setBackgroundColor(
        color: Float3,
        a: Float = 0.0f
    ) = GL11.glClearColor(color.r, color.g, color.b, a)

    fun isKeyPressed(keyCode: Int) = glfwGetKey(windowHandle, keyCode) == GLFW_PRESS

    fun update() {
        glfwSwapBuffers(windowHandle)
        glfwPollEvents()
    }
}