package util;


//Real basic interface to add a clamping function to Java, which doesn't have one.
//I also wanted to use default values for max and min in a function and discovered Java doesn't support that either.
//So strange.
public interface Clamp {

    double minimumValue = 0f;
    double maximumValue = 1f;

    default double mathClamp(double toClamp, double minValue, double maxValue) {

        return Math.max(minValue, Math.min(maxValue, toClamp));

    }

    default double mathClamp(double toClamp) {

        return Math.max(minimumValue, Math.min(maximumValue, toClamp));

    }

}
