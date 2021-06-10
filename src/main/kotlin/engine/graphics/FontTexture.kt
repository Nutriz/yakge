package engine.graphics

import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset
import javax.imageio.ImageIO


data class FontTexture(
    val font: Font,
    val charsetName: String
) {

    data class CharInfo(val startX: Int, val charWidth: Int)

    var width = 0
    var height = 0
    val chars = hashMapOf<Char, CharInfo>()
    lateinit var texture: Texture

    init {
        buildCharMap()
        buildTexture()
    }

    private fun getAllAvailableChars(charsetName: String): List<Char> {
        val encoder = Charset.forName(charsetName).newEncoder()

        val chars = (' '..'ÿ')
        return chars.filter { encoder.canEncode(it) }
    }

    private fun buildCharMap() {
        // Get the font metrics for each character for the selected font by using image
        val img = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        val g2d = img.createGraphics()
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.font = font

        height = g2d.fontMetrics.height
        width = 0
        getAllAvailableChars(charsetName).forEach { char ->
            val charWidth = g2d.fontMetrics.charWidth(char)
            chars[char] = CharInfo(width, charWidth)
            width += charWidth + CharPadding
        }
        g2d.dispose()
    }

    private fun buildTexture() {

        // Create the image associated to the charset
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = img.createGraphics()
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.font = font
        g2d.color = Color.WHITE

        var startX = 0
        chars.entries.forEach { entry ->
            g2d.drawString(entry.key.toString(), startX, g2d.fontMetrics.ascent)
            startX += entry.value.charWidth + CharPadding
        }
        g2d.dispose()

        var buf: ByteBuffer
        ByteArrayOutputStream().use { out ->
            ImageIO.write(img, IMAGE_FORMAT, out)
            out.flush()
            val data = out.toByteArray()
            buf = ByteBuffer.allocateDirect(data.size)
            buf.put(data, 0, data.size)
            buf.flip()
        }

        texture = Texture.loadFromMemory(buf)
    }

    companion object {
        private const val CharPadding = 2
        private const val IMAGE_FORMAT = "png"
    }
}