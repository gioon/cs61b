package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
//    public static final TETile PLAYER = new TETile('@', Color.white, Color.black, "player");
//    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
//            "wall");
//    public static final TETile FLOOR = new TETile('·', Color.lightGray, Color.black,
//            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
//    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
//    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile SAND = new TETile('*', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    public static final TETile PLAYER = new TETile('♂', Color.white, new Color(50, 193, 250, 235),
            "player");
    public static final TETile FLOOR = new TETile('·', Color.gray, Color.black, "floor");
    public static final TETile WALL = new TETile('▒', Color.gray, Color.black, "wall");
    public static final TETile BULB = new TETile('○', Color.gray, Color.black, "bulb");
    public static final TETile[] LIGHTS = new TETile[]{
        new TETile('●', Color.lightGray, new Color(36, 128, 233, 179),
                "bulb with light"),
        new TETile('·', Color.lightGray, new Color(37, 109, 200, 166),
                "floor with light"),
        new TETile('·', Color.lightGray, new Color(40, 100, 177, 153),
                "floor with light"),
        new TETile('·', Color.lightGray, new Color(42, 95, 161, 140),
                "floor with light"),
        new TETile('·', Color.lightGray, new Color(45, 85, 139, 128),
                "floor with light"),
        new TETile('·', Color.lightGray, new Color(45, 80, 125, 115),
                "floor with light")
    };
    public static final TETile GUARD = new TETile('♀', Color.white, Color.pink, "guard");
    public static final TETile NAIL = new TETile('✸', Color.gray, Color.darkGray, "sand");
    public static final TETile FLOWER = new TETile('❀', Color.pink, Color.black, "flower");
    public static final TETile PORTAL = new TETile('∏', Color.orange, Color.black, "portal");
}


