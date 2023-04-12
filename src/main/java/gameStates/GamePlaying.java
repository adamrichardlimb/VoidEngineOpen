package gameStates;

import entities.PlayerEntity;
import level.Map;
import base.SymbolTable;
import renderEngine.Loader;

public class GamePlaying extends _GameState {

    private Map currentMap;
    private PlayerEntity player;

    //Creates the entities necessary to play the game.
    public GamePlaying(Loader loader) {
        player = new PlayerEntity(loader);
    }

    //Updates the necessary entities.
    public void update() {
        SymbolTable.getActiveCamera().update();
        player.update();
    }


    //Performs the necessary ending procedures before moving states.
    public void endState() {

    }

}