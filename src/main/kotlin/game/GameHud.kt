package game

import engine.GameItem
import engine.Hud
import engine.graphics.FontTexture
import java.awt.Font

class GameHud: Hud {

    private val items = mutableListOf<GameItem>()

    override val gameItems: MutableList<GameItem>
        get() = items

    companion object {
        private val ArialFont20 = Font("Arial", Font.PLAIN, 20)
        private const val Charset = "ISO-8859-1"
        val DefaultFontTexture = FontTexture(ArialFont20, Charset)
    }
}