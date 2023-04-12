package components.graphicalComponents;

import components.Component;
import renderEngine.IRenderable;
import renderEngine.TexturedVertex;

//TODO - Make loader globally accessible, create Renderable constructors for Tile, Entity and Container.
//TODO - When this is done, add a check for a BoxComponent in the respective constructors.

/**
 * The renderable component, attaching this to a component will create a VAO and VBO for that object.
 * In order for the object to render, it requires a BoxComponent.
 * That isn't to say that everything that you see necessarily has a RenderableComponent attached.
 *
 * For example, tiles (will be) rendered by their instances. For that they use the same VBO and just update the VAO with their position.
 */
public class RenderableComponent extends Component implements IRenderable {

    //The VAO and VBO of the thing to render.
    public int vaoID;
    public int vboID;

    //By default, let's not use a texture. Instead, make an ugly white square on the screen.
    //We use an int because OpenGL doesn't appear to have a load uniform bool method.
    public int useTexture = 0;

    //The four vertices of the thing to render.
    private TexturedVertex[] vertices;


    //This method creates a simple square that fills the screen. It is down to the BoxComponent to specify the
    //actual size and position of the object. If a texture is assigned, we just use the whole texture.
    public RenderableComponent() {

        this.vertices = makeRenderable();

    }

    /**
     * A getter for the TexturedVertex array of the Renderable.
     *
     * @return - An array of TexturedVertices of size 4.
     */
    @Override
    public TexturedVertex[] getVertices() {
        return vertices;
    }
}
