package game.littletown

import engine.GameItem
import engine.graphics.Material
import engine.graphics.Texture
import engine.utils.ObjLoader
import org.joml.Vector3f
import org.joml.Vector4f

class TileManager {

    val tiles = mutableListOf<GameItem>()

    fun initialTiles() {
        repeat(6) { z ->
            repeat(6) { x ->
                val tile = WaterTile(x, z)
                tiles += tile
            }
        }

        val tile = GrassTile(1, 1)
        tiles += tile
    }

    fun addTile(x: Int, z: Int) {
        if (tiles.any { it.position.x == x.toFloat() && it.position.z == z.toFloat() }) {
            val tile = GrassTile(x, z)
            tiles += tile
        } else {
            val tile = WaterTile(x, z)
            tiles += tile
        }
    }
}

// TODO have an abstract/sealed Tile class
class WaterTile(x: Int = 0, z: Int = 0) : GameItem(x.toFloat(), 0f, z.toFloat()) {

open class WaterTile(x: Int = 0, z: Int = 0) : GameItem(x.toFloat(), -1f, z.toFloat()) {

    init {
        this.mesh = tileMesh
        this.mesh.material = tileMaterial
        val contrast = (50..100).random().toFloat() / 100
        tint = Vector4f(Vector3f(0.3f, 0.65f, 1f).mul(contrast), 1f)
    }

    companion object {
        val tileMesh = ObjLoader.loadMesh("model/tiles/water.obj")
        val tileMaterial = Material(texture = Texture.loadFromFile("texture/water.png"))
    }
}