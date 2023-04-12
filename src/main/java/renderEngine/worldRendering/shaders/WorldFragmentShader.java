package renderEngine.worldRendering.shaders;

import renderEngine.Shader;

public class WorldFragmentShader extends Shader {

    public WorldFragmentShader() {

        shaderCode = "#version 130\n" +
                "\n" +
                "uniform sampler2D texture_diffuse;\n" +
                "\n" +
                "in vec4 vertexColour;\n" +
                "in vec2 passTextureCoord;\n" +
                "\n" +
                "out vec4 fragColour;\n" +
                "\n" +
                "void main(){\n" +
                "\n" +
                "    fragColour = vertexColour;\n" +
                "    fragColour = texture(texture_diffuse, passTextureCoord);\n" +
                "\n" +
                "}";

    }

}
