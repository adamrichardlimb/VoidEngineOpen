package util;

import renderEngine.IRenderable;
import renderEngine.TexturedVertex;

/**
 * A class to make things renderable without making them entities.
 * This is mostly used for GUIs, but could theoretically be used for backgrounds or game layers (i.e. rain, snow, etc.)
 */
public class RenderableBase implements IRenderable {

    public int vaoID;
    public int vboID;

    public int useTexture = 0;

    protected TexturedVertex[] vertices;

    public RenderableBase() {
        vertices = makeRenderable();
    }

    @Override
    public TexturedVertex[] getVertices() {
        return vertices;
    }

}
