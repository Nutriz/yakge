package game.littletown

import engine.GameItem
import engine.GameLifecycle
import engine.HudNano
import engine.Window
import engine.graphics.Material
import engine.graphics.Texture
import engine.utils.MouseInput
import engine.utils.ObjLoader
import game.littletown.util.GridSelectionDetector
import org.joml.Vector3f
import org.lwjgl.opengl.GL30.*


object GameSettings {
    var GridSize = 60
}

class LittleTown : GameLifecycle {

    lateinit var window: Window
    private val myCamera = MyCamera()
    private var renderer: Renderer? = null
    private var hud: HudNano? = null

    private val scene = Scene()
    private lateinit var gridSelection: GameItem

    private val tileManger = TileManager()
    private val gridSelector = GridSelectionDetector()

    lateinit var window: Window

    val cameraSelector = CameraBoxSelectionDetector()
    val mouseSelector = MouseBoxSelectionDetector()
    val gridSelector = GridSelectionDetector()

    override fun init(window: Window) {
        this.window = window
        renderer = Renderer(window)
        hud = HudNano(window)
        window.setBackgroundColor(0.5f, 0.5f, 0.5f)

        repeat(1) {
            val cubeMesh = ObjLoader.loadMesh("model/cube.obj")
            cubeMesh.material = Material(texture = Texture.loadFromFile("texture/grassblock.png"))
            val cube = GameItem().apply { mesh = cubeMesh }
            cube.scale = 0.5f
            cube.position.set(0f + it, -1f+it, 0f+it)
            gameItems += cube
        }
        tileManger.initialTiles()
        scene.tiles = tileManger.tiles
    }

    private fun initObjects() {
        // grid selector
        val planeMesh = ObjLoader.loadMesh("model/tiles/plane.obj")
        planeMesh.material = Material()
        gridSelection = GameItem().apply { mesh = planeMesh }
        scene.gameItems += gridSelection

        // axis
        scene.lineItems += LineItem(Vector3f(), Vector3f(1f, 0f,0f), color = Color.red)
        scene.lineItems += LineItem(Vector3f(), Vector3f(0f, 1f, 0f), color = Color.green)
        scene.lineItems += LineItem(Vector3f(), Vector3f(0f, 0f, 1f), color = Color.blue)

        // grid
        val size = GameSettings.GridSize
        for (x in -size..size) {
            val start = Vector3f(-size.toFloat(), 0f, x.toFloat())
            val end = Vector3f(size.toFloat(), 0f, x.toFloat())
            scene.lineItems += LineItem(start, end, color = Color.black)
        }
        for (y in -size..size) {
            val start = Vector3f(y.toFloat(), 0f, -size.toFloat())
            val end = Vector3f(y.toFloat(), 0f, size.toFloat())
            scene.lineItems += LineItem(start, end, color = Color.black)
        }
    }

    override fun input(window: Window, mouseInput: MouseInput) {
        myCamera.cameraInput(window)
    }

    override fun update(delta: Float, mouseInput: MouseInput) {
        // Update camera position
        myCamera.move()
        if (mouseInput.isRightButtonPressed) {
            myCamera.rotate(mouseInput)
        }

        myCamera.camera.updateViewMatrix()


//        cameraSelector.selectGameItem(gameItems, myCamera.camera)
//        cameraSelector.selectGameItem(tileManger.tiles, myCamera.camera)
        mouseSelector.selectGameItem(tileManger.tiles, window, mouseInput.currentPos, myCamera.camera)

        val (x, y) = gridSelector.mouseToWorld(window, mouseInput.currentPos, myCamera.camera)

        gameItems.first().position.x = x.toFloat()
        gameItems.first().position.z = y.toFloat()

        hud?.addText("mouse", "mouse: ${mouseInput.currentPos.x}, ${mouseInput.currentPos.y}", 100, 100)
        hud?.addText("grid", "grid: ${x}, ${y}", 100, 120)

//        hud?.addText("cam", "cam: ${myCamera.camera.position.toNormalizedString()}", 200, 0)
//        hud?.addText("rot", "rot: ${myCamera.camera.rotation.toNormalizedString()}", 200, 100)
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

        renderer?.render(window, myCamera.camera, scene)
        hud?.render(window)
    }

    override fun cleanup() {
        renderer?.cleanup()
        scene.cleanUp()
        hud?.cleanup()
    }
}