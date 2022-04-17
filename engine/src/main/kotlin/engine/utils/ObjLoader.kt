package engine.utils

import engine.graphics.Mesh
import org.joml.Vector2f
import org.joml.Vector3f
import java.io.File
import kotlin.time.ExperimentalTime


data class Face(
    val indexGroups: List<IndexesGroup>,
)

data class IndexesGroup(val idxPos: Int = NO_VALUE, val idxTextCoords: Int = NO_VALUE, val idxVecNormal: Int = NO_VALUE) {
    companion object {
        const val NO_VALUE = -1
    }
}

@OptIn(ExperimentalTime::class)
object ObjLoader {
    fun loadMesh(fileName: String): Mesh {
        return measureAndLog("$fileName loaded in") {
            val positionsData = mutableListOf<Vector3f>()
            val texCoordsData = mutableListOf<Vector2f>()
            val normalsData = mutableListOf<Vector3f>()
            val facesData = mutableListOf<Face>()

            File(fileName).forEachLine { line ->
                val tokens = line.split(" ")
                when (tokens.first()) {
                    "v" -> positionsData += Vector3f(tokens[1].toFloat(), tokens[2].toFloat(), tokens[3].toFloat())
                    "vt" -> texCoordsData += Vector2f(tokens[1].toFloat(), tokens[2].toFloat())
                    "vn" -> normalsData += Vector3f(tokens[1].toFloat(), tokens[2].toFloat(), tokens[3].toFloat())
                    "f" -> {
                        val indexGroups = mutableListOf<IndexesGroup>()
                        tokens.drop(1).forEach { groupString ->
                            val group = groupString.split("/").map { it.toInt() - 1 }
                            indexGroups += IndexesGroup(group[0], group[1], group[2])
                        }
                        facesData += Face(indexGroups)
                    }
                }
            }
            val usePosForIdx = positionsData.size >= texCoordsData.size
            reorderLists(positionsData, texCoordsData, normalsData, facesData, usePosForIdx)
        }
    }

    private fun reorderLists(
        positionsData: MutableList<Vector3f>,
        texCoordsData: MutableList<Vector2f>,
        normalsData: MutableList<Vector3f>,
        facesData: MutableList<Face>,
        usePosForIdx: Boolean = true
    ): Mesh {
        Log.info("Mesh reordering with positions: $usePosForIdx")
        val arraySize = if (usePosForIdx) positionsData.size else texCoordsData.size
        val positions = FloatArray(arraySize * 3)
        val texCoords = FloatArray(arraySize * 2)
        val normals = FloatArray(arraySize * 3)
        val indices = mutableListOf<Int>()

        facesData.forEach { face ->
            face.indexGroups.forEach { groupId ->
                val index = if (usePosForIdx) groupId.idxPos else groupId.idxTextCoords

                val position = positionsData[groupId.idxPos]
                positions[index * 3] = position.x
                positions[index * 3 + 1] = position.y
                positions[index * 3 + 2] = position.z

                val coords = texCoordsData[groupId.idxTextCoords]
                texCoords[index * 2] = coords.x
                texCoords[index * 2 + 1] = 1 - coords.y

                val normal = normalsData[groupId.idxVecNormal]
                normals[index * 3] = normal.x
                normals[index * 3 + 1] = normal.y
                normals[index * 3 + 2] = normal.z

                indices += index
            }
        }
        return Mesh(positions, texCoords, normals, indices.toIntArray())
    }
}