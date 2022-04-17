package engine.utils

import org.lwjgl.opengl.GL11.*

object Yakge {
    fun clearColor() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)
    }
}