package level;

import renderEngine.Loader;
import renderEngine.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a map.
 *
 * The ArrayList tiles holds onto the chunks, with the ID of the chunk being used as the index of the ArrayList.
 * The contents of the ArrayList at that index is a two-dimensional array of integers, which are the tileIDs of the tile
 * for chunk.
 *
 * So tiles.get(5) returns the tiles of the chunk of ID 5.
 *
 */
public class Map {

    private Loader loader;
    private List<Integer> chunkIDs = new ArrayList<Integer>();
    private List<int[][]> tiles = new ArrayList<int[][]>();
    private Texture tileSheet;

    public Map(Loader loader, Texture tileset) {
        this.loader = loader;
        this.tileSheet = tileset;
    }

    //TODO - Add check to ensure we can't instantiate the same chunk twice.
    public Chunk instantiateChunk(int chunkID) {
        if(chunkID < 0 || chunkID > chunkIDs.size()){
            throw new IllegalArgumentException("Attempt to instantiate an illegal chunk! ChunkID: " + chunkID + " is either less than zero, or greater than the length of the list of chunks and therefore doesn't exist.");
        }

        if(tiles.get(chunkID) == null) {
            throw new NullPointerException("Attempt to instantiate null chunk of ID: " + chunkID);
        }

        return new Chunk(chunkID, tiles.get(chunkID), this.loader, tileSheet);
    }

    public void addTiles(int[][] tileList) {
        this.tiles.add(tileList);
    }

    public void addID(int ID) {

        this.chunkIDs.add(ID);
    }
}