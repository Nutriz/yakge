import engine.GameEngine
import game.TestGame

fun main() {
    GameEngine(
        width = 1200,
        height = 700,
        game = TestGame()
    ).start()
}
