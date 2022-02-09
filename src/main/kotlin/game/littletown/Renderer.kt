package game.littletown

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

    private val shaderProgram = ShaderProgram(
        File("shader/vertex.glsl").readText(),
        File("shader/fragment.glsl").readText()
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
    }

    fun render(
        window: Window,
        camera: Camera,
        tileManger: TileManager,
        items: List<GameItem>,
    ) {
        renderScene(window, camera, tileManger, items)
    }

    private fun renderScene(
        window: Window,
        camera: Camera,
        tileManger: TileManager,
        items: List<GameItem>
    ) {
        perspectiveConfig.updateRatio(window)

        shaderProgram.bind {
            updateProjectionMatrix()
            shaderProgram.setUniform("textureSampler", 0)
            val viewMatrix = Transformation.getViewMatrix(camera)
            renderTiles(tileManger, viewMatrix)
            renderItems(items, viewMatrix)
        }
    }

    private fun updateProjectionMatrix() {
        val projectionMatrix = Transformation.getProjectionMatrix(perspectiveConfig)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)
    }

    private fun renderTiles(tileManger: TileManager, viewMatrix: Matrix4f) {
        tileManger.tiles.forEach { tile ->
            val item = tile.gameItem
            val modelViewMatrix = Transformation.getModelViewMatrix(item, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)

            shaderProgram.setUniform("material", item.mesh.material)
            shaderProgram.setUniform("tint", item.tint)
            shaderProgram.setUniform("selected", if (tile.selected) 1f else 0f)
            item.mesh.render()
        }
    }

    private fun renderItems(items: List<GameItem>, viewMatrix: Matrix4f) {
        items.forEach { item ->
            val modelViewMatrix = Transformation.getModelViewMatrix(item, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)

            shaderProgram.setUniform("material", item.mesh.material)
            shaderProgram.setUniform("tint", item.tint)

            item.mesh.render()
        }
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}