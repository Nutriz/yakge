package game.littletown

import engine.*
import engine.graphics.Material
import engine.utils.Color
import engine.utils.MouseInput
import engine.utils.ObjLoader
import game.littletown.util.CameraBoxSelectionDetector
import game.littletown.util.GridSelectionDetector
import game.littletown.util.MouseBoxSelectionDetector
import org.joml.Vector3f
import org.lwjgl.opengl.GL30.*


object GameSettings {
    var GridSize = 60
}

class LittleTown : GameLifecycle {

    private var renderer: Renderer? = null
    private var hud: HudNano? = null

    private val myCamera = MyCamera()

    private val gameItems = mutableListOf<GameItem>()
    private val lineItems = mutableListOf<LineItem>()

    private val tileManger = TileManager()

    lateinit var window: Window

    val cameraSelector = CameraBoxSelectionDetector()
    val mouseSelector = MouseBoxSelectionDetector()
    val gridSelector = GridSelectionDetector()

    override fun init(window: Window) {
        this.window = window
        renderer = Renderer(window)
        hud = HudNano(window)
        window.setBackgroundColor(0.5f, 0.5f, 0.5f)

        initObjects()

        tileManger.initialTiles()
    }

    private fun initObjects() {
        // Cube selector
        val planeMesh = ObjLoader.loadMesh("model/tiles/plane.obj")
        planeMesh.material = Material()
        val cube = GameItem().apply { mesh = planeMesh }
        gameItems += cube

        // axis
        lineItems += LineItem(Vector3f(), Vector3f(1f, 0f,0f), color = Color.red)
        lineItems += LineItem(Vector3f(), Vector3f(0f, 1f, 0f), color = Color.green)
        lineItems += LineItem(Vector3f(), Vector3f(0f, 0f, 1f), color = Color.blue)

        // grid
        val size = GameSettings.GridSize
        for (x in -size..size) {
            val start = Vector3f(-size.toFloat(), 0f, x.toFloat())
            val end = Vector3f(size.toFloat(), 0f, x.toFloat())

            lineItems += LineItem(start, end, color = Color.black)
        }
        for (y in -size..size) {
            val start = Vector3f(y.toFloat(), 0f, -size.toFloat())
            val end = Vector3f(y.toFloat(), 0f, size.toFloat())

            lineItems += LineItem(start, end, color = Color.black)
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

        val (x, z) = gridSelector.mouseToWorldPosition(window, mouseInput.currentPos, myCamera.camera)
        gameItems.first().position.x = x.toFloat()
        gameItems.first().position.z = z.toFloat()
        if (mouseInput.isLeftButtonPressed) {
            tileManger.addTile(x, z)
        }
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

        renderer?.render(window, myCamera.camera, gameItems, tileManger.tiles, lineItems)
        hud?.render(window)
    }

    override fun cleanup() {
        renderer?.cleanup()
        hud?.cleanup()
        gameItems.forEach { item ->
            item.mesh.cleanUp()
        }
        lineItems.forEach { line ->
            line.mesh.cleanUp()
        }
    }
}