package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static final TETile NOTHING = Tileset.NOTHING;
    public static final TETile FLOOR = Tileset.FLOOR;
    public static final TETile WALL = Tileset.WALL;
    public static final TETile LOCKED_DOOR = Tileset.LOCKED_DOOR;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        String inputLower = input.toLowerCase();
        char[] chars = inputLower.toCharArray();

        if (chars[0] == 'l') {
            ArrayList<String> lines = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader("data.txt"));
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<Character, TETile> tileMap = new HashMap<>();
            tileMap.put(NOTHING.character(), NOTHING);
            tileMap.put(FLOOR.character(), FLOOR);
            tileMap.put(WALL.character(), WALL);
            tileMap.put(LOCKED_DOOR.character(), LOCKED_DOOR);

            int width = lines.get(0).replace("\\R", "").length();
            int height = lines.size();
            TETile[][] world = new TETile[width][height];
            for (int x = 0; x < width; x += 1) {
                for (int y = 0; y < height; y += 1) {
                    world[x][y] = NOTHING;
                }
            }
            for (int y = height - 1; y >= 0; y -= 1) {
                String line = lines.get(height - 1 - y).replace("\\R", "");
                for (int x = 0; x < width; x += 1) {
                    world[x][y] = tileMap.get(line.charAt(x));
                }
            }

            return world;
        }

        if (chars[0] != 'n') {
            throw new RuntimeException("Please enter the input string correctly!");
        }

        String seedString = "";
        long seed = 0;

        int i;
        for (i = 1; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                seedString += chars[i];
            } else if (chars[i] == 's') {
                seed = Long.parseLong(seedString);
                break;
            } else {
                throw new RuntimeException("Please enter the input string correctly!");
            }
        }

        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, seed, NOTHING, FLOOR, WALL, LOCKED_DOOR);
        TETile[][] world = mg.generate();

        if (inputLower.endsWith(":q")) {
            String worldString = TETile.toString(world);
            try {
                FileWriter fw = new FileWriter("data.txt");
                fw.write(worldString);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return world;
    }
}
