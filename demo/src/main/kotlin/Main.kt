
import demo.DemoGame
import engine.GameEngine

fun main() {
    println("Starting Demo Game")

    GameEngine(
        width = 1400,
        height = 1000,
        game = DemoGame()
    ).start()
}