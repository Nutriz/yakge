import engine.GameEngine
import game.TestGame

fun main() {
    GameEngine(
        width = 900,
        height = 600,
        game = TestGame()
    ).start()
}
