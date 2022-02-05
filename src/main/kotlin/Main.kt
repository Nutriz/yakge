
import engine.GameEngine
import game.littletown.LittleTown

fun main() {
    GameEngine(
        width = 1400,
        height = 1000,
        game = LittleTown()
    ).start()
}
