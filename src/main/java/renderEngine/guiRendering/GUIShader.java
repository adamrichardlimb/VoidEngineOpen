package renderEngine.guiRendering;

import renderEngine.ShaderProgram;

import java.io.IOException;

public class GUIShader extends ShaderProgram {

    public GUIShader(String vertexShaderPath, String fragmentShaderPath) throws IOException {
        super(vertexShaderPath, fragmentShaderPath);
    }

}
