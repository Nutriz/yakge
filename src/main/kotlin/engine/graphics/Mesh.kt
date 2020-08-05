package engine.graphics

import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import toFloatBuffer
import toIntBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.properties.Delegates

class Mesh(positions: FloatArray, indices: IntArray) {

    var vaoId by Delegates.notNull<Int>()
    private var vboId by Delegates.notNull<Int>()
    private var idxVboId by Delegates.notNull<Int>()

    val vertexCount = indices.size

    init {
        val positionsBuffer = positions.toFloatBuffer()
        val indicesBuffer = indices.toIntBuffer()

        createVao()
        createPosVbo(positionsBuffer)
        createIndicesVbo(indicesBuffer)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    private fun createVao() {
        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)
    }

    private fun createPosVbo(buffer: FloatBuffer) {
        vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
        MemoryUtil.memFree(buffer)
    }

    private fun createIndicesVbo(buffer: IntBuffer) {
        idxVboId = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
        MemoryUtil.memFree(buffer)
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)
        deleteVbos()
        deleteVao()
    }

    private fun deleteVao() {
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }

    private fun deleteVbos() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(vboId)
        glDeleteBuffers(idxVboId)
    }
}