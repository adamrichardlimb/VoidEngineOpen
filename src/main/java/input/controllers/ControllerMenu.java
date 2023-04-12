package input.controllers;

import components.interactionComponents.CollisionBoxComponent;
import renderEngine.guiRendering.Container;
import input._ControllerScheme;
import base.SymbolTable;
import renderEngine.guiRendering.LayoutManager;

/**
 * A basic controller for the menu, as of right now all it does is query the containers and execute a SetPosThroughClick
 *
 * When the layout manager is done, it'll simply send of its coordinates to the LayoutManager and let the LayoutManager
 * check if you clicked on a container, and if so, if you clicked on something in that container, and if so, what you
 * clicked on, and then execute that functionality.
 */
public class ControllerMenu extends _ControllerScheme {

    /**
     * A basic controller constructor, not reflective of what this will be used for in the future. Maybe we'll make the
     * main menu an entity with a renderable and have that be the toControl?
     */
    public ControllerMenu() {
    }

    public void onMouseLeftPress() {

        double xPos = SymbolTable.mouseClickHandler.getCursorPosX();
        double yPos = SymbolTable.mouseClickHandler.getCursorPosY();

        if(LayoutManager.containerWidgetMap == null) {
            throw new NullPointerException("The list of containers is null, meaning either no containers exist, or the list was removed somehow.");
        }

        if(LayoutManager.containerWidgetMap.size() > 0) {
            System.out.println("More than one...");
            for(Container collider : LayoutManager.containerWidgetMap.keySet()) {
                System.out.println("Doing check...");
                System.out.println(collider.collisionX);
                System.out.println(collider.collisionW);
                if(collider.pointCheck( collider.collisionX, collider.collisionY,
                                        collider.collisionW, collider.collisionH,
                                        (float) xPos, (float) yPos)) {
                    System.out.println("Success!");
                    collider.doClick((float) xPos, (float) yPos);
                }
            }
        }
    }

}