package game

import engine.Camera
import engine.GameItem
import engine.Hud
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

    private val shaderProgram = ShaderProgram(
        File("shader/vertex.glsl").readText(),
        File("shader/fragment.glsl").readText()
    )

    private val hudShaderProgram = ShaderProgram(
        File("shader/hud_vertex.glsl").readText(),
        File("shader/hud_fragment.glsl").readText()
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

        hudShaderProgram.createUniform("projModelMatrix")
        hudShaderProgram.createUniform("hasTexture")
        hudShaderProgram.createUniform("color")
    }

    fun render(
        window: Window,
        camera: Camera,
        items: List<GameItem>,
        ambientLight: Vector3f,
        pointLight: PointLight,
        hud: Hud
    ) {
        renderScene(window, camera, ambientLight, pointLight, items)
        renderHud(window, hud)
    }

    private fun renderScene(
        window: Window,
        camera: Camera,
        ambientLight: Vector3f,
        pointLight: PointLight,
        items: List<GameItem>
    ) {
        perspectiveConfig.updateRatio(window)

        shaderProgram.bind()

        updateProjectionMatrix()

        shaderProgram.setUniform("textureSampler", 0)

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

    private fun renderHud(window: Window, hud: Hud) {
        hudShaderProgram.bind()

        val ortho = Transformation.getOrthoProjectionMatrix(0f, window.width.toFloat(), window.height.toFloat(), 0f)

        hud.gameItems.forEach { item ->
            val projModelMatrix = Transformation.getOrtoProjModelMatrix(item, ortho)
            hudShaderProgram.setUniform("projModelMatrix", projModelMatrix)
            val hasTexture = if(item.mesh.material.hasTexture) 1 else 0
            hudShaderProgram.setUniform("hasTexture", hasTexture)
            hudShaderProgram.setUniform("color", item.mesh.material.diffuse)
            item.mesh.render()
        }

        hudShaderProgram.unbind()
    }

    private fun updateProjectionMatrix() {
        val projectionMatrix = Transformation.getProjectionMatrix(perspectiveConfig)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)
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