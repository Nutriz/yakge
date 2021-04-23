package engine.graphics

import engine.utils.toFloatBuffer
import engine.utils.toIntBuffer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.properties.Delegates

class Mesh(positions: FloatArray, texCoords: FloatArray, indices: IntArray, private val texture: Texture) {

    private var vaoId by Delegates.notNull<Int>()
    private var vboId by Delegates.notNull<Int>()
    private var texCoordsVboId by Delegates.notNull<Int>()
    private var idxVboId by Delegates.notNull<Int>()

    private val vertexCount = indices.size

    init {
        val positionsBuffer = positions.toFloatBuffer()
        val texCoordsBuffer = texCoords.toFloatBuffer()
        val indicesBuffer = indices.toIntBuffer()

        createVao()
        createPosVbo(positionsBuffer)
        createTexCoordsVbo(texCoordsBuffer)
        createIndicesVbo(indicesBuffer)

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
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        MemoryUtil.memFree(buffer)
    }

    private fun createTexCoordsVbo(buffer: FloatBuffer) {
        texCoordsVboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, texCoordsVboId)
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)
        MemoryUtil.memFree(buffer)
    }

    private fun createIndicesVbo(buffer: IntBuffer) {
        idxVboId = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
        MemoryUtil.memFree(buffer)
    }

    fun render() {
        glActiveTexture(GL_TEXTURE0)
        texture.bind()

        glBindVertexArray(vaoId)
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

    private fun deleteVao() {
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }

    private fun deleteVbos() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(vboId)
        glDeleteBuffers(texCoordsVboId)
        glDeleteBuffers(idxVboId)
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        deleteVbos()
        texture.cleanup()
        deleteVao()
    }
}