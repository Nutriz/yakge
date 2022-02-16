package game.test

import engine.Camera
import engine.GameItem
import engine.Hud
import engine.Window
import engine.graphics.DirectionalLight
import engine.graphics.PointLight
import engine.graphics.ShaderProgram
import engine.utils.Log
import engine.utils.PerspectiveConfig
import engine.utils.Transformation
import engine.utils.xyz
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
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

        // point light
        shaderProgram.createUniform("pointLight.color")
        shaderProgram.createUniform("pointLight.position")
        shaderProgram.createUniform("pointLight.intensity")
        shaderProgram.createUniform("pointLight.attenuation.constant")
        shaderProgram.createUniform("pointLight.attenuation.linear")
        shaderProgram.createUniform("pointLight.attenuation.exponent")

        // directional light
        shaderProgram.createUniform("directionalLight.color")
        shaderProgram.createUniform("directionalLight.direction")
        shaderProgram.createUniform("directionalLight.intensity")

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
        directionalLight: DirectionalLight,
        hud: Hud
    ) {
        renderScene(window, camera, ambientLight, pointLight, directionalLight, items)
        renderHud(window, hud)
    }

    private fun renderScene(
        window: Window,
        camera: Camera,
        ambientLight: Vector3f,
        pointLight: PointLight,
        directionalLight: DirectionalLight,
        items: List<GameItem>
    ) {
        perspectiveConfig.updateRatio(window)

        shaderProgram.bind()

        updateProjectionMatrix(window)

        shaderProgram.setUniform("textureSampler", 0)

        val viewMatrix = camera.updateViewMatrix()
        shaderProgram.setUniform("ambientLight", ambientLight)
        shaderProgram.setUniform("specularPower", specularPower)

        val lightViewPos = Transformation.worldToView(pointLight.position, viewMatrix)
        val currPointLight = PointLight(pointLight.color, lightViewPos, pointLight.intensity)
        shaderProgram.setUniform("pointLight", currPointLight)

        val dir = Vector4f(directionalLight.direction, 0f).apply { mul(viewMatrix) }
        val currDirectionalLight = DirectionalLight(directionalLight.color, dir.xyz, directionalLight.intensity)
        shaderProgram.setUniform("directionalLight", currDirectionalLight)

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

    private fun updateProjectionMatrix(window: Window) {
        val projectionMatrix = window.getProjectionMatrix(perspectiveConfig)
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