package components.interactionComponents;

import components.Component;

/**
 * This component is responsible for both screen position and world position.
 * The GUI/World renderers handle the fact that they're holding 2D co-ordinates when OpenGL wants 3D co-ordinates.
 */
public class BoxComponent extends Component {

    //This isn't really private at all, but I think it's good practice to force people to only interface with things
    //through methods provided.
    private float[] position = {0f,0f};
    private float[] size     = {0f,0f};

    /**
     * Default constructor, merely sets position and size to 0,0.
     */
    public BoxComponent() { }

    /**
     * Secondary constructor for creating a world position component.
     * @param startX - The position for the object to start from in X.
     * @param startY - The position for the object to start from in Y.
     * @param startWidth - The size for the object to start from in X.
     * @param startHeight - The size for the object to start from in Y.
     */
    public BoxComponent(float startX, float startY, float startWidth, float startHeight) {
        setPosition(startX, startY);
        setSize(startWidth, startHeight);
    }

    /**
     * A method to simply set the position of the entity using an array of floats.
     * @param startX - The position to set the WorldPosition in x to.
     * @param startY - The position to set the WorldPosition in y to.
     */
    public void setPosition(float startX, float startY) {
        position[0] = startX;
        position[1] = startY;
    }

    /**
     * A method to simply set the size of the entity using an array of floats.
     * @param width - The size to set the WorldPosition in x to.
     * @param height- The size to set the WorldPosition in y to.
     */
    public void setSize(float width, float height) {
        size[0] = width;
        size[1] = height;
    }

    /**
     * A method that allows for adding on values to the position.
     *
     * @param positionX - The amount in x to add to the position.
     * @param positionY - The amount in y to add to the position.
     */
    public void alterPosition(float positionX, float positionY) {
        position[0] += positionX;
        position[1] += positionY;
    }

    /**
     * A method that allows for adding on values to the position.
     *
     * @param alterX - The amount in x to add to the size.
     * @param alterY - The amount in y to add to the size.
     */
    public void alterSize(float alterX, float alterY) {
        size[0] += alterX;
        size[1] += alterY;
    }

    /**
     * Simple getter for the BoxComponent position.
     *
     * @return - Float array of size two with the positions.
     */
    public float[] getPosition() { return position; }

    /**
     * Simple getter for the BoxComponent size.
     *
     * @return - Float array of size two with the sizes.
     */
    public float[] getSize() { return size; }

}
