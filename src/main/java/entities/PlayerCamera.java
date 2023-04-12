package entities;

import components.interactionComponents.BoxComponent;
import components.interactionComponents.CollisionBoxComponent;
import util.Trig;



/**
 * A class for creating a player camera, the same as the abstract camera, but contains the ability to scroll by
 * following the mouse whilst inside some buffer area.
 */
public class PlayerCamera extends Camera implements Trig {

    //TODO - Unhardcode these, move to player preferences.
    private float buffer = 0.2f;
    //private float scrollSpeed = 0.01f;

    public PlayerCamera(PlayerEntity player) {
        super(player);
        //This component is a square that leaves a sort of 'buffer area' around the window.
        //Whilst the player is inside the box, and our mouse is outside it, we should scroll the camera.
        //Once the player is at the edge of the box, we stop scrolling.
        addComponent(new CollisionBoxComponent(- 1 + buffer, 1 - buffer, 2 - (2*buffer), (2 - (2*buffer))));
    }

    /**
     * A method that moves both the position and the collision box of the camera to the appropriate positions.
     */
    private void updatePosition(float positionX, float positionY) {
        getComponent(BoxComponent.class).setPosition(positionX, positionY);
        getComponent(CollisionBoxComponent.class).setPos(positionX - 1 + buffer, positionY + 1 - buffer);
    }

    /**
     * An unfinished method that makes the camera follow the player, will eventually allow you move the camera with the
     * mouse, within a buffer area.
     */
    @Override
    public void update() {

        float[] playerPos = anchor.getComponent(BoxComponent.class).getPosition();
        updatePosition(playerPos[0], playerPos[1]);

        //TODO - Create camera that is easily moveable.
        //TODO - add else method to auto-centre the camera when the player enables the ability.
        //TODO - Test if when at end of box in x/y, we can still move box in y/x respectively.
        /*
        //Grab our components.
        CollisionBoxComponent ourCollisionBox = getComponent(CollisionBoxComponent.class);
        BoxComponent ourBox = getComponent(BoxComponent.class);

        CollisionBoxComponent playerCollision = anchor.getComponent(CollisionBoxComponent.class);

        boolean isPlayerInsideCameraBox = ourCollisionBox.colliderCheck(playerCollision);
        System.out.println(playerPos[0]);
        System.out.println(playerPos[1]);

        //Grab our camera positions and check if we're in the collision box.
        float mouseX = (float) base.SymbolTable.mouseClickHandler.getCursorPosX();
        float mouseY = (float) base.SymbolTable.mouseClickHandler.getCursorPosY();

        boolean isMouseInCameraBox = ourCollisionBox.pointCheck(mouseX, mouseY);

        if(!isPlayerInsideCameraBox) {
            updatePosition(playerPos[0], playerPos[1]);
        }

        //We want to invert our boolean, as we want to be outside of the camera box, in our buffer area.

        if(isPlayerInsideCameraBox && !isMouseInCameraBox) {
            double angleToCorrect = getScreenAngleFromCentre(mouseX, mouseY);
            float movePositionX = (float) -(scrollSpeed * Math.cos(angleToCorrect));
            float movePositionY = (float) -(scrollSpeed * Math.sin(angleToCorrect));

            updatePosition(movePositionX, movePositionY);
        }*/
    }
}
