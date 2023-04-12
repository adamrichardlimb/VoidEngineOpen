package renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import entities.Entity;
import components.graphicalComponents.RenderableComponent;
import entities.RenderableEntity;
import entities.worldEntities.Tile;
import base.SymbolTable;
import renderEngine.guiRendering.Container;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import util.RenderableBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This class will handle all loading and unloading of tiles and entities.
//Whatever list it generates here will be passed to the renderer.

//NOTE - Should these lists be here, really?
//The objects add themselves to their respective lists, so these lists can be anywhere.
//Maybe these lists should belong to their respective renderers?
//In which case, this class should be moved to the render engine...
public class Loader {

    //Make ArrayList for VAOs and VBOs.
    public List<Integer> vaos, vbos, textures;

    //Although Tile is technically a renderable entity by composition, the Entity Renderer is called after
    //the world renderer. And if we leave this as the base class entity, Tiles will be able to be put in this list.
    public List<RenderableEntity> entities;
    public List<Container> guis;

    public Map<Texture, List<Tile>> tiles = new HashMap<>();

    public Loader() {

        vaos = new ArrayList<>();
        vbos = new ArrayList<>();
        textures = new ArrayList<>();
        guis = new ArrayList<>();
        entities = new ArrayList<>();

    }

    //This method will take in the currently available tiles and organise them in our Hashmap.
    public void processTiles(Tile tileToProcess) {

        Texture tileTexture = tileToProcess.getTexture();

        //This will grab all tiles with that texture, which should be basically all the tiles since a tileset can be huge.
        List<Tile> batch = tiles.get(tileTexture);

        //If the batch isn't empty, go ahead and just add the tile to the list of tiles.
        if(batch != null) {
            batch.add(tileToProcess);
        }else {

            //If the batch is empty, we haven't added to it yet. So create it.
            List<Tile> newBatch = new ArrayList<>();
            newBatch.add(tileToProcess);
            tiles.put(tileTexture, newBatch);
        }

    }

    //TODO - Instanced rendering for particles.
    //I'm uncertain as to which order to add these things in, but Hashmapping is going to decrease the number of VAOs
    //needed to be created.
    //NOTE - texture atlasing, instanced rendering and spritebatching involve other files, but this to do is here because they're all related to optimisation
    public void createVAOAndVBO(@NotNull RenderableBase toRender) {

        TexturedVertex[] vertices = toRender.getVertices();
        byte[] indices = toRender.getIndices();

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * TexturedVertex.elementCount);
        for (TexturedVertex texturedVertex : vertices) {
            // Add position, color and texture floats to the buffer
            verticesBuffer.put(texturedVertex.getElements());
        }
        verticesBuffer.flip();

        // OpenGL expects to draw vertices in counter clockwise order by default
        int indicesCount = indices.length;
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        // Create a new Vertex Array Object in memory and select it (bind)
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        // Put the position coordinates in attribute list 0
        GL20.glVertexAttribPointer(0, TexturedVertex.positionElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.positionByteOffset);
        // Put the color components in attribute list 1
        GL20.glVertexAttribPointer(1, TexturedVertex.colorElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.colorByteOffset);
        // Put the texture coordinates in attribute list 2
        GL20.glVertexAttribPointer(2, TexturedVertex.textureElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.textureByteOffset);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

        vaos.add(vaoId);

        // Create a new VBO for the indices and select it (bind) - INDICES
        int vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        vbos.add(vboId);

        toRender.vaoID = vaoId;
        toRender.vboID = vboiId;

    }

    //TODO - Instanced rendering for particles.
    //I'm uncertain as to which order to add these things in, but Hashmapping is going to decrease the number of VAOs
    //needed to be created.
    //NOTE - texture atlasing, instanced rendering and spritebatching involve other files, but this to do is here because they're all related to optimisation
    public void createVAOAndVBO(@NotNull Entity toRender) {

        if(!toRender.hasComponent(RenderableComponent.class)) {
            throw new IllegalArgumentException("Attempt to render entity: " + toRender.toString() + " with no Renderable component!");
        }

        RenderableComponent ourRenderableComponent = toRender.getComponent(RenderableComponent.class);

        TexturedVertex[] vertices = ourRenderableComponent.getVertices();
        byte[] indices = ourRenderableComponent.getIndices();

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * TexturedVertex.elementCount);
        for (TexturedVertex texturedVertex : vertices) {
            // Add position, color and texture floats to the buffer
            verticesBuffer.put(texturedVertex.getElements());
        }
        verticesBuffer.flip();

        // OpenGL expects to draw vertices in counter clockwise order by default
        int indicesCount = indices.length;
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        // Create a new Vertex Array Object in memory and select it (bind)
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

        // Put the position coordinates in attribute list 0
        GL20.glVertexAttribPointer(0, TexturedVertex.positionElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.positionByteOffset);
        // Put the color components in attribute list 1
        GL20.glVertexAttribPointer(1, TexturedVertex.colorElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.colorByteOffset);
        // Put the texture coordinates in attribute list 2
        GL20.glVertexAttribPointer(2, TexturedVertex.textureElementCount, GL11.GL_FLOAT,
                false, TexturedVertex.stride, TexturedVertex.textureByteOffset);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);

        vaos.add(vaoId);

        // Create a new VBO for the indices and select it (bind) - INDICES
        int vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        vbos.add(vboId);

        ourRenderableComponent.vaoID = vaoId;
        ourRenderableComponent.vboID = vboiId;

    }

    public int loadPNGTexture(String filepath) {

        ByteBuffer buffer = null;
        int tWidth = 0;
        int tHeight = 0;

        try {
            //Open our PNG.
            InputStream in = new FileInputStream(filepath);
            PNGDecoder decoder = new PNGDecoder(in);

            tWidth = decoder.getWidth();
            tHeight = decoder.getHeight();

            SymbolTable.setTilesetWidth(tWidth);
            SymbolTable.setTilesetHeight(tHeight);

            buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();

            in.close();
        }catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        // Create a new texture object in memory and bind it
        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_LINEAR);

        return texId;

    }

    //TODO - Look into this destroyOpenGL() method. Refactor somewhere more intuitive.
    //Currently, the GameState calls the renderer, which calls the loader, and that feels... wrong.
    //Maybe I should just have the GameState call both the renderer and loader since they are both in the GameState class.
    public void destroyOpenGL() {
        //Disable VBO index in VAO.
        GL20.glDisableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        for(int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }

        //Unbind and delete any VAOs.
        GL30.glBindVertexArray(0);

        for(int vao: vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for(int texture:textures) {
            GL11.glDeleteTextures(texture);
        }
    }

}