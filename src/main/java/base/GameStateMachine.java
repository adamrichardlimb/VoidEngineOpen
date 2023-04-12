package base;

import gameStates.GameStateEnum;
import gameStates.GameStateManager;
import gameStates._GameState;
import input.KeyNum;
import renderEngine.entityRendering.EntityRenderer;
import renderEngine.entityRendering.shaders.EntityFragmentShader;
import renderEngine.entityRendering.shaders.EntityVertexShader;
import renderEngine.guiRendering.LayoutManager;
import renderEngine.guiRendering.shaders.GUIFragmentShader;
import renderEngine.guiRendering.shaders.GUIVertexShader;
import renderEngine.worldRendering.WorldRenderer;
import renderEngine.guiRendering.GUIRenderer;
import input._InputHandler;
import renderEngine.Loader;
import org.json.simple.parser.ParseException;
import org.lwjgl.opengl.GL;
import renderEngine.worldRendering.shaders.WorldFragmentShader;
import renderEngine.worldRendering.shaders.WorldVertexShader;
import util.FSM;
import util.MouseHandler;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


/**
 * @author Adam Limb
 * The GameStateLoop class is a singleton entity with a finite state machine component that can either be loading, in the main menu or ingame.
 * The active state dictates the update loop to be executed upon reaching the fixed timestep.
 *
 * It's also an entity. Which seems perverse considering that puts it on the level of tiles and other entities.
 * However, we could either have it extend from Component, which is more disgusting, or copy our FSM code.
 * As it stands there seems to be no good options available to us, so Entity it is.
 *
 * Alternatively, we could put an FSM base code into util, extend it here, then have an FSM Component extend from it
 * to serve our FSM component needs... Until then, this is TODO.
 *
 * @see FSM
 * @see FSM.FSMListener
 */
public class GameStateMachine extends FSM<GameStateEnum>{

    private static GameStateMachine instance = null;

    //These are all things we'll always need.
    private static WorldRenderer renderer;
    private static GUIRenderer guiRenderer;
    private static EntityRenderer entityRenderer;
    public static Loader loader;
    public static LayoutManager layoutManager;
    public static _InputHandler<KeyNum> handler;


    private static _GameState activeState;

    /**
     * @Param [long] window - The GLFW window to execute in, this is parsed to the base.SymbolTable to be stored for access
     * by other classes. It also parses itself, so that GUI elements can request state changes.
     * @see SymbolTable
     */
    private GameStateMachine(long window) {
        super(GameStateEnum.STARTUP);

        SymbolTable.getInstance(window, this);


        //Add out valid transitions...
        addValidTransition(GameStateEnum.STARTUP, GameStateEnum.MENU);
        addValidTransition(GameStateEnum.MENU, GameStateEnum.LOADING);
        addValidTransition(GameStateEnum.LOADING, GameStateEnum.PLAYING);
        addValidTransition(GameStateEnum.PLAYING, GameStateEnum.LOADING);
        addValidTransition(GameStateEnum.LOADING, GameStateEnum.MENU);

        //Begin our game loop.
        gameLoop();
    }

    /**
     * A singleton method to create a GameStateLoop if it doesn't exist, and return the instance if it does.
     * Since we have this, I'm unsure as to whether we should keep a copy of the instance in the GST.
     *
     * @param window - The window to be used for instantiation, if necessary.
     * @return - Create a new GameStateLoop, if one exists, simply ignore window and return the current instance.
     * @throws IOException - Thrown by the FSM Listener on invalid state change.
     * @throws ParseException - Thrown by the FSM Listener on invalid state parsed.
     */
    public static GameStateMachine getInstance(long window) throws IOException, ParseException {
        if(instance != null) {
            return instance;
        }else{
            return new GameStateMachine(window);
        }
    }

    /**
     * Another method to return the instance, this time not requiring the window as we have no need to instantiate
     * a GameStateLoop. If you attempt to use this to instantiate the loop, throw an exception.
     *
     * @return - The active GameStateLoop.
     * @throws IllegalArgumentException - If you try to get the instance and it doesn't exist,
     * tell them to use the other method.
     */
    public static GameStateMachine getInstance() {
        if(instance == null){
            throw new IllegalArgumentException("You need to instantiate the GameStateLoop before accessing it. Use getInstance(long window)");
        }else{
            return instance;
        }
    }

    /**
     * This method initialises OpenGL, creates a Loader for our renderer, then creates our renderers, makes some
     * Test map and container to play with, then establishes the controller. After that, we set up our delta time,
     * our accumulator and our interval alongside some alpha.
     *
     * Then, using the base.Timer class, we get our first time, and our key inputs.
     * We then establish our accumulator, which will track how many times to run game updates. Once the accumulator is
     * less than the interval, we must have achieved that many game updates in a loop. Then we can render the scene
     * based on that. Following this, we can update our FPS as we have now rendered the scene and pushed a frame.
     *
     * TODO - The alpha value is then used to interpolate between those frames.
     *
     */
    private void gameLoop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        // Set the clear color
        glEnable( GL_BLEND );
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        addListener(new GameStateManager());

        transitToState(GameStateEnum.MENU);

        float delta;
        float accumulator = 0f;
        float interval = 1f / 10000f;
        float alpha;

        Timer timer = new Timer();

        SymbolTable.mouseClickHandler = new MouseHandler();

        while (!glfwWindowShouldClose(SymbolTable.getWindow())) {

            /* Get delta time and update the accumulator */
            delta = timer.getDelta();
            accumulator += delta;

            //Check for key callbacks, once we have those callbacks, execute on them.
            glfwPollEvents();
            handler.updateInput();

            while (accumulator >= interval) {
                activeState.update();
                timer.updateUPS();
                accumulator -= interval;
            }
            //Render the scene.
            renderer.newRender();
            //Render entities on top of that.
            entityRenderer.render();
            //Render our GUI on top.
            guiRenderer.render();

            timer.updateFPS();
            //System.out.println("FPS: " + timer.getFPS());
            //System.out.println("UPS: " + timer.getUPS());

            timer.update();
            //Swap the color buffers
            glfwSwapBuffers(SymbolTable.getWindow());
        }

        //On end, destroy any leftover items.
        renderer.destroyOpenGL();
    }



    //Note - These could all be in the gameLoop of course, but they will likely grow and extend in time.
    //Plus, I feel it's more illustrative of what I'm trying to do here.

    public static void createLoaderAndLayoutManager() {
        loader = new Loader();
        SymbolTable.loader = loader;
        layoutManager = new LayoutManager();
    }

    public static void createRenderers() {

        WorldVertexShader worldVertexShader = new WorldVertexShader();
        WorldFragmentShader worldFragmentShader = new WorldFragmentShader();
        renderer = new WorldRenderer(loader, worldVertexShader, worldFragmentShader);

        GUIVertexShader guiVertexShader = new GUIVertexShader();
        GUIFragmentShader guiFragmentShader = new GUIFragmentShader();
        guiRenderer = new GUIRenderer(loader, guiVertexShader, guiFragmentShader);

        EntityVertexShader entityVertexShader = new EntityVertexShader();
        EntityFragmentShader entityFragmentShader = new EntityFragmentShader();
        entityRenderer = new EntityRenderer(loader, entityVertexShader, entityFragmentShader);
    }

    public static void createInputHandlerAndInitialiseController() {
        handler = new _InputHandler<>();
        SymbolTable.setInputHandler(handler);
    }

    public static void setActiveGameState(_GameState newState) {
        activeState = newState;
    }

    @Override
    public String toString() {
        return "Game State Machine: ";
    }
}