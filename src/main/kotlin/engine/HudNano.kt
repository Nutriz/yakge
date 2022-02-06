package engine

import engine.utils.toNativeByteBuffer
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.system.MemoryUtil.NULL
import java.io.File
import java.nio.ByteBuffer

class HudNano(window: Window) {
    private var vg: Long = 0
    private var colour: NVGColor
    private var fontBuffer: ByteBuffer
    private var counter = 0

    class TextItem(var text: String, var x: Int, var y: Int)

    val textItems = hashMapOf<String, TextItem>()

    init {
        vg = if (window.options.antialiasing) nvgCreate(NVG_ANTIALIAS or NVG_STENCIL_STROKES) else nvgCreate(
            NVG_STENCIL_STROKES)
        if (vg == NULL) {
            throw Exception("Could not init nanovg")
        }
        fontBuffer = File("font/JetBrainsMono-Regular.ttf").toNativeByteBuffer()
        val font: Int = nvgCreateFontMem(vg, FONT_NAME_REGULAR, fontBuffer, 0)
        if (font == -1) {
            throw Exception("Could not add font")
        }
        colour = NVGColor.create()
        counter = 0
    }

    fun render(window: Window) {
        nvgBeginFrame(vg, window.width.toFloat(), window.height.toFloat(), 1f)

        drawTexts()

        nvgEndFrame(vg)

        // Restore state
        window.restoreState()
    }

    private fun drawTexts() {
        nvgFontSize(vg, 18.0f)
        nvgFontFace(vg, FONT_NAME_REGULAR)
        nvgTextAlign(vg, NVG_ALIGN_CENTER or NVG_ALIGN_TOP)
        nvgFillColor(vg, rgba(0x23, 0xa1, 0xf1, 255, colour))
        for (textItem in textItems) {
            nvgText(vg, textItem.value.x.toFloat(), textItem.value.y.toFloat(), textItem.value.text)
        }
    }

    fun addText(key: String, text: String, x: Int, y: Int) {
        if (key in textItems) {
            textItems[key]?.text = text
            textItems[key]?.x = x
            textItems[key]?.y = y
        } else {
            textItems[key] = TextItem(text, x, y)
        }
    }

    private fun rgba(r: Int, g: Int, b: Int, a: Int, colour: NVGColor?): NVGColor {
        colour!!.r(r / 255.0f)
        colour.g(g / 255.0f)
        colour.b(b / 255.0f)
        colour.a(a / 255.0f)
        return colour
    }

    fun cleanup() {
        nvgDelete(vg)
    }

    companion object {
        private const val FONT_NAME_REGULAR = "REGULAR"
    }
}
