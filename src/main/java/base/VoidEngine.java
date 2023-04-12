package base;

import org.json.simple.parser.ParseException;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;


import java.io.IOException;
import java.nio.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

//TODO - Provide checks for whether or not the object exists, and throw good error messages.

/**
 * This is the main class for the Void Engine.
 * The Void Engine is (hopefully) designed to be easily modded and extended using a component-based architecture.
 * You can make maps for the engine with free, open source software like Tiled.
 * Entities are going to be created by naming the components you desire, and providing those components parameters.
 *
 * Networking functions in a 'map-down' like fashion, where default methods/classes can be overwritten by writing a
 * class/method of the same name in a directory that mirrors the structure of the engine.
 *
 * Obviously, all this is subject to change.
 *
 * The Void Engine is an open source Java game engine written by Adam Limb as practice for writing in Java.
 * The first game made in the Void Engine will be Gawne, an RPG with Roguelike dungeons.
 */
public class VoidEngine {

    public static int width = 1280;
    public static int height = 720;
    private static long window;

    public void run() throws IOException, ParseException {
            //Create basic window and some basic callbacks such as resizing.
            init();

            // This will begin the game loop.
            GameStateMachine.getInstance(window);

            // Hitting the quit button in game or in menu will break from the loop, allowing you to quit.
            // Free the window callbacks and destroy the window
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);

            // Terminate GLFW and free the error callback
            glfwTerminate();
            Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);

        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        //When the window resizes, update the width and height of the screen.
        glfwSetWindowSizeCallback(window, (window, width, height) ->
        {
            this.width = width;
            this.height = height;
            glViewport(0, 0, width, height);
        });


        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {

            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if(vidmode == null){
                throw new NullPointerException();
            }

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(window);

    }

    public static void main(String[] args) throws IOException, ParseException {
        new VoidEngine().run();
    }

}