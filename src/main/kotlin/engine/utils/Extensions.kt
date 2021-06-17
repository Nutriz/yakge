package engine.utils

import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.system.MemoryUtil
import java.math.RoundingMode
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.text.NumberFormat
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

fun getRandomColor() = Vector4f(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)

fun FloatArray.toFloatBuffer(): FloatBuffer {
    val floatBuffer = MemoryUtil.memAllocFloat(size)
    floatBuffer.put(this).flip()
    return floatBuffer
}

fun IntArray.toIntBuffer(): IntBuffer {
    val intBuffer = MemoryUtil.memAllocInt(size)
    intBuffer.put(this).flip()
    return intBuffer
}

fun Float.toRadians(): Float {
    return com.curiouscreature.kotlin.math.radians(this)
}

@OptIn(ExperimentalTime::class)
inline fun <T> measureAndLog(textToLog: String = "measured", block: () -> T): T {
    val timed = measureTimedValue(block)
    Log.debug("$textToLog ${timed.duration}")
    return timed.value
}

fun Matrix4f.toNormalizedString(): String {
    val formatter = NumberFormat.getInstance()
    formatter.roundingMode = RoundingMode.HALF_UP
    formatter.maximumFractionDigits = 2
    formatter.minimumFractionDigits = 2
    return this.toString(formatter)
}

val Vector4f.xyz: Vector3f
    get() = Vector3f(this.x, this.y, this.z)
