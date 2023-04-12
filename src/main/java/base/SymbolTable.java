package base;

import entities.Camera;
import input._InputHandler;
import renderEngine.Loader;
import util.MouseHandler;

import java.security.InvalidParameterException;

//NOTE - Singleton class, we only need one of these. Thanks to TutorialsPoint for explaining this concept.
//For example, anything using clicking will need to know the window for the mouse callback.
//Instead of passing around this one variable, we'll make it accessible everywhere.
//Same with the game loop, we need to be able to access the transitToState method in order to enter/exit the game.
//This could be done from a GUI button, but I don't want to be passing the gameLoop to containers.
//Let's just store stuff like that here.
public class SymbolTable {

    public static Loader loader;
    private static SymbolTable instance = null;

    private SymbolTable(long window, GameStateMachine gameLoop) {
        instance = this;
        SymbolTable.window = window;
        gameStateFSM = gameLoop;
    }

    public static SymbolTable getInstance() {
        if(instance == null) {
            throw new IllegalArgumentException("You need to instantiate the base.SymbolTable before accessing it. Use getInstance(long window, GameStateLoop gameLoop)");
        }
        return instance;
    }

    public static SymbolTable getInstance(long window, GameStateMachine gameLoop) {
        if(instance != null) {
            return instance;
        }else{
            return new SymbolTable(window, gameLoop);
        }
    }

    //The window value for the current OpenGL window.
    private static long window;

    public static long getWindow() {
        return window;
    }

    //The width of the window pixels.
    private static int windowWidth;

    public static void setWindowWidth(int windowWidth) {
        SymbolTable.windowWidth = windowWidth;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }


    //The height of the window in pixels;
    private static int windowHeight;

    public static void setWindowHeight(int windowHeight) {
        SymbolTable.windowHeight = windowHeight;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }


    //The main game FSM, which executes all the loops.
    private static GameStateMachine gameStateFSM;

    private static Camera activeCamera;

    public static Camera getActiveCamera() {
        return activeCamera;
    }

    public static void setActiveCamera(Camera activeCamera) {
        if(activeCamera == null) {
            throw new InvalidParameterException("Attempted to set the active camera to null. You likely haven't instantiated your camera. I could probably leave the camera unchanged, but it is the fate of all bad programmers to have crashed games.");
        }else{
            SymbolTable.activeCamera = activeCamera;
        }
    }

    public static MouseHandler mouseClickHandler;

    public static int tilesetResolution;

    public static void setTilesetResolution(int i) {
        tilesetResolution = i;
    }

    public static int tilesetWidth;

    public static void setTilesetWidth(int i) {
        tilesetWidth = i;
    }

    public static int tilesetHeight;

    public static void setTilesetHeight(int i) {
        tilesetHeight = i;
    }

    private static float scale = 0.1f;

    public static float getScale() {
        return scale;
    }

    private static _InputHandler inputHandler;

    public static _InputHandler getInputHandler() {
        return inputHandler;
    }

    public static void setInputHandler(_InputHandler input) {
        inputHandler = input;
    }
}
