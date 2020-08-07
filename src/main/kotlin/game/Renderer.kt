package game

import Log
import com.curiouscreature.kotlin.math.radians
import engine.Window
import engine.graphics.Mesh
import engine.graphics.ShaderProgram
import org.joml.Matrix4f
import org.lwjgl.opengl.GL30.*
import java.io.File

class Renderer(window: Window) {

    private val projectionMatrix = Matrix4f()

    private val shaderProgram: ShaderProgram = ShaderProgram(
        File("shader/vertex.glsl").readText(),
        File("shader/fragment.glsl").readText()
    )

    data class PerspectiveConfig(
            val fov: Float = radians(60.0f),
            val aspectRatio: Float,
            val zNear: Float = 0.1f,
            val zFar: Float = 1000f
    )

    init {
        val perspectiveConfig = PerspectiveConfig(aspectRatio = window.width.toFloat() / window.height.toFloat())
        Log.info(perspectiveConfig)
        with(perspectiveConfig) {
            projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar)
        }

        shaderProgram.createUniform("projectionMatrix")
    }

    fun draw(meshes: List<Mesh>) {

        shaderProgram.bind()

        shaderProgram.setUniform("projectionMatrix", projectionMatrix)

        meshes.forEach { mesh ->
            glBindVertexArray(mesh.vaoId)
            glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0)
            glBindVertexArray(0)
        }

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}