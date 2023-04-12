package renderEngine.worldRendering.shaders;

import renderEngine.Shader;

public class WorldVertexShader extends Shader {

    public WorldVertexShader() {

        shaderCode = "#version 130\n" +
                "\n" +
                "in vec3 vertexPosition;\n" +
                "in vec4 vertexColours;\n" +
                "in vec2 textureCoord;\n" +
                "\n" +
                "uniform vec3 cameraPosition;\n" +
                "uniform vec3 worldPosition;\n" +
                "\n" +
                "out vec4 vertexColour;\n" +
                "out vec2 passTextureCoord;\n" +
                "\n" +
                "void main(){\n" +
                "\n" +
                "    gl_Position = vec4(vertexPosition + cameraPosition - worldPosition, 1.0);\n" +
                "    vertexColour = vertexColours;\n" +
                "    passTextureCoord = textureCoord;\n" +
                "\n" +
                "\n" +
                "}\n" +
                "\n";

    }

}
