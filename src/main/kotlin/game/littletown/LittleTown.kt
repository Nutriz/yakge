package game.littletown

import engine.GameItem
import engine.GameLifecycle
import engine.HudNano
import engine.Window
import engine.graphics.Material
import engine.graphics.Texture
import engine.utils.Color
import engine.utils.MouseInput
import engine.utils.ObjLoader
import org.joml.Vector4f
import org.lwjgl.opengl.GL30.*


class LittleTown : GameLifecycle {

    private var renderer: Renderer? = null
    private var hud: HudNano? = null

    private val myCamera = MyCamera()

    private val gameItems = mutableListOf<GameItem>()

    override fun init(window: Window) {
        renderer = Renderer(window)
        hud = HudNano(window)
        window.setBackgroundColor(0.2f, 0.2f, 0.8f)

        val cubeMesh = ObjLoader.loadMesh("model/cube.obj")
        cubeMesh.material = Material(ambient = Color.white, texture = Texture.loadFromFile("texture/grassblock.png"))
        val cube = GameItem().apply { mesh = cubeMesh }
        cube.scale = 0.5f
        cube.position.set(4f, 0.5f, -2f)

        val monkeyMesh = ObjLoader.loadMesh("model/monkey.obj")
        monkeyMesh.material = Material(Vector4f(1f, 1f, 1f, 1f))
        val monkey = GameItem().apply { mesh = monkeyMesh }
        monkey.position.set(4f, 0f, -5f)
        monkey.rotation.set(0f, 0f, 0f)

        gameItems += cube
        gameItems += monkey
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

        hud?.addText("delta", "delta: $delta", 100, 0)
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

        renderer?.render(window, myCamera.camera, gameItems)
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