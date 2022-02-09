package game.littletown

import engine.GameItem
import engine.GameLifecycle
import engine.HudNano
import engine.Window
import engine.graphics.Material
import engine.graphics.Texture
import engine.utils.MouseInput
import engine.utils.ObjLoader
import org.lwjgl.opengl.GL30.*


class LittleTown : GameLifecycle {

    private var renderer: Renderer? = null
    private var hud: HudNano? = null

    private val myCamera = MyCamera()

    private val gameItems = mutableListOf<GameItem>()

    private val tileManger = TileManager()

    override fun init(window: Window) {
        renderer = Renderer(window)
        hud = HudNano(window)
        window.setBackgroundColor(0.5f, 0.5f, 0.5f)

        val cubeMesh = ObjLoader.loadMesh("model/cube.obj")
        cubeMesh.material = Material(texture = Texture.loadFromFile("texture/grassblock.png"))
        val cube = GameItem().apply { mesh = cubeMesh }
        cube.scale = 0.5f
        cube.position.set(4f, 0.5f, -2f)

        tileManger.initialTiles()

        gameItems += cube
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

//        hud?.addText("cam", "cam: ${myCamera.camera.position.toNormalizedString()}", 200, 0)
//        hud?.addText("rot", "rot: ${myCamera.camera.rotation.toNormalizedString()}", 200, 100)
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

        renderer?.render(window, myCamera.camera, tileManger, gameItems)
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