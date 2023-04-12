package gameStates;

import level.Maploader;
import org.json.simple.parser.ParseException;
import renderEngine.Loader;

import java.io.IOException;

/**
 * This class handles loading into and out of games.
 * In the future this may need to be split for single and multiplayer.
 *
 */
public class GameLoading extends _GameState {

    //Status indicates what exactly we're doing, this is used for the loading screen.
    private String status;

    /**
     * Takes a string to the map you wish to load, creates a maploader
     *
     */
    public GameLoading(String mapToLoad, Loader loader) throws IOException, ParseException {

        Maploader maploader = new Maploader(mapToLoad, loader);

    }

    @Override
    public void update() {



    }

}
