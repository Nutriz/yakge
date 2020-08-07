import engine.GameEngine
import game.DummyGame

fun main() {
    GameEngine(
            width = 900,
            height = 600,
            gameLogic = DummyGame()
    ).run()
}
