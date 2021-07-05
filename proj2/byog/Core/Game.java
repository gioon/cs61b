package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 70;
    public static final int HEIGHT = 35;

    public static final TETile NOTHING = Tileset.NOTHING;
    public static final TETile FLOOR = Tileset.FLOOR;
    public static final TETile WALL = Tileset.WALL;
    public static final TETile LOCKED_DOOR = Tileset.LOCKED_DOOR;

    public static final int HEART = 5;

    public static final int FONT_SIZE = 20;

    private TETile[][] world;
    private Player player;
    private Random random;

    public void drawRandomSeed(String s) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, FONT_SIZE * 2);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.75, "Please enter a random seed (S to stop)");
        font = new Font("Monaco", Font.BOLD, FONT_SIZE);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.5, s + "_");
        StdDraw.show();
    }

    public Long enterRandomSeed() {
        String seedStr;

        while (true) {
            drawRandomSeed("");
            seedStr = "";
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    char c = StdDraw.nextKeyTyped();
                    if (c == 's') {
                        break;
                    }
                    seedStr += c;
                    drawRandomSeed(seedStr);
                }
            }
            if (seedStr.matches("[0-9]+")) {
                break;
            }
        }

        return Long.parseLong(seedStr);
    }

    public void newGame(long seed) {
        random = new Random(seed);
        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, random,
                NOTHING, FLOOR, WALL, LOCKED_DOOR);
        world = mg.generate();
        player = new Player(HEART);
    }

    public void loadGame() {
        Saving saving = null;

        try {
            FileInputStream fileIn = new FileInputStream("data.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            saving = (Saving) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            System.exit(0); // 没有存档会直接退出
        }

        random = saving.random;
        world = saving.world;
        player = saving.player;
//        ArrayList<String> lines = new ArrayList<>();
//        FileReader fr;
//        try {
//            fr = new FileReader("data.txt");
//        } catch (IOException e) {
//            System.exit(0); // 没有存档会直接退出
//            return;
//        }
//        try {
//            BufferedReader br = new BufferedReader(fr);
//            String line;
//            while ((line = br.readLine()) != null) {
//                lines.add(line);
//            }
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Map<Character, TETile> tileMap = new HashMap<>();
//        tileMap.put(NOTHING.character(), NOTHING);
//        tileMap.put(FLOOR.character(), FLOOR);
//        tileMap.put(WALL.character(), WALL);
//        tileMap.put(LOCKED_DOOR.character(), LOCKED_DOOR);
//
//        int width = lines.get(0).replace("\\R", "").length();
//        int height = lines.size();
//
//        world = new TETile[width][height];
//        for (int x = 0; x < width; x += 1) {
//            for (int y = 0; y < height; y += 1) {
//                world[x][y] = NOTHING;
//            }
//        }
//        for (int y = height - 1; y >= 0; y -= 1) {
//            String line = lines.get(height - 1 - y).replace("\\R", "");
//            for (int x = 0; x < width; x += 1) {
//                world[x][y] = tileMap.get(line.charAt(x));
//            }
//        }
    }

    public void saveGame() {
        Saving saving = new Saving(random, world, player);
        try {
            FileOutputStream fileOut = new FileOutputStream("data.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(saving);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String worldString = TETile.toString(world);
//        try {
//            FileWriter fw = new FileWriter("data.txt");
//            fw.write(worldString);
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, FONT_SIZE * 2);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.75, "CS61B: THE GAME");
        font = new Font("Monaco", Font.BOLD, FONT_SIZE);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.5, "New Game (N)");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.45, "Load Game (L)");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.4, "Quit (Q)");
        StdDraw.show();

        char c;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == 'q') {
                    System.exit(0);
                }
                if (c == 'l' || c == 'n') {
                    break;
                }
            }
        }
        if (c == 'l') {
            loadGame();
        }
        if (c == 'n') {
            newGame(enterRandomSeed());
        }

        ter.renderFrame(world);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                if (c == ':') {
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            c = StdDraw.nextKeyTyped();
                            if (Character.toLowerCase(c) == 'q') {
                                saveGame();
                                System.exit(0);
                            } else {
                                break;
                            }
                        }
                    }
                } else {
                    break; // TODO playGame
                }
            }
        }
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

        switch (chars[0]) {
            case 'q':
                System.exit(0);
                break;
            case 'l':
                loadGame();
                break;
            case 'n':
                String seedStr = "";
                long seed = 0;
                for (int i = 1; i < chars.length; i++) {
                    if (Character.isDigit(chars[i])) {
                        seedStr += chars[i];
                    } else if (chars[i] == 's') {
                        seed = Long.parseLong(seedStr);
                        break;
                    } else {
                        throw new RuntimeException("Please enter the input string correctly!");
                    }
                }
                newGame(seed);
                break;
            default:
                throw new RuntimeException("Please enter the input string correctly!");
        }

        // TODO playGame
        if (inputLower.endsWith(":q")) {
            saveGame();
        }

        return world;
    }
}
