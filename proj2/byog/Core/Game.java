package byog.Core;

import byog.Core.Feature.LightSource;
import byog.Core.Feature.Shade;
import byog.Core.Unit.Door;
import byog.Core.Unit.Guard;
import byog.Core.Unit.Player;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
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
    private final TERenderer ter = new TERenderer();

    private static final int C_WIDTH = Parameters.C_WIDTH;
    private static final int C_HEIGHT = Parameters.C_HEIGHT;
    private static final int MAX_WIDTH = Parameters.MAX_WIDTH;
    private static final int MAX_HEIGHT = Parameters.MAX_HEIGHT;
    private static final int INC_WIDTH = Parameters.INC_WIDTH;
    private static final int INC_HEIGHT = Parameters.INC_HEIGHT;
    private static final int FONT_SIZE = Parameters.FONT_SIZE;
    private static final int ROUND = Parameters.ROUND;

    private int width = MAX_WIDTH - (ROUND - 1) * INC_WIDTH;
    private int height = MAX_HEIGHT - (ROUND - 1) * INC_HEIGHT;
    private int rOff = (C_WIDTH - width) / 2; // right offset
    private int uOff = (C_HEIGHT - height) / 2; // up offset
    private int lOff = C_WIDTH - width - rOff; // left offset - xOffset
    private int dOff = C_HEIGHT - height - uOff; // down offset - yOffset
    // canvas的width和height包含offset和map的Width和height否则界面显示不全

    private int round;
    private Random random;
    private TETile[][] world;
    private Door door;
    private Player player;
    private LightSource lightSource;
    private Guard guard;
    private Shade shade;

    private void changeWidthAndHeight() {
        if (width + INC_WIDTH <= MAX_WIDTH && height + INC_HEIGHT <= MAX_HEIGHT) {
            width += INC_WIDTH;
            height += INC_HEIGHT;
            rOff = (C_WIDTH - width) / 2;
            uOff = (C_HEIGHT - height) / 2;
            lOff = C_WIDTH - width - rOff;
            dOff = C_HEIGHT - height - uOff;
        }
    }

    private void drawRandomSeed(String s) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, FONT_SIZE * 2);
        StdDraw.setFont(font);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.75,
                "Please enter a random seed (S to stop)");
        font = new Font("Monaco", Font.BOLD, FONT_SIZE * 2);
        StdDraw.setFont(font);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.5, s + "_");
        StdDraw.show();
    }

    private Long enterRandomSeed() {
        String seedStr = "";
        drawRandomSeed(seedStr);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 's' && !seedStr.equals("")) {
                    break;
                }
                if (Character.isDigit(c)) {
                    seedStr += c;
                }
                drawRandomSeed(seedStr);
            }
        }
        return Long.parseLong(seedStr);
    }

    private void newGame() {
        MapGenerator mg = new MapGenerator(width, height, random);
        mg.generate();
        world = mg.getWorld();
        door = mg.getDoor();
        player = mg.getPlayer();
        lightSource = mg.getLightSource();
        guard = mg.getGuard();
        shade = mg.getShade();
    }

    private void loadGame() {
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

        random = saving.getRandom();
        world = saving.getWorld();
        door = saving.getDoor();
        player = saving.getPlayer();
        lightSource = saving.getLightSource();
        guard = saving.getGuard();
        shade = saving.getShade();
    }

    private void saveGame() {
        Saving saving = new Saving();
        saving.setRandom(random);
        saving.setWorld(world);
        saving.setDoor(door);
        saving.setPlayer(player);
        saving.setLightSource(lightSource);
        saving.setGuard(guard);
        saving.setShade(shade);

        try {
            FileOutputStream fileOut = new FileOutputStream("data.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(saving);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playGame(char c) {
        switch (c) {
            case 'w':
            case 'a':
            case 's':
            case 'd':
                player.move(world, door, guard, c);
                return;
            case '1':
                shade.change();
                return;
            case '2':
                lightSource.change(world, player, guard);
                return;
            default:
        }
    }

    private void drawHUD() {
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, FONT_SIZE + 6);
        StdDraw.setFont(font);
        StdDraw.text(C_WIDTH * 0.2, C_HEIGHT * 0.9,
                "❤ x " + player.getHealth());

        double mx = StdDraw.mouseX();
        double my = StdDraw.mouseY();
        if (mx > lOff && mx < lOff + width
                && my > dOff && my < dOff + height) {
            int wx = (int) (mx - lOff);
            int wy = (int) (my - dOff);
            StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.9,
                    "You see " + world[wx][wy].description() + ".");
        }

        StdDraw.show();
    }

    private void drawMenu() {
        ter.initialize(C_WIDTH, C_HEIGHT, lOff, dOff);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, FONT_SIZE * 3);
        StdDraw.setFont(font);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.75, "CS61B: THE GAME");
        font = new Font("Monaco", Font.BOLD, FONT_SIZE * 2);
        StdDraw.setFont(font);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.52, "New Game (N)");
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.46, "Load Game (L)");
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.40, "Quit (Q)");
        StdDraw.show();
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        drawMenu();

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
            random = new Random(enterRandomSeed());
            newGame();
        }

        ter.renderFrame(world);
        drawHUD();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == ':') {
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            c = Character.toLowerCase(StdDraw.nextKeyTyped());
                            if (c == 'q') {
                                saveGame();
                                System.exit(0);
                            } else {
                                break;
                            }
                        }
                    }
                }
                playGame(c);
            }
            StdDraw.pause(10);
            if (shade.isOpen()) {
                ter.renderFrame(shade.getShade(world, player));
            } else {
                ter.renderFrame(world);
            }
            drawHUD();
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

        int i = 1;
        switch (chars[0]) {
            case 'q':
                System.exit(0);
                break;
            case 'l':
                loadGame();
                break;
            case 'n':
                String seedStr = "";
                while (i < chars.length) {
                    if (Character.isDigit(chars[i])) {
                        seedStr += chars[i];
                    } else if (chars[i] == 's') {
                        i++;
                        break;
                    }
                    i++;
                }
                random = new Random(Long.parseLong(seedStr));
                newGame();
                break;
            default:
                throw new RuntimeException("Please enter the input string correctly!");
        }

        while (i < chars.length) {
            if (chars[i] == ':') {
                if (i + 1 > chars.length) {
                    break;
                }
                if (chars[i + 1] == 'q') {
                    saveGame();
                    break;
                }
                i++;
            }

            playGame(chars[i]);
            i++;
        }

        return world;
    }
}
