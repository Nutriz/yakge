package engine.graphics

import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import toFloatBuffer
import java.nio.FloatBuffer
import kotlin.properties.Delegates

class Mesh(positions: FloatArray) {

    var vaoId by Delegates.notNull<Int>()
    private var vboId by Delegates.notNull<Int>()

    val vertexCount = positions.size / 3

    init {
        val positionsBuffer = positions.toFloatBuffer()

        createVao()
        createVbo(positionsBuffer)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        MemoryUtil.memFree(positionsBuffer)
    }

    private fun createVao() {
        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)
    }

    private fun createVbo(verticesBuffer: FloatBuffer) {
        vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)
        deleteVbo()
        deleteVao()
    }

    private fun deleteVao() {
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }

    private fun deleteVbo() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(vboId)
    }
}