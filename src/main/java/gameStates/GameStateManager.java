package gameStates;

import base.GameStateMachine;
import org.json.simple.parser.ParseException;
import util.FSM;

import java.io.IOException;

/**
 * This class listens for the GameState changes as executes the pre and postconditions respectively.
 */
public class GameStateManager implements FSM.FSMListener<GameStateEnum> {


    public void stateChanged(GameStateEnum fromState, GameStateEnum toState) {

    }

    public boolean preconditions(GameStateEnum fromState, GameStateEnum toState) throws IOException, NullPointerException{

        //This must be our first time starting up, so let's create all necessary objects in the base.GameStateMachine.
        if(fromState == GameStateEnum.STARTUP && toState == GameStateEnum.MENU) {
            GameStateMachine.createInputHandlerAndInitialiseController();
            GameStateMachine.createLoaderAndLayoutManager();
            GameStateMachine.createRenderers();
            GameStateMachine.setActiveGameState(new GameStartup(GameStateMachine.handler, GameStateMachine.loader));
            return true;
        }

        //If the transition is unhandled, we assume there are no preconditions to move to this state.
        return true;

    }

    public void onPreconditionsFailure(GameStateEnum fromState, GameStateEnum toState) {
        if(fromState == null && toState == GameStateEnum.STARTUP) {
            throw new IllegalStateException("Startup failure! Check initialising functions.");
        }
    }

    public boolean postconditions(GameStateEnum fromState, GameStateEnum toState) {
        return false;
    }

    public void onPostconditionsFailure(GameStateEnum fromState, GameStateEnum toState) {

    }
}
