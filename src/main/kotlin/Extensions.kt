import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

fun FloatArray.toFloatBuffer(): FloatBuffer {
    val positionsBuffer = MemoryUtil.memAllocFloat(size)
    positionsBuffer.put(this).flip()
    return positionsBuffer
}