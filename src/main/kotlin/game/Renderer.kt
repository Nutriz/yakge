package game

import engine.Camera
import engine.GameItem
import engine.Window
import engine.graphics.ShaderProgram
import engine.utils.Log
import engine.utils.PerspectiveConfig
import engine.utils.Transformation
import org.joml.Matrix4f
import java.io.File

class Renderer(window: Window) {

    private val perspectiveConfig = PerspectiveConfig()

    private val shaderProgram: ShaderProgram = ShaderProgram(
        File("shader/vertex.glsl").readText(),
        File("shader/fragment.glsl").readText()
    )

    init {
        perspectiveConfig.updateRatio(window)
        Log.info(perspectiveConfig)
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("modelViewMatrix")

        shaderProgram.createUniform("texture_sampler")
    }

    fun render(window: Window, camera: Camera, items: List<GameItem>) {
        perspectiveConfig.updateRatio(window)

        shaderProgram.bind()

        updateProjectionMatrix()

        enableTextureSampler(0)

        val viewMatrix = Transformation.getViewMatrix(camera)
        renderItems(items, viewMatrix)

        shaderProgram.unbind()
    }

    private fun updateProjectionMatrix() {
        val projectionMatrix = Transformation.getProjectionMatrix(perspectiveConfig)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)
    }

    private fun enableTextureSampler(unitIndex: Int) {
        shaderProgram.setUniform("texture_sampler", unitIndex)
    }

    private fun renderItems(items: List<GameItem>, viewMatrix: Matrix4f) {
        items.forEach { item ->
            val modelViewMatrix = Transformation.getModelViewMatrix(item, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
            item.mesh.render()
        }
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}