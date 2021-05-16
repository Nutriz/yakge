package engine.graphics

import engine.utils.Log
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack
import kotlin.properties.Delegates

class ShaderProgram(
    private val vertexSource: String = "",
    private val fragmentSource: String = ""
) {

    private var programId by Delegates.notNull<Int>()

    private var vertexId by Delegates.notNull<Int>()
    private var fragmentId by Delegates.notNull<Int>()

    // TODO refactor to an Uniform data class
    private val uniforms = mutableMapOf<String, Int>()

    init {
        createProgram()
        createVertexShader()
        createFragmentShader()
        link()
    }

    private fun createProgram() {
        programId = glCreateProgram()
        if (programId == 0) {
            throw Exception("Could not create Shader")
        }
    }

    private fun createVertexShader() {
        vertexId = createShader(vertexSource, GL_VERTEX_SHADER)
    }

    private fun createFragmentShader() {
        fragmentId = createShader(fragmentSource, GL_FRAGMENT_SHADER)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw Exception("Error creating shader. Type: $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        checkShaderError(shaderId)

        glAttachShader(programId, shaderId)

        return shaderId
    }

    fun createUniform(uniformName: String) {
        Log.info("createUniform: Creating uniform: $uniformName")
        val uniformLocation = glGetUniformLocation(programId, uniformName)
        if (uniformLocation < 0) {
            Log.error("createUniform: Can't create uniform '$uniformName', not found/used in shader code")
        }
        uniforms[uniformName] = uniformLocation
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        MemoryStack.stackPush().use { stack ->
            uniforms[uniformName]?.let { uniformLocation ->
                glUniformMatrix4fv(uniformLocation, false, value.get(stack.mallocFloat(16)))
            }
        }
    }

    fun setUniform(uniformName: String, value: Boolean) {
        uniforms[uniformName]?.let { uniformLocation ->
            val intBoolean = if (value) 1 else 0
            glUniform1i(uniformLocation, intBoolean)
        }
    }

    fun setUniform(uniformName: String, value: Int) {
            uniforms[uniformName]?.let { uniformLocation ->
                glUniform1i(uniformLocation, value)
        }
    }

    fun setUniform(uniformName: String, value: Float) {
            uniforms[uniformName]?.let { uniformLocation ->
                glUniform1f(uniformLocation, value)
        }
    }

    fun setUniform(uniformName: String, value: Vector3f) {
        uniforms[uniformName]?.let { uniformLocation ->
            glUniform3f(uniformLocation, value.x, value.y, value.z)
        }
    }

    fun setUniform(uniformName: String, value: Vector4f) {
        uniforms[uniformName]?.let { uniformLocation ->
            glUniform4f(uniformLocation, value.x, value.y, value.z, value.w)
        }
    }

    fun setUniform(uniformName: String, pointLight: PointLight) {
        setUniform("$uniformName.color", pointLight.color)
        setUniform("$uniformName.position", pointLight.position)
        setUniform("$uniformName.intensity", pointLight.intensity)
    }

    fun setUniform(uniformName: String, material: Material) {
        setUniform("$uniformName.ambient", material.ambient)
        setUniform("$uniformName.hasTexture", material.hasTexture)
        setUniform("$uniformName.unshaded", material.unshaded)
    }
    private fun link() {
        glLinkProgram(programId)

        if (vertexId != 0)
            glDetachShader(programId, vertexId)
        if (fragmentId != 0)
            glDetachShader(programId, fragmentId)

        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            Log.error("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024))
        }
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }

    private fun checkShaderError(shaderId: Int) {
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw Exception("Error compiling Shader code: ${glGetShaderInfoLog(shaderId, 1024)}")
        }
    }
}