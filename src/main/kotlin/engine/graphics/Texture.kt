package engine.graphics

import org.lwjgl.opengl.GL30.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class Texture(private val textureId: Int) {

    fun bind() = glBindTexture(GL_TEXTURE_2D, textureId)

    fun cleanup() = glDeleteTextures(textureId)

    companion object {

        private const val RGBA = 4

        fun load(fileName: String) = MemoryStack.stackPush().use { stack ->
            val width = stack.mallocInt(1)
            val height = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            val buffer: ByteBuffer = STBImage.stbi_load(fileName, width, height, channels, RGBA)
                ?: throw Exception("Image file " + fileName  + " not loaded: " + STBImage.stbi_failure_reason())

            val textureId = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, textureId)
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
            glGenerateMipmap(GL_TEXTURE_2D)

            STBImage.stbi_image_free(buffer)

            Texture(textureId)
        }
    }
}