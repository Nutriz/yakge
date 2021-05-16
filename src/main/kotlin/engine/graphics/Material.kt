package engine.graphics

import engine.utils.getRandomColor
import org.joml.Vector4f

data class Material(
    val ambient: Vector4f = getRandomColor(),
//    val diffuse: Vector4f = ambient,
//    val specular: Vector4f = getRandomColor(),
//    var reflectance: Float = 1f,
    var unshaded: Boolean = false,
    val texture: Texture? = null
) {
    val hasTexture = texture != null
}