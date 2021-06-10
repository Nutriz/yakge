package engine.graphics

import engine.utils.Log
import org.lwjgl.opengl.GL30.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class Texture(private val textureId: Int, val width: Int, val height: Int) {

    fun bind() = glBindTexture(GL_TEXTURE_2D, textureId)

    fun cleanup() = glDeleteTextures(textureId)

    init {
        Log.info("Texture id $textureId created with $width * $height")
    }

    companion object {

        private const val RGBA = 4

        fun loadFromFile(fileName: String) = MemoryStack.stackPush().use { stack ->
            val widthBuffer = stack.mallocInt(1)
            val heightBuffer = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            val buffer: ByteBuffer = STBImage.stbi_load(fileName, widthBuffer, heightBuffer, channels, RGBA)
                ?: throw Exception("Image file " + fileName + " not loaded: " + STBImage.stbi_failure_reason())

            val width = widthBuffer.get()
            val height = heightBuffer.get()

            val textureId = createTexture(buffer, width, height)

            STBImage.stbi_image_free(buffer)

            Texture(textureId, width, height)
        }

        fun loadFromMemory(imageBuffer: ByteBuffer) = MemoryStack.stackPush().use { stack ->
            val widthBuffer = stack.mallocInt(1)
            val heightBuffer = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            val buffer: ByteBuffer =
                STBImage.stbi_load_from_memory(imageBuffer, widthBuffer, heightBuffer, channels, RGBA)
                    ?: throw Exception("Image buffer not loaded: " + STBImage.stbi_failure_reason())

            val width = widthBuffer.get()
            val height = heightBuffer.get()

            val textureId = createTexture(buffer, width, height)

            STBImage.stbi_image_free(buffer)

            Texture(textureId, width, height)
        }

        private fun createTexture(buf: ByteBuffer, width: Int, height: Int): Int {
            // Create a new OpenGL texture
            val textureId = glGenTextures()
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, textureId)

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)
            glGenerateMipmap(GL_TEXTURE_2D)
            return textureId
        }
    }
}