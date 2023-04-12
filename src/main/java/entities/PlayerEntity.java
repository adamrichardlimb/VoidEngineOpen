package entities;

import components.interactionComponents.BoxComponent;
import components.interactionComponents.CollisionBoxComponent;
import renderEngine.Loader;

/**
 * The beginnings of the player entity class, simply extends renderable entity then sticks on a controller and box
 * components. Includes an update method to move the collision with the player.
 */
public class PlayerEntity extends RenderableEntity {

    public PlayerEntity(Loader loader) {
        super();

        CollisionBoxComponent playerCollision = new CollisionBoxComponent(0, 0, 0.5f, 0.5f);
        addComponent(playerCollision);
    }

    public void update() {
        float[] playerBox = getComponent(BoxComponent.class).getPosition();
        getComponent(CollisionBoxComponent.class).setPos(playerBox[0], playerBox[1]);
    }
}
