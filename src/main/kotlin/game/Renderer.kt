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
import org.joml.Vector3f
import java.io.File

class Renderer(window: Window) {

    private val perspectiveConfig = PerspectiveConfig()

    private val specularPower = 10f

    private val shaderProgram: ShaderProgram = ShaderProgram(
        File("shader/vertex.glsl").readText(),
        File("shader/fragment.glsl").readText()
    )

    init {
        perspectiveConfig.updateRatio(window)
        Log.info(perspectiveConfig)
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("modelViewMatrix")

        shaderProgram.createUniform("textureSampler")

        shaderProgram.createUniform("ambientLight")
        shaderProgram.createUniform("specularPower")

        // light
        shaderProgram.createUniform("light.color")
        shaderProgram.createUniform("light.position")
        shaderProgram.createUniform("light.intensity")
        shaderProgram.createUniform("light.attenuation.constant")
        shaderProgram.createUniform("light.attenuation.linear")
        shaderProgram.createUniform("light.attenuation.exponent")

        // material
        shaderProgram.createUniform("material.ambient")
        shaderProgram.createUniform("material.diffuse")
        shaderProgram.createUniform("material.specular")
        shaderProgram.createUniform("material.reflectance")
        shaderProgram.createUniform("material.unshaded")
        shaderProgram.createUniform("material.hasTexture")
    }

    fun render(window: Window, camera: Camera, items: List<GameItem>, ambientLight: Vector3f, pointLight: PointLight) {
        perspectiveConfig.updateRatio(window)

        shaderProgram.bind()

        updateProjectionMatrix()

        enableTextureSampler(0)

        val viewMatrix = Transformation.getViewMatrix(camera)
        shaderProgram.setUniform("ambientLight", ambientLight)
        shaderProgram.setUniform("specularPower", specularPower)

        val lightViewPos = Transformation.worldToView(pointLight.position)
        val currPointLight = PointLight(pointLight.color, lightViewPos, pointLight.intensity)
        shaderProgram.setUniform("light", currPointLight)

        renderItems(items, viewMatrix)
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

    private fun renderItems(items: List<GameItem>, viewMatrix: Matrix4f) {
        items.forEach { item ->
            val modelViewMatrix = Transformation.getModelViewMatrix(item, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)

            shaderProgram.setUniform("material", item.mesh.material)

            item.mesh.render()
        }
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}