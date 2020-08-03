import engine.GameEngine
import game.DummyGame

fun main() {
    GameEngine(gameLogic = DummyGame()).run()
}
