package engine.utils

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

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