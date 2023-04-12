package util;

public interface Trig {

    /**
     * This method gets you the angle from the centre of the screen (0, 0) and actively accounts for the OpenGL
     * co-ordinate system.
     *
     * @param xPos
     * @param yPos
     * @return
     */
    default double getScreenAngleFromCentre(float xPos, float yPos) {
        //First, determine what quadrant of the screen we're in.
        //If we're in a negative quadrant, we need to account for that by adding on the value for that quadrant,
        //and ensuring the xPos and yPos are positive when we use arctan.
        double corrector = 0;

        if(xPos == 0 && yPos > 0) {
            //We're not in any quadrant, just going straight up.
            return 0f;
        }else if(xPos == 0 && yPos < 0) {
            //We're not in any quadrant, just going straight down.
            return Math.PI;
        }else if(xPos > 0 && yPos > 0){
            //We're in the top-right quadrant, so we don't need to add any correcting value.
            corrector = 0;
        }else if(xPos > 0 && yPos < 0){
            //We're in the bottom-right quadrant, so add 90 degrees (pi/2 radians)
            corrector = Math.PI/2f;
            yPos *= -1;
        }else if(xPos < 0 && yPos < 0) {
            //We're in the bottom-left quadrant, so add 180 degrees. (pi radians)
            corrector = Math.PI;
            xPos *= -1;
            yPos *= -1;
        } else if(xPos < 0 && yPos > 0){
            //We're in the top-left quadrant, so add 270 degrees (3pi/2 radians)
            corrector = (3 * Math.PI)/2f;
            xPos *= -1;
        }
        double divisor = yPos/xPos;
        return Math.atan(divisor) + corrector;
    }

}
