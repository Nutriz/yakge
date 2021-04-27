package game

import engine.Camera
import engine.GameItem
import engine.GameLifecycle
import engine.Window
import engine.graphics.Mesh
import engine.graphics.Texture
import engine.utils.MouseInput
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

        val positions = floatArrayOf(
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        )
        val texCoords = floatArrayOf(
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        )
        val indices = intArrayOf(
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,
        )
        val texture = Texture.load("texture/grassblock.png")
        mesh = Mesh(positions, texCoords, indices, texture)
        val gameItem1 = GameItem(mesh)
        gameItem1.position.set(0f, -0f, -5f)
        val gameItem2 = GameItem(mesh)
        gameItem2.position.set(1f, 0f, -4f)
        val gameItem3 = GameItem(mesh)
        gameItem3.position.set(1f, 1f, -3f)

        gameItems += gameItem1
        gameItems += gameItem2
        gameItems += gameItem3
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
        const val CAMERA_POS_STEP = 0.05f
        const val MOUSE_SENSITIVITY = 0.4f
    }
}