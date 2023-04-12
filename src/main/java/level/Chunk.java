package level;

import entities.worldEntities.Tile;
import base.SymbolTable;
import renderEngine.Loader;
import renderEngine.Texture;

/**
 * This class defines a chunk, which, upon creation - creates new tiles.
 * There exists a deprecated method called destroyChunk, which will be used for chunk unloading.
 */
public class Chunk {

    private final int CHUNKID;
    private Tile[][] TILES;

    public Chunk(int ID, int[][] tiles, Loader loader, Texture tileSheet) {

        this.CHUNKID = ID;

        Tile[][] init = new Tile[tiles.length][64];
        for(int i = 0; i < tiles.length; i++) {
            for(int t = 0; t < 64; t++) {
                //NOTE: The symbol table has this at 0.1f which is a magic number, but I want 20 tiles across the screen to be the maximum zoom right now, which is 0.1f.
                //So it is and it isn't, but we'll create our own Map format sooner enough.
                //When making this class reusable for other gamemodes, I should add it to the map JSON and parse it here
                //Until then, don't remove this TODO.
                init[0][t] = new Tile(SymbolTable.getScale(), loader, CHUNKID, t, tiles[i][t], tileSheet);
            }
        }
        this.TILES = init;
    }


    public void destroyChunk() {
        for (Tile[] layer : TILES) {
            for (Tile tile : layer) {
                tile.cull();
            }
        }
    }
}