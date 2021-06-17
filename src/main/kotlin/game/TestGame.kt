package game

import engine.*
import engine.graphics.DirectionalLight
import engine.graphics.Material
import engine.graphics.PointLight
import engine.graphics.Texture
import engine.utils.*
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL30.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


class TestGame : GameLifecycle {

    private var renderer: Renderer? = null

    private val camera = Camera()
    private val cameraInc = Vector3f()
    private val ambientLight = Vector3f(0.3f, 0.3f, 0.3f)
    private val pointLight = PointLight(
        color = Vector3f(1f, 1f, 1f),
        position = Vector3f(0f, 3f, -2f)
    )

    private val directionalLight = DirectionalLight(intensity = 0.2f)
    private var lightAngle = -90f

    private val lightInc = Vector3f()

    private val gameItems = mutableListOf<GameItem>()
    private lateinit var hud: GameHud

    override fun init(window: Window) {
        renderer = Renderer(window)
        window.setBackgroundColor(0.2f, 0.2f, 0.8f)

        camera.movePosition(1f, 4f, 3f)
        camera.moveRotation(30f, 0f, 0f)

        val mapMesh = ObjLoader.loadMesh("model/map.obj")
        mapMesh.material = Material(ambient = Color.white)
        val map = GameItem().apply { mesh = mapMesh }
        map.position.set(0f, 0f, -6f)

        val cubeMesh = ObjLoader.loadMesh("model/cube.obj")
        cubeMesh.material = Material(ambient = Color.white, texture = Texture.loadFromFile("texture/grassblock.png"))
        val cube = GameItem().apply { mesh = cubeMesh }
        cube.scale = 0.5f
        cube.position.set(4f, 0.5f, -2f)

        val ironmanMesh = ObjLoader.loadMesh("model/ironman.obj")
        val ironman = GameItem().apply { mesh = ironmanMesh }
        ironman.position.set(4f, 0f, -5f)
        ironman.rotation.set(0f, 0f, 0f)
        ironman.scale *= 0.02f

        initRgbSphere()

        val lightMesh = ObjLoader.loadMesh("model/sphere.obj")
        lightMesh.material = Material(Vector4f(1f, 1f, 1f, 1f), unshaded = true)
        val light = GameItem().apply { mesh = lightMesh }
        light.scale *= 0.05f

        gameItems += map
        gameItems += cube
//        gameItems += ironman
        gameItems += light

        hud = GameHud()
        val item = TextItem("A", GameHud.DefaultFontTexture)
        item.mesh.material.diffuse = Color.white
        hud.gameItems += item
    }

    private fun initRgbSphere() {
        val meshR = ObjLoader.loadMesh("model/sphere.obj")
        meshR.material = Material(Color.red)
        val gameItemR = GameItem().apply { mesh =  meshR }
        gameItemR.position.set(1f, 1f, 0f)
        gameItemR.scale = 0.5f

        val meshG = ObjLoader.loadMesh("model/sphere.obj")
        meshG.material = Material(Color.green)
        val gameItemG = GameItem().apply { mesh =  meshG }
        gameItemG.position.set(2f, 1f, 0f)
        gameItemG.scale = 0.5f

        val meshB = ObjLoader.loadMesh("model/sphere.obj")
        meshB.material = Material(Color.blue)
        val gameItemB = GameItem().apply { mesh =  meshB }
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
            window.isKeyPressed(GLFW_KEY_O) -> pointLight.intensity -= 0.01f
            window.isKeyPressed(GLFW_KEY_P) -> pointLight.intensity += 0.01f
        }

        when {
            window.isKeyPressed(GLFW_KEY_L) -> lightAngle += 0.5f
            window.isKeyPressed(GLFW_KEY_K) -> lightAngle -= 0.5f
        }

        pointLight.intensity = pointLight.intensity.coerceAtLeast(0f)
        pointLight.intensity = pointLight.intensity.coerceAtMost(1f)

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.position.zero()
            camera.rotation.zero()
        }
    }

    override fun update(delta: Float, mouseInput: MouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP)

        pointLight.position.add(lightInc.mul(CAMERA_POS_STEP))

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed) {
            val relativeDiff = mouseInput.relativeVec
            // X mouse movement must rotate Y axis, Y mouse movement must rotate X axis
            camera.moveRotation(relativeDiff.y * MOUSE_SENSITIVITY, relativeDiff.x * MOUSE_SENSITIVITY, 0f)
        }

//        gameItems.first { it.mesh.vertexCount > 300000 }.rotation.add(0f, 1f, 0f)
        gameItems.last().position.set(pointLight.position)

        updateDirectionalLight()

        (hud.gameItems.first() as TextItem).updateText("angle $lightAngle°")
    }

    private fun updateDirectionalLight() {
        when (lightAngle) {
            in 90f..360f -> {
                directionalLight.intensity = 0f
                if (lightAngle >= 270)
                    lightAngle = -90f
            }
            !in -80f..80f -> {
                val factor = 1f - (abs(lightAngle) - 80f) / 10f
                directionalLight.intensity = factor.coerceAtLeast(0f)
                directionalLight.color.y = factor.coerceAtLeast(0.9f)
                directionalLight.color.z = factor.coerceAtLeast(0.5f)
            }
            else -> {
                directionalLight.intensity = 1f
                directionalLight.color.set(1f)
            }
        }
        Log.debug("angle: $lightAngle, factor: ${directionalLight.intensity}, dir: ${directionalLight.direction}, color: ${directionalLight.color}")
        directionalLight.direction.x = sin(lightAngle.toRadians())
        directionalLight.direction.y = cos(lightAngle.toRadians())
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        renderer?.render(window, camera, gameItems, ambientLight, pointLight, directionalLight, hud)
    }

    override fun cleanup() {
        renderer?.cleanup()
        gameItems.forEach { item ->
            item.mesh.cleanUp()
        }
        hud.cleanup()
    }

    companion object {
        const val CAMERA_POS_STEP = 0.1f
        const val MOUSE_SENSITIVITY = 0.4f
    }
}