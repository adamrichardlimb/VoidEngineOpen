package entities.worldEntities;

import entities.Entity;
import components.interactionComponents.BoxComponent;
import components.graphicalComponents.RenderableComponent;
import base.SymbolTable;
import renderEngine.Loader;
import renderEngine.Texture;
import renderEngine.TexturedVertex;

import static base.SymbolTable.*;

//TODO - Create a spritebatching class

/**
 * A world tile, has no collision but does have the ability to render.
 *
 * In time, the chunk class will be able to create tiles and tell them to attach specific components to themselves.
 * Starting with collision, obviously. When that happens, I suppose this will become an abstract class.
 *
 * Has an optional texture, which will be sent to the shader to be drawn, if specified.
 */
public class Tile extends Entity {

    private Loader loader;

    private int tileID;

    //Optional tile texture.
    private Texture tileTexture = null;

    /**
     * Tile constructor, currently doing more of the processing of vertices than it really should be.
     * Adds itself to the list of tiles in the loader, allowing it to be rendered through the WorldRenderer.
     *
     * @param scale - The scale of the tiles, this is to be moved into the Camera, which is then sent to the Shader.
     * @param loader - The loader, this should move into the renderable component.
     * @param chunkID - The ChunkID, which we use to set our position.
     * @param positionWithinChunk - The position in the chunk, which we use to set our position.
     * @param tileID - Our tileID within our spritesheet, which we can generate our ST co-ordinates from.
     * @param tileSheet - Our tilesheet, where we read our ST co-ordinates.
     */
    public Tile(float scale, Loader loader, int chunkID, int positionWithinChunk, int tileID, Texture tileSheet) {

        super();

        this.loader = loader;

        this.tileTexture = tileSheet;

        this.tileID = tileID;

        setWorldPosFromChunkPosAndID(scale, chunkID, positionWithinChunk);

        setSTFromTileID(this.tileID);

        addComponent(new RenderableComponent());
        addComponent(new BoxComponent());


        loader.createVAOAndVBO(this);
        loader.processTiles(this);
    }

    //TODO - Move scale to 'zoom' in the camera... Or see Chunk for scale-related TODO.
    /**
     * Sets the position of the tile in the world from the ID of the chunk it is in, and the position of the tile
     * within said chunk.
     *
     * @param scale - The scale of the tiles to render. This should be moved out of here soon.
     * @param chunkID - The ID of the chunk the tile is in.
     * @param tilePositionInChunk - The tiles position within that chunk.
     */
    public void setWorldPosFromChunkPosAndID(float scale, int chunkID, int tilePositionInChunk) {

        //Grab the renderable component for the instance.
        RenderableComponent tileRenderableComponent = getComponent(RenderableComponent.class);

        //Grab the position component for the instance.
        BoxComponent tileBoxComponent = getComponent(BoxComponent.class);
        //Create local variable to set our position component to.
        float[] worldPos = {0, 0};

        for(int i = 0; i < 4; i++) {

            float[] xyzw = tileRenderableComponent.getVertices()[i].getXYZW();

            tileRenderableComponent.getVertices()[i].setXYZ(xyzw[0] * scale, xyzw[1] * scale, xyzw[2] * scale);

        }

        //Use tilePositionInChunk to find our location relative to our chunk.
        if(tilePositionInChunk < 8){
            //If in the first 8, we're in the first row so just set y to 0f, x to the id * scale of a tile.
            worldPos[0] = -tilePositionInChunk * scale;
            worldPos[1] = 0f;
        }else{
            //The modulo of the tilePositionInChunk by 8 is the x-coordinate.
            int worldPosInXFactor = tilePositionInChunk % 8;

            //System.out.println(worldPosInXFactor);

            //Simply multiply by the scale, as before.
            worldPos[0] = -worldPosInXFactor * scale;

            //The however many times 8 successfully goes into tilePositionInChunk is the row we're on (for the purposes of multiplication).
            //For instance, 7/8 = 0, 0*scale is zero - which is what we want in y.
            int worldPosInYFactor = tilePositionInChunk / 8;
            worldPos[1] = worldPosInYFactor * scale;
        }

        //Now simply account for our chunks position relative to other chunks.
        worldPos[0] += (chunkID % 8) * scale * 8;
        worldPos[1] += (chunkID / 8) * scale * 8;

        tileBoxComponent.alterPosition(worldPos[0], worldPos[1]);

        //System.out.println((worldPos[0]) + " " + (worldPos[1]));

    }

    /**
     * Simple getter for the tiles texture.
     * @return - The Texture we're using for our tile. Typically a tilesheet.
     */
    public Texture getTexture() {
        return tileTexture;
    }

    /**
     * Sets the Texture for this tile, this is handed on over from the map file.
     *
     * @param newTexture - The texture to assign to the tile.
     */
    public void setTexture(Texture newTexture) {
        this.tileTexture = newTexture;
    }

    /**
     * Gets the ID of the tile, which is its position within a tilesheet.
     *
     * @return - The ID of the tile in the tilesheet.
     */
    public int getTileID() {
        return tileID;
    }


    /**
     * Using the tileID along with information from the base.SymbolTable about the tileset, we can find our ST co-ordinates
     * in that tileset. This should be moved into the loader, or its own class, most likely.
     *
     * @param tileID - The ID in the tilesheet we want to use as a texture for our tile.
     */
    public void setSTFromTileID(int tileID) {

        //Grab the information of the tileset.
        float tilesetWidth = (float) SymbolTable.tilesetWidth;

        float tilesetHeight= (float) SymbolTable.tilesetHeight;

        float tileRes = (float) tilesetResolution;


        float widthRatio = (tileRes / tilesetWidth);
        float heightRatio= (tileRes/ tilesetHeight);

        //Define floats for our S/T coordinates for our vertices.
        float vertexOneS;
        float vertexOneT;

        //If the ID is less than the width in tiles, then you're in the first row.
        //And your ID * tileSize will be your position.
        if(tileID < (tilesetWidth / tileRes)) {
            vertexOneS = tileID * widthRatio;
        }
        else {
            //The remainder is where you are in x.
            int remainder = (int) (tileID % (tilesetWidth / tileRes));
            vertexOneS = (remainder * widthRatio);
        }

        //Apparently -1 is the top of my image, and 0 is the bottom? This appears to be a problem just with tiles since
        //GUIs load textures just fine. DO NOT FORGET THIS EVER. It's 3AM as I'm writing this, I've been coding for
        //since 9. But, with this I now can render entire maps with extreme lag as I load the png for each individual
        //texture lmao.

        //If your ID is less than the width of the tileset divided by the tileres
        //your tile must be at the top row of the tileset.
        if(tileID < (tilesetWidth/tileRes)) {
            vertexOneT = -1;
        }
        else {
            //There are width/res number of tiles in x.
            //So however many times your ID can be divided by that
            //is your y in chunk space.
            int quotient = (int) (tileID / (tilesetWidth / tileRes));
            vertexOneT = (quotient * heightRatio) - 1;
        }

        TexturedVertex[] ourVertices = getComponent(RenderableComponent.class).getVertices();

        //Though, I'm yet to spritebatch a GUI so who knows. Oh yeah, that reminds me:
        //TODO - Move this into a Spritebatching class!
        ourVertices[0].setST(vertexOneS, vertexOneT + heightRatio);
        ourVertices[1].setST(vertexOneS, vertexOneT);
        ourVertices[2].setST(vertexOneS + widthRatio, vertexOneT + heightRatio);
        ourVertices[3].setST(vertexOneS + widthRatio, vertexOneT);
    }


    /**
     * Removes the tile from the loader, preventing it from being rendered.
     *
     * @deprecated
     */
    public void cull() {

        loader.vaos.remove((Integer) getComponent(RenderableComponent.class).vaoID);
        //TODO - Implement instanced rendering and remove self from tiles HashMap in loader.

    }
}
