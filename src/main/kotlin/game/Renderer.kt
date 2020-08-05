package game

import engine.graphics.ShaderProgram
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.io.File
import java.nio.FloatBuffer

class Renderer {

    private val shaderProgram: ShaderProgram = ShaderProgram(
        File("shader/vertex.glsl").readText(),
        File("shader/fragment.glsl").readText()
    )

    private val vertices = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    var vaoId = 0

    init {
        val verticesBuffer = MemoryUtil.memAllocFloat(vertices.size)
        verticesBuffer.put(vertices).flip()

        createVao()
        createVbo(verticesBuffer)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        MemoryUtil.memFree(verticesBuffer)
    }

    private fun createVao() {
        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)
    }

    private fun createVbo(verticesBuffer: FloatBuffer) {
        val vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
    }

    fun draw() {
        shaderProgram.bind()

        glBindVertexArray(vaoId)
        glDrawArrays(GL_TRIANGLES, 0, 3)

        glBindVertexArray(0)
        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}