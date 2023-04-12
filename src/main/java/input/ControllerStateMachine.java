package input;

import entities.Entity;
import input.controllers.ControllerPlayer;
import input.controllers.ControllerMenu;
import base.SymbolTable;
import util.Clamp;
import util.FSM;
import util.MouseHandler;

/**
 * An unfinished ControllableComponent. As it stands, we can only control one thing, the player.
 *
 * However, as the functionality of the player grows, we'll want to instantiate it upon the creation of the map.
 * Which means we'll be abstracting this out for different objects we can control.
 */
public class ControllerStateMachine extends FSM implements FSM.FSMListener, Clamp {

    private Entity entityToControl;
    public _InputHandler schemeLayout;

    public enum controllerStates {

        //For the menu.
        MENU,

        //When in the inventory.
        INVENTORY,

        INGAME,

        //When out and about, outside of an inventory.
        ACTIVE,

        //For making people immobile for extended periods of time.
        INACTIVE,

        STARTUP

        /*

        ***Consider having a dictionary for corresponding player states!***

        Future possible controllers states:
        Burrowed - When something crushes you, you can spam movement to escape.
        Frozen - When something freezes you, you can spam click to crack the ice.
        Activating - When activating an item, you can move, but not attack or dodge.

        Free-roam - Just give people the option to roam around freely if in a specific player state.


         */

    }

    /**
     * TODO - Rewrite this to actually be a component, or move into its own class.
     * @param entToControl - The entity we wish to control.
     */
    public ControllerStateMachine(Entity entToControl) {
        super(controllerStates.INACTIVE);

        entityToControl = entToControl;

        schemeLayout = SymbolTable.getInputHandler();
        System.out.println(schemeLayout);

        SymbolTable.mouseClickHandler = new MouseHandler();

        addValidTransition(controllerStates.INACTIVE, controllerStates.ACTIVE);
        addValidTransition(controllerStates.INACTIVE, controllerStates.MENU);
        addValidTransition(controllerStates.MENU, controllerStates.ACTIVE);
        addValidTransition(controllerStates.INACTIVE, controllerStates.STARTUP);
        addValidTransition(controllerStates.STARTUP, controllerStates.MENU);
        addValidTransition(controllerStates.INGAME, controllerStates.MENU);
        addValidTransition(controllerStates.MENU, controllerStates.INGAME);

        addListener(this);
        transitToState(controllerStates.STARTUP);
    }

    /**
     * An implementation of the FSMListener method. We're listening to ourselves, and upon notification of moving states
     * we execute some action. Here we're switching out the controller the player uses.
     *
     * @param fromState - The state we moved from.
     * @param toState - The state we moved to, and are now in.
     * @throws IllegalArgumentException - If you specify an illegal state, we'll throw an error. This should be
     * implemented after we have more fleshed out states and so it remains a TODO.
     */
    @Override
    public void stateChanged(Enum fromState, Enum toState) throws IllegalArgumentException{

        System.out.println(this.toString() + "Moved from " + fromState + " to " + toState);

        if (toState == controllerStates.INGAME) {

            schemeLayout.setActiveController(new ControllerPlayer(entityToControl, this));

        }else if(toState == controllerStates.MENU) {

            schemeLayout.setActiveController(new ControllerMenu());

        }else if(toState == controllerStates.STARTUP) {

            transitToState(controllerStates.MENU);

        }
    }

    @Override
    public String toString() {
        return "ControllerStateMachine: ";
    }

}
