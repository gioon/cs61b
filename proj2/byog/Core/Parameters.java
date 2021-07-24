package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Parameters {
    /* Feel free to change the width and height. */
    public static final int C_WIDTH = 70; // canvas width
    public static final int C_HEIGHT = 45; // canvas height
    public static final int MAX_WIDTH = 60; // map max width
    public static final int MAX_HEIGHT = 35; // map max height
    public static final int INC_WIDTH = 10; // increase width per round
    public static final int INC_HEIGHT = 5; // increase height per round
    public static final int FONT_SIZE = 16;
    public static final int ROUND = 3;

    public static final TETile NOTHING = Tileset.NOTHING;
    public static final TETile FLOOR = Tileset.FLOOR;
    public static final TETile WALL = Tileset.WALL;
    public static final TETile LOCKED_DOOR = Tileset.LOCKED_DOOR;
    public static final TETile UNLOCKED_DOOR = Tileset.UNLOCKED_DOOR;
    public static final TETile PLAYER = Tileset.PLAYER;
    public static final TETile BULB = Tileset.BULB;
    public static final TETile[] LIGHTS = Tileset.LIGHTS;
    public static final TETile GUARD = Tileset.GUARD;
    public static final int ATTEMPT = 20;
    public static final int HEALTH = 5;
}
