package renderEngine;

/**
 * This interface holds the functionality of renderables, it creates the squares you draw to the screen.
 * Do not use this to make entities renderable - use the renderable component for that.
 *
 * If you use this interface on something, it should inherit from the RenderableBase or otherwise include the commented
 * out variables below.
 *
 */
public interface IRenderable {

    //The VAO and VBO of the thing to render.
    //int vaoID;
    //int vboID;

    //By default, let's not use a texture. Instead, make an ugly white square on the screen.
    //We use an int because OpenGL doesn't appear to have a load uniform bool method.
    //int useTexture = 0;

    //The four vertices of the thing to render.
    //TexturedVertex[] vertices;



    //How to connect those four vertices.
    byte[] indices = {

            0, 1, 2,
            2, 1, 3

    };


    //This method creates a simple square that fills the screen.
    default TexturedVertex[] makeRenderable() {

        float[] sts = {

                0f, 0f,
                0f, 1f,
                1f, 0f,
                1f, 1f

        };

        TexturedVertex v0 = new TexturedVertex();
        v0.setXYZ(0f, 0f, 0f);
        v0.setST(sts[0], sts[1]);

        TexturedVertex v1 = new TexturedVertex();
        v1.setXYZ(0f, 1f, 0f);
        v1.setST(sts[2], sts[3]);

        TexturedVertex v2 = new TexturedVertex();
        v2.setXYZ(1f, 0f, 0f);
        v2.setST(sts[4], sts[5]);

        TexturedVertex v3 = new TexturedVertex();
        v3.setXYZ(1f, 1f, 0f);
        v3.setST(sts[6], sts[7]);

        return new TexturedVertex[]{v0, v1, v2, v3};

    }

    /**
     * A getter for the TexturedVertex array of the Renderable.
     *
     * @return - An array of TexturedVertices of size 4.
     */
    TexturedVertex[] getVertices();

    /**
     * A getter for the indices of the Renderable.
     *
     * @return - An array of the indices of size 6.
     */
    default byte[] getIndices() {
        return indices;
    }

    /**
     * This method will take floats as percentages of the screen (measured from the top left) and convert them to
     * the OpenGL co-ordinate system. It is primarily used by GUI elements.
     *
     *
     * @param proportionX - The percentage in x of the screen/container.
     * @param proportionY - The percentage in y of the screen/container.
     * @return - An array of floats of size two, with the co-ordinates converted.
     */
    //And Judge Claude Frollo gave the function a cruel name, a name that means "unreasonable":
    default float[] convertMyCoordSystemToOpenGLCoordSystem(float proportionX, float proportionY) {

        return new float[]{ (proportionX * 2) - 1, (proportionY * - 2) + 1,};

    }
}
