package renderEngine.worldRendering;

import entities.Camera;
import components.graphicalComponents.RenderableComponent;
import entities.worldEntities.Tile;
import base.SymbolTable;
import org.lwjgl.opengl.GL11;
import renderEngine.Loader;
import renderEngine.Renderer;
import renderEngine.Texture;
import renderEngine.worldRendering.shaders.WorldFragmentShader;
import renderEngine.worldRendering.shaders.WorldVertexShader;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL20.glUseProgram;

//We can then implement multiple tilesets by having the max tileID as a property of the tileset and unloading/loading
//the texture for that tileset when we move from one tileset to another.
public class WorldRenderer extends Renderer {

    //Basically, we get a list of entities using a texture, then for those entities do some rendering with the
    //appropriate transformations. This is my first time properly implementing a HashMap.
    //Although, I'm following ThinMatrix's Java3D game library tutorial for this.

    //On map creation, the maps tileset is created and stored as a key to the HashMap.
    //When a tile is assigned that texture, it goes into the bucket for that texture.
    //NOTE: Consider using a WeakHashMap to cull unused textures. Be warned that this may cause issues when we get to chunkloading.
    public WorldRenderer(Loader loader, String vertexShaderPath, String fragmentShaderPath) throws IOException {
        super(loader, vertexShaderPath, fragmentShaderPath);
    }

    public WorldRenderer(Loader loader, WorldVertexShader worldVertexShader, WorldFragmentShader worldFragmentShader) {
        super(loader, worldVertexShader, worldFragmentShader);
    }

    //This method allows us to bind and unbind objects with identical textures in one go rather than do it individually.
    //Thanks HashMap! Very cool!
    // Right now we're binding for every tile, even though the texture is the same.
    public void newRender() {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        glUseProgram(shaderProgram.getID());

        for (Texture tile : loader.tiles.keySet()) {
            //Bind to our texture.
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tile.getOpenGLTexID());

            //Grab all tiles associated with that texture.
            List<Tile> batch = loader.tiles.get(tile);
            if (batch != null) {
                for (Tile tileToRender : batch) {
                    //Prepare my texture.
                    prepareTexture(tileToRender);
                    //Prepare information specific to this instance.
                    prepareInstance(tileToRender);

                    //And then draw my objects. Batch rendering should be good for this. Maybe check performance between
                    //batch rendering and instanced rendering?

                    //Grab the renderable component of a tile, grab the number of the indices we have and render from that.
                    GL11.glDrawElements(GL11.GL_TRIANGLES, tileToRender.getComponent(RenderableComponent.class).getIndices().length, GL11.GL_UNSIGNED_BYTE, 0);
                }
            } else {
                throw new NullPointerException("Tried to render a null batch in Spritebatch. This may be due to no tiles being associated with a Texture.");
            }
            //Unbind from this texture and be done with it.
            unbindTexture(tile);
        }

        glUseProgram(0);
    }
}
