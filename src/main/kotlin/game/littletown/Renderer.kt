package game.littletown

import engine.*
import engine.graphics.ShaderProgram
import engine.utils.Log
import engine.utils.PerspectiveConfig
import engine.utils.Transformation
import org.joml.Matrix4f
import java.io.File


class Renderer(window: Window) {

    private val perspectiveConfig = PerspectiveConfig()

    private val shaderProgram = ShaderProgram(
        vertexSource = File("shader/vertex.glsl").readText(),
        fragmentSource = File("shader/fragment.glsl").readText()
    )

    private val lineShaderProgram = ShaderProgram(
        vertexSource = File("shader/line_vertex.glsl").readText(),
        fragmentSource = File("shader/line_fragment.glsl").readText()
    )

    init {
        perspectiveConfig.updateRatio(window)
        Log.info(perspectiveConfig)
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("modelViewMatrix")

        // material
        shaderProgram.createUniform("material.ambient")
        shaderProgram.createUniform("material.diffuse")
        shaderProgram.createUniform("material.specular")
        shaderProgram.createUniform("material.reflectance")
        shaderProgram.createUniform("material.unshaded")
        shaderProgram.createUniform("material.hasTexture")

        shaderProgram.createUniform("tint")
        shaderProgram.createUniform("selected")

        // Line shader
        lineShaderProgram.createUniform("projectionMatrix")
        lineShaderProgram.createUniform("viewMatrix")
        lineShaderProgram.createUniform("color")
    }

    fun render(
        window: Window,
        camera: Camera,
        scene: Scene,
    ) {
        renderScene(window, camera, scene.gameItems, scene.tiles)
        renderLines(window, camera, scene.lineItems)
    }

    private fun renderScene(
        window: Window,
        camera: Camera,
        items: List<GameItem>,
        tiles: List<GameItem>,
    ) {
        perspectiveConfig.updateRatio(window)

        shaderProgram.bind {
            updateProjectionMatrix(window)
            shaderProgram.setUniform("textureSampler", 0)
            renderItems(items, camera.viewMatrix)
            renderItems(tiles, camera.viewMatrix)
        }
    }

    private fun renderLines(window: Window, camera: Camera, lineItems: MutableList<LineItem>) {
        perspectiveConfig.updateRatio(window)

        lineShaderProgram.bind {
            val projectionMatrix = window.getProjectionMatrix(perspectiveConfig)
            lineShaderProgram.setUniform("projectionMatrix", projectionMatrix)

            lineItems.forEach { line ->
                lineShaderProgram.setUniform("viewMatrix", camera.viewMatrix)
                lineShaderProgram.setUniform("color", line.color)
                line.renderLine()
            }
        }
    }

    private fun updateProjectionMatrix(window: Window) {
        val projectionMatrix = window.getProjectionMatrix(perspectiveConfig)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)
    }

    private fun renderItems(gameItems: List<GameItem>, viewMatrix: Matrix4f) {
        gameItems.forEach { gameItem ->
            val modelViewMatrix = Transformation.getModelViewMatrix(gameItem, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)

            shaderProgram.setUniform("material", gameItem.mesh.material)
            shaderProgram.setUniform("tint", gameItem.tint)
            shaderProgram.setUniform("selected", if (gameItem.selected) 1f else 0f)
            gameItem.mesh.render()
        }
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}