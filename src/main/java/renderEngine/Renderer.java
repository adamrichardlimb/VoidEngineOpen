package renderEngine;

import components.interactionComponents.BoxComponent;
import components.graphicalComponents.RenderableComponent;
import entities.worldEntities.Tile;
import base.SymbolTable;
import org.lwjgl.opengl.*;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;


public abstract class Renderer {

    protected Loader loader;
    protected ShaderProgram shaderProgram;

    //For whatever renderer we have, use the respective shaders.
    public Renderer(Loader loader, String vertexShaderPath, String fragmentShaderPath) throws IOException {
        this.loader = loader;
        this.shaderProgram = new ShaderProgram(vertexShaderPath, fragmentShaderPath);
    }

    public Renderer(Loader loader, Shader vertexShader, Shader fragmentShader) {
        this.loader = loader;
        this.shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
    }

    //NOTE - The actual rendering methods are in the respective renderers.

    //I'll want these for all kinds of renderers.
    //TODO - Refactor this for all renderables. Consider a factory class?
    protected void prepareTexture(Tile tile) {
        GL30.glBindVertexArray(tile.getComponent(RenderableComponent.class).vaoID);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    protected void prepareInstance(Tile tileToRender) {
        //Grab the active camera from the symbol table, load position from the position component to the vertex shader.
        loadVector2fAs3f(0, SymbolTable.getActiveCamera().getComponent(BoxComponent.class).getPosition());
        //Grab the position from the position component of the tile, load to the vertex shader.
        loadVector2fAs3f(1, tileToRender.getComponent(BoxComponent.class).getPosition());
        //Bind to the indices buffer.
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, tileToRender.getComponent(RenderableComponent.class).vboID);
    }

    protected void unbindTexture(Texture textureToUnbind) {
        //Now unbind and disable everything for that element.
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    protected void loadVector3f(int location, float[] vector) {
        GL20.glUniform3f(location, vector[0], vector[1], vector[2]);
    }

    protected void loadVector2fAs3f(int location, float[] vector) {
        if(vector.length != 2) {
            throw new IllegalArgumentException("Attempted to use the loadVector2fas3f method on a vector of length that is not two.");
        }
        GL20.glUniform3f(location, vector[0], vector[1], 0f);

    }

    protected void loadVector2f(int location, float[] vector) {
        GL20.glUniform2f(location, vector[0], vector[1]);
    }

    //This is used to load bools too. Just a heads-up.
    protected void loadInt(int location, int intToLoad) {
        GL20.glUniform1i(location, intToLoad);
    }

    //To be called on exit.
    public void destroyOpenGL() {
        loader.destroyOpenGL();
        //Kill the shader program, the linked shaders are killed upon linking.
        glDeleteProgram(shaderProgram.getID());
    }
}
