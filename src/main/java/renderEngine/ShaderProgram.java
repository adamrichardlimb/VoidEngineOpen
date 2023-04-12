package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import util.StringReader;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

//Initially I wrapped this up with the renderer, but now it ought to be separate for making different shaders.
//TODO - Consider making abstract upon introduction of geometry shaders, particle shaders, etc.
//In the future, I'd like people to write their own shader code and maybe use different shaders on different maps.
public class ShaderProgram implements StringReader {

    //The OpenGL ID of the ShaderProgram.
    private final int shaderProgram;

    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) throws IOException {

        //Upon creation, create our shaders.
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        String fileAsString = readFileAsString(vertexShaderPath);
        System.out.println(fileAsString);

        glShaderSource(vertexShader, readFileAsString(vertexShaderPath));
        glCompileShader(vertexShader);

        //Check for compilation errors.
        int status = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if (status != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(vertexShader));
        }

        //Create fragment shader.
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, readFileAsString(fragmentShaderPath));
        glCompileShader(fragmentShader);

        //Once again, check for errors.
        status = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if (status != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(fragmentShader));
        }

        //Now create a shader program.
        this.shaderProgram = glCreateProgram();

        // Position information will be attribute 0
        GL20.glBindAttribLocation(shaderProgram, 0, "vertexPosition");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(shaderProgram, 1, "vertexColours");
        //Texture information will be attribute 2.
        GL20.glBindAttribLocation(shaderProgram, 2, "textureCoord");

        //Attach and link shaders.
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        //Validate program and delete shaders now they're linked.
        glValidateProgram(shaderProgram);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

    }

    /**
     * From a vertex shader and a fragment shader class, we can create our own shaders.
     *
     *
     * @param ourVertexShader - Our VertexShader object (found in their respective folders.)
     * @param ourFragmentShader - Our FragmentShader object (found in their respective folders.)
     */
    public ShaderProgram(Shader ourVertexShader, Shader ourFragmentShader) {

        //Upon creation, create our shaders.
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShader, ourVertexShader.shaderCode);
        glCompileShader(vertexShader);

        //Check for compilation errors.
        int status = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if (status != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(vertexShader));
        }

        //Create fragment shader.
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, ourFragmentShader.shaderCode);
        glCompileShader(fragmentShader);

        //Once again, check for errors.
        status = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if (status != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(fragmentShader));
        }

        //Now create a shader program.
        this.shaderProgram = glCreateProgram();

        // Position information will be attribute 0
        GL20.glBindAttribLocation(shaderProgram, 0, "vertexPosition");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(shaderProgram, 1, "vertexColours");
        //Texture information will be attribute 2.
        GL20.glBindAttribLocation(shaderProgram, 2, "textureCoord");

        //Attach and link shaders.
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        //Validate program and delete shaders now they're linked.
        glValidateProgram(shaderProgram);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

    }

    public int getID() {

        return shaderProgram;

    }

}
