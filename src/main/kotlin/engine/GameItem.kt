package engine

import com.curiouscreature.kotlin.math.Float3
import engine.graphics.Mesh

data class GameItem(
        val mesh: Mesh,
        val position: Float3 = Float3(),
        val scale: Float = 1f,
        val rotation: Float3 = Float3()
)