package game.littletown

import engine.Camera
import org.joml.Intersectionf
import org.joml.Vector2f
import org.joml.Vector3f


object CameraBoxSelectionDetector {

    private val max: Vector3f = Vector3f()
    private val min: Vector3f = Vector3f()
    private val nearFar: Vector2f = Vector2f()

    private var dir: Vector3f = Vector3f()

    fun selectGameItem(tiles: List<Tile>, camera: Camera) {
        var selectedTile: Tile? = null
        var closestDistance = Float.POSITIVE_INFINITY
        dir = camera.viewMatrix.positiveZ(dir).negate()
        for (tile in tiles) {
            tile.selected = false
            val item = tile.gameItem
            min.set(item.position)
            min.add(-item.scale, -item.scale, -item.scale)
            max.set(item.position)
            max.add(item.scale, item.scale, item.scale)
            if (Intersectionf.intersectRayAab(camera.position, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x
                selectedTile = tile
            }
        }
        selectedTile?.selected = true
    }
}