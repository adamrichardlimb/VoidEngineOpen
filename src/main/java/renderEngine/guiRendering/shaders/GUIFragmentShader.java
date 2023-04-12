package renderEngine.guiRendering.shaders;

import renderEngine.Shader;

public class GUIFragmentShader extends Shader {

    public GUIFragmentShader() {

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
                "    if(fragColour.a < 0.5) {\n" +
                "        discard;\n" +
                "    }\n" +
                "    fragColour = vertexColour;\n" +
                "}";

    }

}
