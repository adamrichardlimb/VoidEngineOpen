package renderEngine;

// NOTE - Consider the differences between a Texture file and a series of coloured vertices.
// One has texture co-ordinates associated with it yes, but so do coloured vertices...

// TODO - Replace individual texture with TextureAtlas, then turn textureAtlas into sprites. Move to renderEngine.
public class Texture {

    //A texture ID from OpenGL.
    private int openGLTexID;

    //The width and height of that texture.
    private int textureWidth;
    private int textureHeight;

    //The resolution of a tile in a tileset.
    private int tileSize;

    public Texture(Loader loader, int openGLTexID, int textureWidth, int textureHeight, int tileSize) {
        this.openGLTexID = openGLTexID;
        this.textureHeight = textureHeight;
        this.textureWidth = textureWidth;
        this.tileSize = tileSize;
    }

    public int getOpenGLTexID() {
        return openGLTexID;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public int getTileSize() {
        return tileSize;
    }
}