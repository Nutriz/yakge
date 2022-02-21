package game.littletown

import engine.*
import engine.graphics.Material
import engine.utils.Color
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

    override fun init(window: Window) {
        this.window = window
        renderer = Renderer(window)
        hud = HudNano(window)
        window.setBackgroundColor(0.5f, 0.5f, 0.5f)

        initObjects()

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

        // TODO manage to don't add twice very quickly
        val (x, z) = gridSelector.mouseToWorldPosition(window, mouseInput.currentPos, myCamera.camera)
        gridSelection.position.x = x.toFloat()
        gridSelection.position.z = z.toFloat()
        if (mouseInput.isLeftButtonPressed) {
            tileManger.addTile(x, z)
        }
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