package engine

import engine.graphics.FontTexture
import engine.graphics.Material
import engine.graphics.Mesh
import org.joml.Vector3f
import org.joml.Vector4f

open class GameItem(
    val position: Vector3f = Vector3f(),
    var scale: Float = 1f,
    val rotation: Vector3f = Vector3f(),
    var tint: Vector4f = Vector4f(1f, 1f, 1f, 1f),
) {
    constructor() : this(Vector3f(0f, 0f, 0f))
    constructor(x: Float = 0f, y: Float = 0f, z: Float = 0f) : this(Vector3f(x, y, z))
    constructor(x: Int = 0, y: Int = 0, z: Int = 0) : this(Vector3f(x.toFloat(), y.toFloat(), z.toFloat()))

    lateinit var mesh: Mesh
}

data class TextItem(
    var text: String,
    val fontTexture: FontTexture,
) : GameItem(Vector3f()) {

    init {
        mesh = buildMesh()
    }

    private fun buildMesh(): Mesh {

        val positions = mutableListOf<Float>()
        val texCoords = mutableListOf<Float>()
        val indices = mutableListOf<Int>()

        var startx = 0f
        text.forEachIndexed { i, char ->

            val charInfo = fontTexture.chars[char]!!

            // top left
            positions += startx // x
            positions += 0.0f // y
            positions += zPos // z
            texCoords += charInfo.startX.toFloat() / fontTexture.width.toFloat()
            texCoords += 0f
            indices += i * verticesPerQuad

            // bottom left
            positions += startx // x
            positions += fontTexture.height.toFloat() // y
            positions += zPos // z
            texCoords += charInfo.startX.toFloat() / fontTexture.width.toFloat()
            texCoords += 1f
            indices += i * verticesPerQuad + 1

            // bottom right
            positions += startx + charInfo.charWidth // x
            positions += fontTexture.height.toFloat() // y
            positions += zPos // z
            texCoords += (charInfo.startX.toFloat() + charInfo.charWidth.toFloat()) / fontTexture.width.toFloat()
            texCoords += 1f
            indices += i * verticesPerQuad + 2

            // top right
            positions += startx + charInfo.charWidth // x
            positions += 0f // y
            positions += zPos // z
            texCoords += (charInfo.startX.toFloat() + charInfo.charWidth.toFloat()) / fontTexture.width.toFloat()
            texCoords += 0f
            indices += i * verticesPerQuad + 3

            // Add indices for left top and bottom right vertices
            indices += i * verticesPerQuad
            indices += i * verticesPerQuad + 2

            startx += charInfo.charWidth
        }

        return Mesh(positions.toFloatArray(), texCoords.toFloatArray(), listOf(0f).toFloatArray(), indices.toIntArray(), Material(texture = fontTexture.texture))
    }

    fun updateText(text: String) {
        this.text = text
        mesh.deleteBuffers()
        val textColor = mesh.material.diffuse
        mesh = buildMesh()
        mesh.material.diffuse = textColor
    }

    companion object {
        const val zPos = 0.0f
        const val verticesPerQuad = 4
    }
}

