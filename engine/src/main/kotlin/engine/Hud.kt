package engine


interface Hud {

    val gameItems: MutableList<GameItem>

    fun cleanup() {
        for (gameItem in gameItems) {
            gameItem.mesh.cleanUp()
        }
    }
}