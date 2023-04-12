package renderEngine.entityRendering;

import components.graphicalComponents.RenderableComponent;
import components.interactionComponents.BoxComponent;
import entities.Entity;
import base.SymbolTable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import renderEngine.Loader;
import renderEngine.Renderer;
import renderEngine.entityRendering.shaders.EntityFragmentShader;
import renderEngine.entityRendering.shaders.EntityVertexShader;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class EntityRenderer extends Renderer {

    public EntityRenderer(Loader loader, String vertexShaderPath, String fragmentShaderPath) throws IOException {
        super(loader, vertexShaderPath, fragmentShaderPath);
    }

    public EntityRenderer(Loader loader, EntityVertexShader vertexShader, EntityFragmentShader fragmentShader) {
        super(loader, vertexShader, fragmentShader);
    }

    public void render() {
        glUseProgram(shaderProgram.getID());

        for(Entity entity : loader.entities) {

            //Detect any mouse presses on our GUI (done by GLFW Callbacks)

            //Grab the entities BoxComponent.
            BoxComponent entityBox = entity.getComponent(BoxComponent.class);

            BoxComponent cameraBox = SymbolTable.getActiveCamera().getComponent(BoxComponent.class);

            //Bind to the VAO.
            GL30.glBindVertexArray(entity.getComponent(RenderableComponent.class).vaoID);
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
            loadVector2fAs3f(0, cameraBox.getPosition());
            loadVector2fAs3f(1, entityBox.getPosition());
            loadVector2fAs3f(2, entityBox.getSize());

            //Bind to the indices buffer.
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, entity.getComponent(RenderableComponent.class).vboID);


            //Draw vertices through indices with DrawElements.
            GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, entity.getComponent(RenderableComponent.class).getIndices().length, GL11.GL_UNSIGNED_BYTE, 0);

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
