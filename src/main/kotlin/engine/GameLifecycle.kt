package engine

import engine.utils.MouseInput

interface GameLifecycle {
    fun init(window: Window)
    fun input(window: Window, mouseInput: MouseInput)
    fun update(delta: Float, mouseInput: MouseInput)
    fun render(window: Window)
    fun cleanup()
}