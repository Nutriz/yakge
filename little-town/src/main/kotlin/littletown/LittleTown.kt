package littletown

import engine.GameItem
import engine.GameLifecycle
import engine.HudNano
import engine.Window
import engine.graphics.Material
import engine.graphics.Texture
import engine.utils.MouseInput
import engine.utils.ObjLoader
import engine.utils.Yakge


class LittleTown : GameLifecycle {

    private var renderer: Renderer? = null
    private var hud: HudNano? = null

    private val myCamera = MyCamera()

    private val gameItems = mutableListOf<GameItem>()

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

        repeat(1) {
            val cubeMesh = ObjLoader.loadMesh("model/cube.obj")
            cubeMesh.material = Material(texture = Texture.loadFromFile("texture/uv.jpg"))
            val cube = GameItem().apply { mesh = cubeMesh }
            cube.scale = 0.5f
            cube.position.set(0f + it, -1f+it, 0f+it)
            gameItems += cube
        }
        tileManger.initialTiles()
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
        Yakge.clearColor()
        renderer?.render(window, myCamera.camera, gameItems, tileManger.tiles)
        hud?.render(window)
    }

    override fun cleanup() {
        renderer?.cleanup()
        hud?.cleanup()
        gameItems.forEach { item ->
            item.mesh.cleanUp()
        }
    }
}