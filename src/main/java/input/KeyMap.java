package input;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

//TODO - Create user preferences, upon key remapping, update those preferences.

/**
 * A class to take in GLFW integer values upon key callback and associate them with an enum value corresponding to a key
 * We can also remap these keys to allow people to specify their own controller layouts. Understand that a controller
 * layout and a controller scheme are two different things. You're not changing the functionality of the controller,
 * merely what you do to execute that functionality.
 *
 * @param <T> = The key to remap to.
 * @see _InputHandler
 * @see _ControllerScheme
 */
public class KeyMap<T extends Enum<T>> extends HashMap<Integer, T> {

    /**
     * GLFW uses ints to signify buttons, we need to cast them to Integers to shove them in a hashmap.
     * Then we grab the currently assigned value, and overwrite it.
     *
     * @param keyToRemap - The button we intend to remap.
     * @param remapValue - The button we will be remapping to.
     */
    public void remapKey(int keyToRemap, T remapValue) {

        Integer toRemap = (Integer) keyToRemap;
        T toReplace = this.get(toRemap);
        this.replace(toRemap, toReplace, remapValue);

    }

    /**
     * The default constructor merely associates the integer buttons in GLFW with their values in the Keys enum.
     * TODO - A secondary constructor, which takes in overwritten values and remaps appropriately.
     */
    public KeyMap() {
        this.put(GLFW_KEY_0, (T) KeyNum.KEY_0);// You can't cast an int to an integer, nor can you use a primitive type as a key to a HashMap.
    // GLFW keyboard presses are ints, so we could either make them objects and then use them as keys, or just create
    // An array where the index of the array points to an enum value corresponding to the key they want.
    // A better implementation may come in time, but this is it for now.
    //NOTE - Once implemented move this to the controllers themselves, since right now it's remapping ALL controllers.

        this.put(GLFW_KEY_1, (T) KeyNum.KEY_1);
        this.put(GLFW_KEY_2, (T) KeyNum.KEY_2);
        this.put(GLFW_KEY_3, (T) KeyNum.KEY_3);
        this.put(GLFW_KEY_4, (T) KeyNum.KEY_4);
        this.put(GLFW_KEY_5, (T) KeyNum.KEY_5);
        this.put(GLFW_KEY_6, (T) KeyNum.KEY_6);
        this.put(GLFW_KEY_7, (T) KeyNum.KEY_7);
        this.put(GLFW_KEY_8, (T) KeyNum.KEY_8);
        this.put(GLFW_KEY_9, (T) KeyNum.KEY_9);

        this.put(GLFW_KEY_A, (T) KeyNum.KEY_A);
        this.put(GLFW_KEY_B, (T) KeyNum.KEY_B);
        this.put(GLFW_KEY_C, (T) KeyNum.KEY_C);
        this.put(GLFW_KEY_D, (T) KeyNum.KEY_D);
        this.put(GLFW_KEY_E, (T) KeyNum.KEY_E);
        this.put(GLFW_KEY_F, (T) KeyNum.KEY_F);
        this.put(GLFW_KEY_G, (T) KeyNum.KEY_G);
        this.put(GLFW_KEY_H, (T) KeyNum.KEY_H);
        this.put(GLFW_KEY_I, (T) KeyNum.KEY_I);
        this.put(GLFW_KEY_J, (T) KeyNum.KEY_J);
        this.put(GLFW_KEY_K, (T) KeyNum.KEY_K);
        this.put(GLFW_KEY_L, (T) KeyNum.KEY_L);
        this.put(GLFW_KEY_M, (T) KeyNum.KEY_M);
        this.put(GLFW_KEY_N, (T) KeyNum.KEY_N);
        this.put(GLFW_KEY_O, (T) KeyNum.KEY_O);
        this.put(GLFW_KEY_P, (T) KeyNum.KEY_P);
        this.put(GLFW_KEY_Q, (T) KeyNum.KEY_Q);
        this.put(GLFW_KEY_R, (T) KeyNum.KEY_R);
        this.put(GLFW_KEY_S, (T) KeyNum.KEY_S);
        this.put(GLFW_KEY_T, (T) KeyNum.KEY_T);
        this.put(GLFW_KEY_U, (T) KeyNum.KEY_U);
        this.put(GLFW_KEY_V, (T) KeyNum.KEY_V);
        this.put(GLFW_KEY_W, (T) KeyNum.KEY_W);
        this.put(GLFW_KEY_X, (T) KeyNum.KEY_X);
        this.put(GLFW_KEY_Y, (T) KeyNum.KEY_Y);
        this.put(GLFW_KEY_Z, (T) KeyNum.KEY_Z);
    }

}
