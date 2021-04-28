package engine.utils

import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.random.Random

fun getRandomColor() = Vector3f(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

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