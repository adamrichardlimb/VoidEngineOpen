package gameStates;

import input.KeyNum;
import input._InputHandler;
import input.controllers.ControllerMenu;
import base.GameStateMachine;
import renderEngine.Loader;
import renderEngine.guiRendering.Container;
import renderEngine.guiRendering.Widget;

/**
 * This class indicates the startup state, which for now is simply a menu.
 *
 */
public class GameStartup extends _GameState {

    Container MainMenu;

    //I'm pretty torn on this input handler thing, I don't think attaching a listener and shifting controller through
    //that is necessarily the best way to do things. Maybe it is /within/ objects? Look, let's play it by ear.
    public GameStartup(_InputHandler<KeyNum> handler, Loader loader) {
        handler.setActiveController(new ControllerMenu());
        MainMenu = new Container(loader, GameStateMachine.layoutManager);
        MainMenu.moveable = true;
        MainMenu.setProportionalSize(1f,1f);

        Widget testWidget = new Widget(MainMenu);
    }

}
