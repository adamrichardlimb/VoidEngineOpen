package input;

import base.SymbolTable;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;


/**
 * This class defines and handles all inputs the player specifies
 *
 * It invokes the same method on each controller scheme.
 * WARNING - This is /not/ the place to define your controller functionality.
 * Please use a ControllerScheme to handle all of that.
 * */
public class _InputHandler<T extends Enum<T>> {

    //Some controller to be plugged in/out depending on the controller state machine.
    private _ControllerScheme activeController;

    private Map<T, Boolean> callbackDictionary;

    private Map<T, Runnable> onExecute;

    public _InputHandler() {
        createKeyResponses();
        createDefaultKeymapping();
        createKeyCallbacks();
    }

    //NOTE: These are just callbacks, so they're only called on key presses/events.
    //This is good for mouse inputs, but bad for keyboard inputs. Hence why we use HashMaps to match keys to bools.
    //So this should just handle the setting of bools, which are then used to update the relevant objects on update.

    /**
     * This method creates the key callbacks, which works as follows:
     *
     * On key press, we execute this method, we take our input key and remap it to whatever the active controller has
     * the key remapped to. Next, we use that remappedKey to set the value of a bool in a HashMap to true on press,
     * and false when the key is released.
     *
     * The updateInput method, called every frame, is then responsible for executing the methods associated with those
     * keys.
     */
    public void createKeyCallbacks() {

        glfwSetKeyCallback(SymbolTable.getWindow(), new GLFWKeyCallback() {

            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {

                Integer newKey = key;

                T remappedKey = (T) activeController.controllerRemapping.get(newKey);

                switch (action) {
                    case GLFW_PRESS:
                        callbackDictionary.replace(remappedKey, true);
                        break;

                    case GLFW_RELEASE:
                        callbackDictionary.replace(remappedKey, false);
                        break;

                    default:
                        break;
                }

            }

        });

        glfwSetMouseButtonCallback(SymbolTable.getWindow(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {

                switch (action) {
                    case GLFW_PRESS: {
                        if(button == 0) {
                            activeController.onMouseLeftPress();
                        }
                        if(button == 1) {
                            activeController.onMouseRightPress();
                        }
                        break;

                    }
                    case GLFW_RELEASE: {

                        if(button == 0) {
                            activeController.onMouseLeftRelease();
                        }

                        if(button == 1) {
                            activeController.onMouseRightRelease();
                        }
                        break;

                    }


                }
            }

        });

    }

    /**
     *A method for setting the active controller. If you fail to create a controllerScheme we throw an error.
     *This may not always be the case however, since I would like to add pre and post conditions for state changes.
     *If that occurs, I'll be throwing my errors there since controller creation is definitely a post condition for a
     *state change.
     *
     * @param newController - The controller scheme to move to.
     */
    public void setActiveController(_ControllerScheme newController) {

        if(newController == null) {
            throw new NullPointerException("Attempt to assign a null controller in _InputHandler. Likely because you failed to instantiate a controller.");
        }else {
            this.activeController = newController;
        }

    }

    /**
     * Creates a dictionary of keys and sets them all to false.
     * On key callback, we will set these booleans as true if pressed, false when released.
     *
     */
    public void createDefaultKeymapping() {

        Map<T, Boolean> defaultKeymap = new HashMap<>();

        for(KeyNum key: KeyNum.values()) {

            defaultKeymap.put((T) key, false);

        }

        this.callbackDictionary = defaultKeymap;

    }

    /**
     * This method executes upon instantiation, and merely populates the onExecute hashmap with the appropriate
     * methods for the controllers. All controllers have these methods (which are empty by default) so we won't
     * get an error.
     */
    private void createKeyResponses() {
        onExecute = new HashMap<>();
        onExecute.put((T) KeyNum.KEY_A, () -> activeController.onKeyAPress());
        onExecute.put((T) KeyNum.KEY_B, () -> activeController.onKeyBPress());
        onExecute.put((T) KeyNum.KEY_C, () -> activeController.onKeyCPress());
        onExecute.put((T) KeyNum.KEY_D, () -> activeController.onKeyDPress());
        onExecute.put((T) KeyNum.KEY_E, () -> activeController.onKeyEPress());
        onExecute.put((T) KeyNum.KEY_F, () -> activeController.onKeyFPress());
        onExecute.put((T) KeyNum.KEY_G, () -> activeController.onKeyGPress());
        onExecute.put((T) KeyNum.KEY_H, () -> activeController.onKeyHPress());
        onExecute.put((T) KeyNum.KEY_I, () -> activeController.onKeyIPress());
        onExecute.put((T) KeyNum.KEY_J, () -> activeController.onKeyJPress());
        onExecute.put((T) KeyNum.KEY_K, () -> activeController.onKeyKPress());
        onExecute.put((T) KeyNum.KEY_L, () -> activeController.onKeyLPress());
        onExecute.put((T) KeyNum.KEY_M, () -> activeController.onKeyMPress());
        onExecute.put((T) KeyNum.KEY_N, () -> activeController.onKeyNPress());
        onExecute.put((T) KeyNum.KEY_O, () -> activeController.onKeyOPress());
        onExecute.put((T) KeyNum.KEY_P, () -> activeController.onKeyPPress());
        onExecute.put((T) KeyNum.KEY_Q, () -> activeController.onKeyQPress());
        onExecute.put((T) KeyNum.KEY_R, () -> activeController.onKeyRPress());
        onExecute.put((T) KeyNum.KEY_S, () -> activeController.onKeySPress());
        onExecute.put((T) KeyNum.KEY_T, () -> activeController.onKeyTPress());
        onExecute.put((T) KeyNum.KEY_U, () -> activeController.onKeyUPress());
        onExecute.put((T) KeyNum.KEY_V, () -> activeController.onKeyVPress());
        onExecute.put((T) KeyNum.KEY_W, () -> activeController.onKeyWPress());
        onExecute.put((T) KeyNum.KEY_X, () -> activeController.onKeyXPress());
        onExecute.put((T) KeyNum.KEY_Y, () -> activeController.onKeyYPress());
        onExecute.put((T) KeyNum.KEY_Z, () -> activeController.onKeyZPress());

        onExecute.put((T) KeyNum.KEY_0, () -> activeController.onKey0Press());
        onExecute.put((T) KeyNum.KEY_1, () -> activeController.onKey1Press());
        onExecute.put((T) KeyNum.KEY_2, () -> activeController.onKey2Press());
        onExecute.put((T) KeyNum.KEY_3, () -> activeController.onKey3Press());
        onExecute.put((T) KeyNum.KEY_4, () -> activeController.onKey4Press());
        onExecute.put((T) KeyNum.KEY_5, () -> activeController.onKey5Press());
        onExecute.put((T) KeyNum.KEY_6, () -> activeController.onKey6Press());
        onExecute.put((T) KeyNum.KEY_7, () -> activeController.onKey7Press());
        onExecute.put((T) KeyNum.KEY_8, () -> activeController.onKey8Press());
        onExecute.put((T) KeyNum.KEY_9, () -> activeController.onKey9Press());
    }

    /**
     * This method is called every frame, and it goes through the list of keys in the key enum, and gets the bool from
     * the callback dictionary, if it's true, it gets the associated method in the onExecute map and executes it.
     *
     * Note that we aren't remapping keys here, key remapping is processed on input.
     */
    public void updateInput() {

        for(KeyNum T : KeyNum.values()) {
            if(callbackDictionary.get(T)) {
                onExecute.get(T).run();
            }
        }

    }
}
