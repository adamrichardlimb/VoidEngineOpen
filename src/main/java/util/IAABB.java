package util;

import components.interactionComponents.CollisionBoxComponent;

/**
 * A basic interface to do some checking of where things collide.
 */
public interface IAABB {

    default boolean pointCheck(float xPos, float yPos, float xWidth, float yHeight, float xPoint, float yPoint) {
        return  xPos < xPoint &&
                xPoint < xPos + xWidth &&
                yPos > yPoint &&
                yPoint > yPos - yHeight;
    }

    default boolean collisionCheck(float xPos1, float yPos1,
                                   float xWidth1, float yHeight1,
                                   float xPos2, float yPos2,
                                   float xWidth2, float yHeight2) {

        return  xPos1 < xPos2 + xWidth2 &&
                xPos1 + xWidth1 > xPos2 &&
                yPos1 > yPos2 - yHeight2 &&
                yPos1 - yHeight1 < yPos2;
    }

}
