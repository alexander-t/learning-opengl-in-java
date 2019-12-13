package se.tarlinder.opengl;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20C.*;

public class Shader {
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public Shader(String filename) {
        programId = glCreateProgram();

        vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderId, ResourceUtils.readFileAsString("/shaders/" + filename + ".vs"));
        glCompileShader(vertexShaderId);
        if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) != 1) {
            throw new ShaderException(glGetShaderInfoLog(vertexShaderId));
        }

        fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderId, ResourceUtils.readFileAsString("/shaders/" + filename + ".fs"));
        glCompileShader(fragmentShaderId);
        if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) != 1) {
            throw new ShaderException(glGetShaderInfoLog(fragmentShaderId));
        }

        // Mandatory: there must be a vertex shader
        glAttachShader(programId, vertexShaderId);
        // Mandatory: there must be a fragment shader to produce a color
        glAttachShader(programId, fragmentShaderId);

        glBindAttribLocation(programId, 0, "vertices");
        glBindAttribLocation(programId, 1, "textures");

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) != 1) {
            throw new ShaderException(glGetProgramInfoLog(programId));
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) != 1) {
            throw new ShaderException(glGetProgramInfoLog(programId));
        }
    }

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(programId, name);
        if (location != -1) {
            glUniform1i(location, value);
        }
    }

    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(programId, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16); // 4x4
        value.get(buffer);
        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void bind() {
        glUseProgram(programId);
    }
}
