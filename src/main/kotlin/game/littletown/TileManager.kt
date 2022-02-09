package game.littletown

import engine.GameItem
import engine.graphics.Material
import engine.graphics.Texture
import engine.utils.ObjLoader
import org.joml.Vector3f
import org.joml.Vector4f

class TileManager {

    val tiles = mutableListOf<Tile>()

    fun initialTiles() {
        repeat(3) { z ->
            repeat(3) { x ->
                val tile = Tile.Water(x, z)
                tiles += tile
            }
        }

        val tile = Tile.Grass(1, 1)
        tiles += tile
    }
}

sealed class Tile(x: Int = 0, y: Float = 0f, z: Int = 0) {
    val gameItem: GameItem = GameItem(x.toFloat(), y, z.toFloat())
    var selected = false

    class Water(x: Int = 0, z: Int = 0): Tile(x, 0f, z) {

        init {
            gameItem.mesh = mesh
            gameItem.mesh.material = material

            val contrast = (50..100).random().toFloat() / 100
            gameItem.tint = Vector4f(Vector3f(0.3f, 0.65f, 1f).mul(contrast), 1f)
        }

        companion object {
            val mesh = ObjLoader.loadMesh("model/tiles/water.obj")
            val material = Material(texture = Texture.loadFromFile("texture/water.png"))
        }
    }

    class Grass(x: Int = 0, z: Int = 0): Tile(x, 0.2f, z) {

        init {
            gameItem.mesh = mesh
            gameItem.mesh.material = material
        }

        companion object {
            val mesh = ObjLoader.loadMesh("model/tiles/grass.obj")
            val material = Material(texture = Texture.loadFromFile("texture/palette.png"))
        }
    }
}