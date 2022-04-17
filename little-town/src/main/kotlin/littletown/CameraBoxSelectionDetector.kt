package littletown

import engine.Camera
import engine.GameItem
import org.joml.Intersectionf
import org.joml.Vector2f
import org.joml.Vector3f

open class CameraBoxSelectionDetector {
    private val max: Vector3f = Vector3f()
    private val min: Vector3f = Vector3f()
    private val nearFar: Vector2f = Vector2f()
    private val dir: Vector3f = Vector3f()

    open fun selectGameItem(gameItems: MutableList<GameItem>, camera: Camera) {
        dir.set(camera.viewMatrix.positiveZ(dir).negate())
        selectGameItem(gameItems, camera.position, dir)
    }

    protected fun selectGameItem(gameItems: MutableList<GameItem>, center: Vector3f, dir: Vector3f) {
        var selectedGameItem: GameItem? = null
        var closestDistance = Float.POSITIVE_INFINITY
        for (gameItem in gameItems) {
            gameItem.selected = false
            min.set(gameItem.position)
            max.set(gameItem.position)
            min.add(-gameItem.scale, -gameItem.scale, -gameItem.scale)
            max.add(gameItem.scale, gameItem.scale, gameItem.scale)
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x
                selectedGameItem = gameItem
            }
        }
        selectedGameItem?.selected = true
    }
}