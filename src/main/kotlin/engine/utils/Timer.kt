package engine.utils

object Timer {

    private var lastLoopTime = getTime()

    fun getElapsedTime(): Float {
        val time = getTime()
        val elapsedTime = (time - lastLoopTime).toFloat()
        lastLoopTime = time
        return elapsedTime
    }

    fun getTime() = System.nanoTime() / 1000_000_000.0
}