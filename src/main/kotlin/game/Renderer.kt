package game

import engine.Camera
import engine.GameItem
import engine.Window
import engine.graphics.PointLight
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
        shaderProgram.createUniform("modelMatrix")

        shaderProgram.createUniform("textureSampler")

        // light
        shaderProgram.createUniform("light.color")
        shaderProgram.createUniform("light.position")
        shaderProgram.createUniform("light.intensity")

        // material
        shaderProgram.createUniform("material.ambient")
        shaderProgram.createUniform("material.hasTexture")
        shaderProgram.createUniform("material.unshaded")
    }

    fun render(window: Window, camera: Camera, items: List<GameItem>, pointLight: PointLight) {
        perspectiveConfig.updateRatio(window)

        shaderProgram.bind()

        updateProjectionMatrix()

        enableTextureSampler(0)

        val viewMatrix = Transformation.getViewMatrix(camera)
        renderItems(items, viewMatrix, pointLight)
//        Log.info("${items.sumBy { it.mesh.vertexCount / 3 }} rendered triangles")

        shaderProgram.unbind()
    }

    private fun updateProjectionMatrix() {
        val projectionMatrix = Transformation.getProjectionMatrix(perspectiveConfig)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)
    }

    private fun enableTextureSampler(unitIndex: Int) {
        shaderProgram.setUniform("textureSampler", unitIndex)
    }

    private fun renderItems(items: List<GameItem>, viewMatrix: Matrix4f, pointLight: PointLight) {
        items.forEachIndexed { index, item ->
            val modelViewMatrix = Transformation.getModelViewMatrix(item, viewMatrix)
            shaderProgram.setUniform("modelMatrix", Transformation.getModelMatrix(item))
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)

            shaderProgram.setUniform("light", pointLight)
            shaderProgram.setUniform("material", item.mesh.material)

            item.mesh.render()
        }
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}