package renderEngine.entityRendering.shaders;

import renderEngine.Shader;

public class EntityFragmentShader extends Shader {

    public EntityFragmentShader() {

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
                "    fragColour = texture(texture_diffuse, passTextureCoord);\n" +
                "    fragColour = vertexColour;\n" +
                "\n" +
                "}";

    }

}
