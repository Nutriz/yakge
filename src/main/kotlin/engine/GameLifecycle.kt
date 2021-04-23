package engine

interface GameLifecycle {
    fun init(window: Window)
    fun input(window: Window)
    fun update(delta: Float)
    fun render(window: Window)
    fun cleanup()
}