package game

import engine.GameItem
import engine.GameLogic
import engine.Window
import engine.graphics.Mesh
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

    private lateinit var gameItem: GameItem

    override fun init(window: Window) {
        renderer = Renderer(window)
        uiRenderer = UiRenderer(window.windowHandle)

        val positions = floatArrayOf(
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
        )
        val colours = floatArrayOf(
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        )
        val indices = intArrayOf(0, 1, 3, 3, 1, 2)
        mesh = Mesh(positions, colours, indices)
        gameItem = GameItem(mesh)
        gameItem.position.set(0f, 0f, -2f)
    }

    override fun input(window: Window) {

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
                gameItem.rotation.z = rotation
            }
        }
    }

    override fun update(delta: Float) {

    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        renderer.render(window, listOf(gameItem))

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