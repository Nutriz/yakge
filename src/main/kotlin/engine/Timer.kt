package engine

object Timer {

    private var lastLoopTime = time

    val time: Double
        get() = System.nanoTime() / 1000_000_000.0

    val elapsedTime: Float
        get() {
            val time = time
            val elapsedTime = (time - lastLoopTime).toFloat()
            lastLoopTime = time
            return elapsedTime
        }
}