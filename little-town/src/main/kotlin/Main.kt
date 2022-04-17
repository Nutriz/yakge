import engine.GameEngine
import littletown.LittleTown

fun main() {
    println("Starting Little Town Game")

    GameEngine(
        width = 1400,
        height = 1000,
        game = LittleTown()
    ).start()
}