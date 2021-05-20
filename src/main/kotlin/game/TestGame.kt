package game

import engine.Camera
import engine.GameItem
import engine.GameLifecycle
import engine.Window
import engine.graphics.Material
import engine.graphics.PointLight
import engine.graphics.Texture
import engine.utils.Color
import engine.utils.MouseInput
import engine.utils.ObjLoader
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL30.*


class TestGame : GameLifecycle {

    private var renderer: Renderer? = null

    private val camera = Camera()
    private val cameraInc = Vector3f()
    private val ambientLight = Vector3f(0.3f, 0.3f, 0.3f)
    private val light = PointLight(
        color = Vector3f(1f, 1f, 1f),
        position = Vector3f(0f, 3f, -2f)
    )
    private val lightInc = Vector3f()

    private val gameItems = mutableListOf<GameItem>()

    override fun init(window: Window) {
        renderer = Renderer(window)
        window.setBackgroundColor(0.2f, 0.2f, 0.8f)

        camera.movePosition(1f, 4f, 3f)
        camera.moveRotation(30f, 0f, 0f)

        val mapMesh = ObjLoader.loadMesh("model/map.obj")
        mapMesh.material = Material(ambient = Color.white)
        val map = GameItem(mapMesh)
        map.position.set(0f, 0f, -6f)

        val cubeMesh = ObjLoader.loadMesh("model/cube.obj")
        cubeMesh.material = Material(ambient = Color.white, texture = Texture.load("texture/grassblock.png"))
        val cube = GameItem(cubeMesh, scale = 0.5f)
        cube.position.set(4f, 0.5f, -2f)

//        val ironmanMesh = ObjLoader.loadMesh("model/ironman.obj")
//        val ironman = GameItem(ironmanMesh)
//        ironman.position.set(4f, 0f, -5f)
//        ironman.rotation.set(0f, 0f, 0f)
//        ironman.scale *= 0.02f

        initRgbSphere()

        val lightMesh = ObjLoader.loadMesh("model/sphere.obj")
        lightMesh.material = Material(Vector4f(1f, 1f, 1f, 1f), unshaded = true)
        val light = GameItem(lightMesh)
        light.scale *= 0.05f

        gameItems += map
        gameItems += cube
//        gameItems += ironman
        gameItems += light
    }

    private fun initRgbSphere() {
        val meshR = ObjLoader.loadMesh("model/sphere.obj")
        meshR.material = Material(Color.red)
        val gameItemR = GameItem(meshR)
        gameItemR.position.set(1f, 1f, 0f)
        gameItemR.scale = 0.5f

        val meshG = ObjLoader.loadMesh("model/sphere.obj")
        meshG.material = Material(Color.green)
        val gameItemG = GameItem(meshG)
        gameItemG.position.set(2f, 1f, 0f)
        gameItemG.scale = 0.5f

        val meshB = ObjLoader.loadMesh("model/sphere.obj")
        meshB.material = Material(Color.blue)
        val gameItemB = GameItem(meshB)
        gameItemB.position.set(3f, 1f, 0f)
        gameItemB.scale = 0.5f

        gameItems += gameItemR
        gameItems += gameItemG
        gameItems += gameItemB
    }

    override fun input(window: Window, mouseInput: MouseInput) {
        cameraInc.zero()
        lightInc.zero()
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
        when {
            window.isKeyPressed(GLFW_KEY_LEFT) -> lightInc.x = -1f
            window.isKeyPressed(GLFW_KEY_RIGHT) -> lightInc.x = 1f
        }
        when {
            window.isKeyPressed(GLFW_KEY_UP) -> lightInc.z = 1f
            window.isKeyPressed(GLFW_KEY_DOWN) -> lightInc.z = -1f
        }
        when {
            window.isKeyPressed(GLFW_KEY_PAGE_UP) -> lightInc.y = 1f
            window.isKeyPressed(GLFW_KEY_PAGE_DOWN) -> lightInc.y = -1f
        }
        when {
            window.isKeyPressed(GLFW_KEY_O) -> light.intensity -= 0.01f
            window.isKeyPressed(GLFW_KEY_P) -> light.intensity += 0.01f
        }

        light.intensity = light.intensity.coerceAtLeast(0f)
        light.intensity = light.intensity.coerceAtMost(1f)

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.position.zero()
            camera.rotation.zero()
        }
    }

    override fun update(delta: Float, mouseInput: MouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP)

        light.position.add(lightInc.mul(CAMERA_POS_STEP))

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed) {
            val relativeDiff = mouseInput.relativeVec
            // X mouse movement must rotate Y axis, Y mouse movement must rotate X axis
            camera.moveRotation(relativeDiff.y * MOUSE_SENSITIVITY, relativeDiff.x * MOUSE_SENSITIVITY, 0f)
        }

//        gameItems.first { it.mesh.vertexCount > 300000 }.rotation.add(0f, 1f, 0f)
        gameItems.last().position.set(light.position)
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        renderer?.render(window, camera, gameItems, ambientLight, light)
    }

    override fun cleanup() {
        renderer?.cleanup()
        gameItems.forEach { item ->
            item.mesh.cleanup()
        }
    }

    companion object {
        const val CAMERA_POS_STEP = 0.1f
        const val MOUSE_SENSITIVITY = 0.4f
    }
}