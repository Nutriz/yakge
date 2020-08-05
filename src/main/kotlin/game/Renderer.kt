package game

import engine.graphics.Mesh
import engine.graphics.ShaderProgram
import org.lwjgl.opengl.GL30.*
import java.io.File

class Renderer {

    private val shaderProgram: ShaderProgram = ShaderProgram(
        File("shader/vertex.glsl").readText(),
        File("shader/fragment.glsl").readText()
    )

    init {

    }

    fun draw(meshes: List<Mesh>) {
        shaderProgram.bind()

        meshes.forEach { mesh ->
            glBindVertexArray(mesh.vaoId)
            glDrawArrays(GL_TRIANGLES, 0, mesh.vertexCount)
            glBindVertexArray(0)
        }

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}