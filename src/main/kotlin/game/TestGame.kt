package game

import engine.Camera
import engine.GameItem
import engine.GameLifecycle
import engine.Window
import engine.graphics.Mesh
import engine.graphics.Texture
import engine.utils.MouseInput
import engine.utils.ObjLoader
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL30.*


class TestGame : GameLifecycle {

    private lateinit var renderer: Renderer

    private val camera = Camera()
    private val cameraInc = Vector3f()
    private lateinit var mesh: Mesh

    private val gameItems = mutableListOf<GameItem>()

    override fun init(window: Window) {
        renderer = Renderer(window)

        mesh = ObjLoader.loadMesh("model/cube.obj")
        mesh.texture = Texture.load("texture/grassblock.png")
        val gameItem1 = GameItem(mesh)
        gameItem1.position.set(0f, 0f, -4f)
        gameItem1.rotation.set(0f, 0f, 0f)
        gameItem1.scale *= 1.0f

        gameItems += gameItem1
    }

    override fun input(window: Window, mouseInput: MouseInput) {
        cameraInc.zero()
        when {
            window.isKeyPressed(GLFW_KEY_W) -> cameraInc.z = -1f
            window.isKeyPressed(GLFW_KEY_S) -> cameraInc.z = 1f
        }
        when {
            window.isKeyPressed(GLFW_KEY_A) -> cameraInc.x = -1f
            window.isKeyPressed(GLFW_KEY_D) -> cameraInc.x = 1f
        }
        when {
            window.isKeyPressed(GLFW_KEY_Z) -> cameraInc.y = -1f
            window.isKeyPressed(GLFW_KEY_X) -> cameraInc.y = 1f
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.position.zero()
            camera.rotation.zero()
        }
    }

    override fun update(delta: Float, mouseInput: MouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP)

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed) {
            val relativeDiff = mouseInput.relativeVec
            // X mouse movement must rotate Y axis, Y mouse movement must rotate X axis
            camera.moveRotation(relativeDiff.y * MOUSE_SENSITIVITY, relativeDiff.x * MOUSE_SENSITIVITY, 0f)
        }

//        gameItems.first().rotation.add(0f, 1f, 0f)
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        renderer.render(window, camera, gameItems)
    }

    override fun cleanup() {
        renderer.cleanup()
        mesh.cleanup()
    }

    companion object {
        const val CAMERA_POS_STEP = 0.1f
        const val MOUSE_SENSITIVITY = 0.4f
    }
}