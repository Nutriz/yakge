package game

import engine.GameItem
import engine.GameLogic
import engine.Window
import engine.graphics.Mesh
import engine.graphics.Texture
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.nuklear.*
import org.lwjgl.nuklear.Nuklear.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack.stackPush


class DummyGame : GameLogic {

    private lateinit var renderer: Renderer
    private lateinit var uiRenderer: UiRenderer

    private lateinit var mesh: Mesh

    private val gameItems = mutableListOf<GameItem>()

    override fun init(window: Window) {
        renderer = Renderer(window)
        uiRenderer = UiRenderer(window.windowHandle)

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
        val gameItem = GameItem(mesh)
        gameItem.position.set(0f, 0f, -2f)

        gameItems += gameItem
    }

    override fun input(window: Window) {

        val gameItem = gameItems.first()

        when {
            window.isKeyPressed(GLFW.GLFW_KEY_X) -> gameItem.scale += 0.01f
            window.isKeyPressed(GLFW.GLFW_KEY_C) -> gameItem.scale -= 0.01f
            else -> gameItem.scale
        }

        when {
            window.isKeyPressed(GLFW.GLFW_KEY_LEFT) -> gameItem.position.x -= 0.01f
            window.isKeyPressed(GLFW.GLFW_KEY_RIGHT) -> gameItem.position.x += 0.01f
            window.isKeyPressed(GLFW.GLFW_KEY_UP) -> gameItem.position.y += 0.01f
            window.isKeyPressed(GLFW.GLFW_KEY_DOWN) -> gameItem.position.y -= 0.01f
        }

        when {
            window.isKeyPressed(GLFW.GLFW_KEY_S) -> {
                var rotation = gameItem.rotation.z + 1.5f
                if (rotation > 360) {
                    rotation = 0f
                }
                gameItem.rotation.set(rotation)
            }
        }
    }

    override fun update(delta: Float) {

    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        renderer.render(window, gameItems)

//        uiRenderer.newFrame()
//        layout(uiRenderer.ctx, 50, 50)
//        uiRenderer.renderFrame()
    }

    fun layout(ctx: NkContext?, x: Int, y: Int) {
        stackPush().use { stack ->

            val compression = BufferUtils.createIntBuffer(1).put(0, 20);
            val background = NkColorf.create()
                    .r(0.10f)
                    .g(0.18f)
                    .b(0.24f)
                    .a(1.0f)
            val rect = NkRect.mallocStack(stack)
            if (nk_begin(
                            ctx,
                            "Demo",
                            nk_rect(x.toFloat(), y.toFloat(), 230f, 250f, rect),
                            NK_WINDOW_BORDER or NK_WINDOW_MOVABLE or NK_WINDOW_SCALABLE or NK_WINDOW_MINIMIZABLE or NK_WINDOW_TITLE
                    )) {
                nk_layout_row_static(ctx, 30f, 80, 1)
                if (nk_button_label(ctx, "button")) {
                    println("button pressed")
                }
                nk_layout_row_dynamic(ctx, 30f, 2)
                nk_layout_row_dynamic(ctx, 25f, 1)
                nk_property_int(ctx, "Compression:", 0, compression, 100, 10, 1f)
                nk_layout_row_dynamic(ctx, 20f, 1)
                nk_label(ctx, "background:", NK_TEXT_LEFT)
                nk_layout_row_dynamic(ctx, 25f, 1)
                if (nk_combo_begin_color(ctx, nk_rgb_cf(background, NkColor.mallocStack(stack)), NkVec2.mallocStack(stack).set(nk_widget_width(ctx), 400f))) {
                    nk_layout_row_dynamic(ctx, 120f, 1)
                    nk_color_picker(ctx, background, NK_RGBA)
                    nk_layout_row_dynamic(ctx, 25f, 1)
                    background
                            .r(nk_propertyf(ctx, "#R:", 0f, background.r(), 1.0f, 0.01f, 0.005f))
                            .g(nk_propertyf(ctx, "#G:", 0f, background.g(), 1.0f, 0.01f, 0.005f))
                            .b(nk_propertyf(ctx, "#B:", 0f, background.b(), 1.0f, 0.01f, 0.005f))
                            .a(nk_propertyf(ctx, "#A:", 0f, background.a(), 1.0f, 0.01f, 0.005f))
                    nk_combo_end(ctx)
                }
            }
            nk_end(ctx)
        }
    }

    override fun cleanup() {
        renderer.cleanup()
        mesh.cleanup()
        uiRenderer.shutdown()
    }
}