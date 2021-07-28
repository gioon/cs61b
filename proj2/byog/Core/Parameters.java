package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Parameters {
    /* Feel free to change the width and height. */
    public static final int C_WIDTH = 70; // canvas width
    public static final int C_HEIGHT = 45; // canvas height
    public static final int FONT_SIZE = 16;

//    public static final int MAX_WIDTH = 60; // map max width
//    public static final int MAX_HEIGHT = 35; // map max height
//    public static final int INC_WIDTH = 10; // increase width per round
//    public static final int INC_HEIGHT = 5; // increase height per round
//    public static final int ROUND = 3;
    public static final int MAX_WIDTH = 50; // map max width
    public static final int MAX_HEIGHT = 20; // map max height
    public static final int INC_WIDTH = 10; // increase width per round
    public static final int INC_HEIGHT = 5; // increase height per round
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
    public static final TETile NAIL = Tileset.NAIL;
    public static final TETile FLOWER = Tileset.FLOWER;
    public static final TETile PORTAL = Tileset.PORTAL;

    public static final int GUARD_PROB = 2;
    // range: > 0, 2 means generate a guard with light where room's width and height > 3 (2 + 1)
    //                     generate a flower where hallway's width or height > 5 (2 + 1 + 2)
    public static final int NAIL_PROB = 3; // range: 0-100, 3 means <= 3%
    public static final int ATTEMPT = 20;
    public static final int HEALTH = 5;
    public static final int SHIELD_THRESH = 5;
    // 5 steps: one shield, before used cannot gain any more shield
    public static final int POWER_AWARD = 5; // one flower: award 5
    public static final int SPEED_UP = 3; // consume 1 power: move 3 steps

}
