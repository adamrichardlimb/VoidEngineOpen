package renderEngine.guiRendering.shaders;

import renderEngine.Shader;

public class GUIVertexShader extends Shader {

    public GUIVertexShader() {

        shaderCode = "#version 130\n" +
                "\n" +
                "in vec4 vertexPosition;\n" +
                "in vec4 vertexColours;\n" +
                "in vec2 textureCoord;\n" +
                "\n" +
                "uniform vec3 screenPos;\n" +
                "uniform vec3 screenSize;\n" +
                "\n" +
                "out vec4 vertexColour;\n" +
                "out vec2 passTextureCoord;\n" +
                "\n" +
                "void main(){\n" +
                "\n" +
                "    gl_Position = vec4((vertexPosition.x * screenSize.x) + screenPos.x, (vertexPosition.y * screenSize.y) + screenPos.y, 1.0, 1.0);\n" +
                "    vertexColour = vertexColours;\n" +
                "    passTextureCoord = textureCoord;\n" +
                "\n" +
                "\n" +
                "}";

    }

}
