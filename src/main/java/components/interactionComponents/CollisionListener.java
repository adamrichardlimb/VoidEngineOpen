package components.interactionComponents;

/**
 * An interface to listen for, and execute some code upon, two objects colliding with one another.
 */
public interface CollisionListener {

    /**
     * An empty method to be overridden upon implementation. Will be called by a CollisionBox when it has collided
     * with another CollisionBox.
     *
     * @param colliderOne - The CollisionBox that was collided with, and called the listener.
     * @param colliderTwo - The CollisionBox that impacted with the previous CollisionBox.
     */
    void onCollide(CollisionBoxComponent colliderOne, CollisionBoxComponent colliderTwo);

}
