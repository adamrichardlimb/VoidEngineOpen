package level;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import base.SymbolTable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import renderEngine.Loader;
import renderEngine.Texture;

/**
 * This class acts as the map loader and unloader, taking in a path to the JSON of the map and creating a Map
 * from that.
 *
 * How this is done is better explained in the comments of the Maploader.
 *
 * TODO - Move map building out of Maploader constructor and into a method.
 */
public class Maploader {

    private Texture tileset;
    private int tilesheetInt;
    private String tilesheetPath;
    public int[][] IDs;
    public Loader loader;

    //TODO - Move PNGLoading here to create a tileset to be spritebatched.
    //TODO - Use width/height of image to check for bad tileIDs with if(ID > width * height) throw Exception
    public Maploader(String path, Loader loader)  {

        this.loader = loader;
    }


    public Map loadMap(String path, Loader loader) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        try(Reader reader = new FileReader(path)) {

            //Create JSONParser.
            Object obj = parser.parse(reader);
            JSONObject level = (JSONObject) obj;

            try {
                //Reader thinks these are longs for some reason, or at least, seems to force me to cast to them.
                //Make temporary variables and cast to int.
                long tempWidth = (long) level.get("width");
                long tempHeight = (long) level.get("height");
                int width = (int) tempWidth;
                int height = (int) tempHeight;

                //Get and set the tilesize.
                long tempSize = (long) level.get("tileheight");
                int tileSize = (int) tempSize;

                SymbolTable.setTilesetResolution(tileSize);

                //Build a map of IDs.
                JSONArray layers = (JSONArray) level.get("layers");

                //Set the amount of layers, then initialise the array.
                int numLayers = layers.size();

                IDs = new int[numLayers][width * height];

                //Build layers.
                int layerCount = 0;

                //WARNING: This must be an Object, but to be used properly must then be cast to a JSONObject.
                for (Object ly : layers) {

                    //Get the data array for the layer.
                    JSONObject layer = (JSONObject) ly;

                    JSONArray tempLayer = (JSONArray) layer.get("data");

                    //Iterate over the tiles, casting to int and putting in array.
                    for (int i = 0; i < width * height; i++) {

                        long value = (long) tempLayer.get(i);
                        int castValue = (int) value;

                        IDs[layerCount][i] = castValue;

                    }

                    layerCount++;

                }

                //Get the number of tilesheets we'll be using.
                JSONArray tilesheets = (JSONArray) level.get("tilesets");

                //WARNING: This wasn't coded with multiple tilesets in mind.
                for(Object ly : tilesheets) {
                        //Get the data array for the layer.
                        JSONObject layer = (JSONObject) ly;

                        if(layer.get("source") != null){
                            this.tilesheetPath = (String) layer.get("source");
                        }else{
                            throw new NullPointerException("Tilesheet path is null! Have you entered it correctly?");
                        }

                        //System.out.println(tilesheetPath);
                        tilesheetInt = loader.loadPNGTexture(tilesheetPath);
                }

                //TODO - Refactor all 'resolution' to 'tileSize'
                this.tileset = new Texture(loader, tilesheetInt, width, height, tileSize);

                //Upon successfully reading JSON data, create a map.
                Map mapToCreate = new Map(loader, tileset);

                if( ((width * height) % 64) != 0 ) throw new RuntimeException("Map can't be chunked! Are the dimensions multiples of 8?");

                //Generate an array of number of chunks.
                int[] chunkIDs = new int[(width * height) / 64];

                //Populate those chunks with IDs.
                for(int i = 0; i < chunkIDs.length; i++) {

                    chunkIDs[i] = i;

                }


                //TODO - Make chunks load/unload dynamically.
                for (int i = 0; i < chunkIDs.length; i++) {

                    //Initialise an array of an array for 64 tiles (8 x 8 = 64 = one chunk)
                    int[][] chunkTileIDs = new int[numLayers][64];


                    //For each layer.
                    for(int l = 0; l < numLayers; l++) {

                        //Get an 8 * 8 grid.
                        for(int y = 0; y < 8; y++) {
                            for(int x = 0; x < 8; x++) {

                                //Get the location in chunk space
                                //a co-ordinate system with (width / 8, height/8)
                                int tempX = getChunkXByID(width, i);
                                int tempY = getChunkYByID(width, i);

                                //The value in the actual map array is 8 * x along
                                //plus 8 * y * width
                                //Because every drop in y in chunk space means you've gone the entire width of the chunk 8 times in tilespace.
                                //(tempX * 8) + (tempY * 8 * width) gets you where to start reading from.
                                //The read along in x, and shift down in y.
                                int value = (tempX * 8) + (tempY * 8 * width) + x + (y * width);

                                //Assign those IDs to the chunks ID system.
                                chunkTileIDs[l][x + (y * 8)] = IDs[l][value];



                            }

                        }
                        //System.out.println("Hi!");

                    }


                    //The map acts as a holder for all the chunk details, and will instantiate them on the fly.
                    mapToCreate.addID(i);
                    mapToCreate.addTiles(chunkTileIDs);
                    //new Chunk(this, i, this.TILESHEETS, chunkTileIDs);

                }

                return mapToCreate;

            }catch(NullPointerException nullValueInJSON) {
                System.out.println("Null value in JSON, follow the template at res/template.json and ensure all values are there.");
                throw nullValueInJSON;
            }
        }catch(FileNotFoundException | ParseException exception){
            System.out.println(exception);
            throw exception;
        }


    }

    /**
     * A method to get the chunks x position in the map from its ID. If a Map is made up of 5x5 chunks, then this refers
     * to the position in x with respect to chunks, /not/ tiles.
     *
     * Similar code can be found in the tile class as well, but I'm yet to move it to an interface.
     *
     * @param ID - The chunk ID.
     * @return - The chunks x position in the map.
     */
    public int getChunkXByID(int width, int ID) {

        //If the ID is less than the width in tiles, then you're in the first row.
        //And your ID * tileSize will be your absolute position.
        if(ID < (width / 8)) return ID;

        else {
            //The remainder is where you are in x.
            int remainder = ID % (width / 8);
            return remainder;
        }
    }

    /**
     * A method to get the chunks y position in the map from its ID. If a Map is made up of 5x5 chunks, then this refers
     * to the position in y with respect to chunks, /not/ tiles.
     *
     * Similar code can be found in the tile class as well, but I'm yet to move it to an interface.
     *
     * @param ID - The chunk ID.
     * @return - The chunks y position in the map.
     */
    public int getChunkYByID(int width, int ID) {

        //If your ID is less than the width of the map divided by 8
        //your chunk must be at the top of the map.
        if(ID < (width / 8)) return 0;

        else {

            //There are width/8 number of chunks in x.
            //So however many times your ID can be divided by that
            //is your y in chunk space.
            return (ID / (width / 8));

        }

    }
}