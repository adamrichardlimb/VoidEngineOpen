package entities;

import components.interactionComponents.BoxComponent;

//TODO - Abstract into different camera types upon creating more cameras.
//NOTE: Consider making camera a component.

/**
 * A basic camera, point it at an entity and it will follow it. Simplicity itself.
 */
public class Camera extends Entity{

    //We will follow this entity.
    protected Entity anchor;

    /**
     * Camera constructor, takes an entity and will, on game update, follow it.
     *
     * @param anchor - The entity to follow or 'anchor' to.
     */
    public Camera(Entity anchor) {
        assert(anchor != null);

        this.anchor = anchor;

        if(!anchor.hasComponent(BoxComponent.class)) {
            throw new IllegalArgumentException("Camera creation failure: Attempt to attach a camera to an entity with no BoxComponent.");
        }

        this.addComponent(new BoxComponent(0f, 0f, 0, 0));

    }

    /**
     * Basic update function to follow whatever it is we're anchored to.
     */
    public void update() {

        //Grab the position of our anchor, set it to our camera position.
        float[] anchorPosition = anchor.getComponent(BoxComponent.class).getPosition();

        //Grab our BoxComponent, set its position to the anchors position.
        getComponent(BoxComponent.class).setPosition(anchorPosition[0], anchorPosition[1]);
    }

}
