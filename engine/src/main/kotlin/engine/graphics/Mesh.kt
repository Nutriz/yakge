package engine.graphics

import engine.utils.Log
import engine.utils.toFloatBuffer
import engine.utils.toIntBuffer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.properties.Delegates

class Mesh(
    positions: FloatArray,
    texCoords: FloatArray,
    normals: FloatArray,
    indices: IntArray,
    var material: Material = Material(),
) {

    private var vaoId by Delegates.notNull<Int>()
    private var posVboId by Delegates.notNull<Int>()
    private var texCoordsVboId by Delegates.notNull<Int>()
    private var idxVboId by Delegates.notNull<Int>()
    private var normalsVboId by Delegates.notNull<Int>()

    val vertexCount = indices.size

    init {
        val positionsBuffer = positions.toFloatBuffer()
        val texCoordsBuffer = texCoords.toFloatBuffer()
        val normalsBuffer = normals.toFloatBuffer()
        val indicesBuffer = indices.toIntBuffer()

        Log.debug("Mesh in memory size ${ (positionsBuffer.capacity() + texCoordsBuffer.capacity() + normalsBuffer.capacity() + indicesBuffer.capacity()) * 4 * 0.001} kb")

        createVao()
        createPosVbo(positionsBuffer)
        createTexCoordsVbo(texCoordsBuffer)
        createNormalsVbo(normalsBuffer)
        createIndicesVbo(indicesBuffer)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    private fun createVao() {
        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)
    }

    private fun createPosVbo(buffer: FloatBuffer) {
        posVboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, posVboId)
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


    private fun createNormalsVbo(buffer: FloatBuffer) {
        normalsVboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, normalsVboId)
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0)
        MemoryUtil.memFree(buffer)
    }

    private fun createIndicesVbo(buffer: IntBuffer) {
        idxVboId = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
        MemoryUtil.memFree(buffer)
    }

    fun render() {
        if (material.texture != null) {
            glActiveTexture(GL_TEXTURE0)
            material.texture?.bind()
        }

        glBindVertexArray(vaoId)
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

    fun deleteBuffers() {
        glDisableVertexAttribArray(0)
        deleteVbos()
        deleteVao()
    }

    fun cleanUp() {
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        deleteVbos()
        material.texture?.cleanup()
        deleteVao()
    }

    private fun deleteVao() {
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)
    }

    private fun deleteVbos() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(posVboId)
        glDeleteBuffers(texCoordsVboId)
        glDeleteBuffers(normalsVboId)
        glDeleteBuffers(idxVboId)
    }
}