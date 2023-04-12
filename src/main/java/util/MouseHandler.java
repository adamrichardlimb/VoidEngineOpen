package util;


import base.SymbolTable;
import base.VoidEngine;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

//Handling functions related to the mouse, such as getting the mouse position.
public class MouseHandler implements Clamp {

    public double getCursorPosX() {
        //Grab the cursor position in x, convert based on window size.
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(SymbolTable.getWindow(), posX, null);
        double cursorPosInX = (posX.get(0) / (VoidEngine.width / 2f) - 1);

        //For good measure, clamp the result.
        return mathClamp(cursorPosInX, -1d, 1d);
    }

    public double getCursorPosY() {
        //Grab the cursor position in x, convert based on window size.
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(SymbolTable.getWindow(), null, posY);
        double cursorPosInY = -(posY.get(0) / (VoidEngine.height / 2f)) + 1;

        //For good measure, clamp the result.
        return mathClamp(cursorPosInY, -1d, 1d);
    }
}