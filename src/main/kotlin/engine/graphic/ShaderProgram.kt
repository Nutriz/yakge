package engine.graphic

import org.lwjgl.opengl.GL20.*
import kotlin.properties.Delegates

class ShaderProgram(
    private val vertexSource: String = "",
    private val fragmentSource: String = ""
) {

    private var programId by Delegates.notNull<Int>()

    private var vertexId by Delegates.notNull<Int>()
    private var fragmentId by Delegates.notNull<Int>()

    init {
        createProgram()
        createVertexShader()
        createFragmentShader()
        link()
    }

    private fun createProgram() {
        programId = glCreateProgram()
        if (programId == 0) {
            throw Exception("Could not create Shader");
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
            throw Exception("Error creating shader. Type: $shaderType");
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        checkShaderError(shaderId)

        glAttachShader(programId, shaderId)

        return shaderId
    }

    private fun link() {
        glLinkProgram(programId)

        if (vertexId != 0)
            glDetachShader(programId, vertexId)
        if (fragmentId != 0)
            glDetachShader(programId, fragmentId)

        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
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
            throw Exception("Error compiling Shader code: ${glGetShaderInfoLog(shaderId, 1024)}");
        }
    }
}