package engine

interface GameLogic {
    fun init()
    fun input(window: Window)
    fun update(delta: Float)
    fun render(window: Window)
    fun cleanup()
}