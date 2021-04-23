package game

import engine.GameItem
import engine.Window
import engine.graphics.ShaderProgram
import engine.utils.Log
import engine.utils.PerspectiveConfig
import engine.utils.Transformation
import java.io.File

class Renderer(window: Window) {

    private val shaderProgram: ShaderProgram = ShaderProgram(
        File("shader/vertex.glsl").readText(),
        File("shader/fragment.glsl").readText()
    )

    private val perspectiveConfig = PerspectiveConfig()

    init {
        perspectiveConfig.updateRatio(window)
        Log.info(perspectiveConfig)
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("worldMatrix")

        shaderProgram.createUniform("texture_sampler")
    }

    fun render(window: Window, items: List<GameItem>) {
        perspectiveConfig.updateRatio(window)

        shaderProgram.bind()

        updateProjectionMatrix()
        enableTextureSampler(0)
        renderEachItem(items)

        shaderProgram.unbind()
    }

    private fun enableTextureSampler(unitIndex: Int) {
        shaderProgram.setUniform("texture_sampler", unitIndex)
    }

    private fun updateProjectionMatrix() {
        val projectionMatrix = Transformation.getProjectionMatrix(perspectiveConfig)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)
    }

    private fun renderEachItem(items: List<GameItem>) {
        items.forEach { item ->
            val worldMatrix = Transformation.getWorldMatrix(item.position, item.rotation, item.scale)
            shaderProgram.setUniform("worldMatrix", worldMatrix)
            item.mesh.render()
        }
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}