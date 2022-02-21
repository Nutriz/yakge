package engine

class Scene {
    val gameItems = mutableListOf<GameItem>()
    val lineItems = mutableListOf<LineItem>()
    lateinit var tiles: MutableList<GameItem>

    fun cleanUp() {
        gameItems.forEach { item ->
            item.mesh.cleanUp()
        }
        tiles.forEach { tile ->
            tile.mesh.cleanUp()
        }
        lineItems.forEach { line ->
            line.mesh.cleanUp()
        }
    }
}