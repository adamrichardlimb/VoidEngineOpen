package input.controllers;

import entities.Entity;
import components.interactionComponents.BoxComponent;
import input.ControllerStateMachine;
import input._ControllerScheme;

//NOTE - Please /only/ use this style for controller creation.
//NOTE - Please also name anything that extends from the _ControllerSchemeTemplate as follows: Controller[NameOfStateInControllerState]
//NOTE - For example, we use this controller in the state INGAME, which can be found in the ControllerStateMachine file.
//NOTE - So this file is called ControllerInGame.
public class ControllerPlayer extends _ControllerScheme {

    private Entity entityToControl;
    private ControllerStateMachine controllerStateMachine;

    /**
     * A basic controller for the player, simply shifts the box component around currently.
     *  @param entityToControl - The entity we wish to control.
     * @param controllerStateMachine - The state machine we wish to use.
     */
    public ControllerPlayer(Entity entityToControl, ControllerStateMachine controllerStateMachine) {
        this.entityToControl = entityToControl;
        this.controllerStateMachine = controllerStateMachine;
    }

    @Override
    public void onKeyWPress() {

        entityToControl.getComponent(BoxComponent.class).alterPosition(0, -0.01f);

    }

    @Override
    public void onKeySPress() {

        entityToControl.getComponent(BoxComponent.class).alterPosition(0,0.01f);

    }

    @Override
    public void onKeyAPress() {

        entityToControl.getComponent(BoxComponent.class).alterPosition(0.01f,0);

    }

    @Override
    public void onKeyDPress() {

        entityToControl.getComponent(BoxComponent.class).alterPosition(-0.01f,0);

    }

    @Override
    public void onKeyLPress() {

        controllerStateMachine.transitToState(ControllerStateMachine.controllerStates.MENU);

    }

}
