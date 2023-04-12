package components.interactionComponents;

import components.Component;
import util.IAABB;

import java.util.List;

/**
 * A collision box class. Has AABB functionality for both checking one box against another and for checking if a point
 * is inside the container. The collision box can be specified manually, through writing out floats, and alternatively,
 * through using a BoxComponent.
 *
 * Using a BoxComponent allows collision boxes to dynamically change as the objects position and size changes.
 */
public class CollisionBoxComponent extends Component implements IAABB {

    public float positionX;
    public float positionY;
    public float width;
    public float height;

    private BoxComponent ourBox;

    private List<CollisionListener> collisionListeners;


    /**
     * CollisionBox constructor - sets the position and size.
     *
     * @param positionX - Position in x to begin at.
     * @param positionY - Position in y to begin at.
     * @param width - Width in x.
     * @param height - Height in y.
     */
    public CollisionBoxComponent(float positionX, float positionY, float width, float height) {
        this.positionX  = positionX;
        this.positionY  = positionY;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the position of the CollisionBoxComponent.
     *
     * @param positionX - Position in x to set to.
     * @param positionY - Position in y to set to.
     */
    public void setPos(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setSize(float sizeX, float sizeY) {
        this.width = sizeX;
        this.height= sizeY;
    }


    /**
     * A method to notify listeners that a CollisionBox has collided with it.
     *
     * @param colliderOther - The CollisionBox that has collided with this object.
     */
    public void onCollide(CollisionBoxComponent colliderOther) {
        assert(colliderOther != null);
        if(!(collisionListeners == null || collisionListeners.size() == 0)) {
            for(CollisionListener listener : collisionListeners) {
                listener.onCollide(this, colliderOther);
            }
        }
    }

    /**
     * A check to see if a specific collisionBox has collided with this object.
     *
     * @param colliderOther - The CollisionBox we want to see if we've collided with.
     * @return - True, if we have collided, false otherwise.
     */
    public boolean colliderCheck(CollisionBoxComponent colliderOther) {

        assert(colliderOther != null);

        return collisionCheck( this.positionX, this.positionY,
                        this.width, this.height,
                        colliderOther.positionX, colliderOther.positionY,
                        colliderOther.width, colliderOther.height);

    }

    /**
     * A check to see if a point is inside our CollisionBox.
     *
     * @param x - The co-ordinate in x to check is inside our CollisionBox.
     * @param y - The co-ordinate in y to check is inside our CollisionBox.
     * @return - True, if the specified point is inside our CollisionBox, false otherwise.
     */
    public boolean checkPoint(float x, float y) {

        return  pointCheck(this.positionX, this.positionY, this.width, this.height, x, y);

    }
}
