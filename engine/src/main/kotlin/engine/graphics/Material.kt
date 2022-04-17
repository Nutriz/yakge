package engine.graphics

import engine.utils.Color
import engine.utils.getRandomColor
import org.joml.Vector4f

data class Material(
    val ambient: Vector4f = getRandomColor(),
    var diffuse: Vector4f = ambient,
    val specular: Vector4f = Color.white,
    var reflectance: Float = 1f,
    var unshaded: Boolean = false,
    val texture: Texture? = null
) {
    val hasTexture = texture != null
}