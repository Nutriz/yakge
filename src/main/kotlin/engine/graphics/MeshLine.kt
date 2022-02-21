package engine.graphics

import engine.utils.toFloatBuffer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import kotlin.properties.Delegates

class MeshLine(
    positions: FloatArray
) {

    private var vaoId by Delegates.notNull<Int>()
    private var posVboId by Delegates.notNull<Int>()

    init {
        val positionsBuffer = positions.toFloatBuffer()

        vaoId = glGenVertexArrays()
        posVboId = glGenBuffers()
        glBindVertexArray(vaoId)

        glBindBuffer(GL_ARRAY_BUFFER, posVboId)
        glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(0)
        MemoryUtil.memFree(positionsBuffer)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun render() {
        glBindVertexArray(vaoId)
        glDrawArrays(GL_LINES, 0, 2)
        glBindVertexArray(0)
    }

    fun cleanUp() {
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
        glDeleteBuffers(posVboId)
    }
}