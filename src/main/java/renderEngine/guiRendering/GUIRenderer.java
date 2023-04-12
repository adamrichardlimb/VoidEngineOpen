package renderEngine.guiRendering;

import components.graphicalComponents.RenderableComponent;
import renderEngine.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import renderEngine.Renderer;
import renderEngine.Shader;
import renderEngine.guiRendering.shaders.GUIFragmentShader;
import renderEngine.guiRendering.shaders.GUIVertexShader;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class GUIRenderer extends Renderer {

    public GUIRenderer(Loader loader, String guiVertexShaderPath, String guiFragmentShaderPath) throws IOException {
        super(loader, guiVertexShaderPath, guiFragmentShaderPath);
    }

    public GUIRenderer(Loader loader, GUIVertexShader guiVertexShader, GUIFragmentShader guiFragmentShader) {
        super(loader, guiVertexShader, guiFragmentShader);
    }

    public void render() {
        //NOTE - GUI Rendering occurs after WorldRendering - so don't clear the buffer.
        glUseProgram(shaderProgram.getID());

        for(Container gui : loader.guis) {

            //Detect any mouse presses on our GUI (done by GLFW Callbacks)

            //Bind to the VAO.
            GL30.glBindVertexArray(gui.vaoID);
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
            loadVector3f(0, gui.screenPos);
            loadVector3f(1, gui.screenSize);

            //Bind to the indices buffer.
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, gui.vboID);


            //Draw vertices through indices with DrawElements.
            GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, gui.getIndices().length, GL11.GL_UNSIGNED_BYTE, 0);

            //Now unbind and disable everything for that element.
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            GL30.glBindVertexArray(0);
        }

        glUseProgram(0);

    }
}
