package byog.Core;

import byog.Core.Feature.LightSource;
import byog.Core.Feature.Shade;
import byog.Core.Unit.Door;
import byog.Core.Unit.Flower;
import byog.Core.Unit.Guard;
import byog.Core.Unit.Player;
import byog.Core.Unit.Portal;
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
import java.util.Arrays;
import java.util.Random;

public class Game {
    private final TERenderer ter = new TERenderer();

    private static final int C_WIDTH = Parameters.C_WIDTH;
    private static final int C_HEIGHT = Parameters.C_HEIGHT;
    private static final int FONT_SIZE = Parameters.FONT_SIZE;

    private static final int MAX_WIDTH = Parameters.MAX_WIDTH;
    private static final int MAX_HEIGHT = Parameters.MAX_HEIGHT;
    private static final int INC_WIDTH = Parameters.INC_WIDTH;
    private static final int INC_HEIGHT = Parameters.INC_HEIGHT;
    private static final int ROUND = Parameters.ROUND;

    private static final int LEFT_OFF = (C_WIDTH - MAX_WIDTH) / 2; // left offset - xOffset
    private static final int DOWN_OFF = (C_HEIGHT - MAX_HEIGHT) / 2; // down offset - yOffset
    // canvas的width和height包含offset和map的Width和height否则界面显示不全

    private int width;
    private int height;
    private int gameState;

    private int round;
    private Random random;
    private TETile[][] world;
    private Door door;
    private Player player;
    private LightSource lightSource;
    private Guard guard;
    private Flower flower;
    private Portal portal;
    private Shade shade;

    private void setWidthAndHeight() {
        if (round > 0) {
            width = MAX_WIDTH - (ROUND - round) * INC_WIDTH;
            height = MAX_HEIGHT - (ROUND - round) * INC_HEIGHT;
        }
    }

    private void drawRandomSeed(String s) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, FONT_SIZE * 2);
        StdDraw.setFont(font);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.75,
                "Please enter a random seed (S to stop)");
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
        setWidthAndHeight();

        MapGenerator mg = new MapGenerator(width, height, random);
        mg.generate();
        world = mg.getWorld();
        door = mg.getDoor();
        player = mg.getPlayer();
        lightSource = mg.getLightSource();
        guard = mg.getGuard();
        flower = mg.getFlower();
        portal = mg.getPortal();
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

        round = saving.getRound();
        setWidthAndHeight();

        random = saving.getRandom();
        world = saving.getWorld();
        door = saving.getDoor();
        player = saving.getPlayer();
        lightSource = saving.getLightSource();
        guard = saving.getGuard();
        flower = saving.getFlower();
        portal = saving.getPortal();
        shade = saving.getShade();
    }

    private void saveGame() {
        Saving saving = new Saving();
        saving.setRound(round);
        saving.setRandom(random);
        saving.setWorld(world);
        saving.setDoor(door);
        saving.setPlayer(player);
        saving.setLightSource(lightSource);
        saving.setGuard(guard);
        saving.setFlower(flower);
        saving.setPortal(portal);
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
                gameState = player.move(world, c);
                // 0: nothing 1: win / new game 2: lose
                return;
            case '1':
                shade.change();
                return;
            case '2':
                lightSource.change(world);
                return;
            case 'f':
                player.changeSpeed();
                return;
            case 'g':
                portal.setPortal(world);
                return;
            default:
        }
    }

    private void drawText(String s) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, FONT_SIZE * 2);
        StdDraw.setFont(font);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.5, s);
        StdDraw.show();
        StdDraw.pause(500);
    }

    private void drawGame() {
        drawText("ROUND " + round);
        ter.renderFrame(world);
        drawHUD();
    }

    private void drawHUD() {
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, FONT_SIZE + 6);
        StdDraw.setFont(font);
        StdDraw.text(C_WIDTH * 0.2, C_HEIGHT * 0.92,
                "❤ - " + player.getHealth()
                + "     ⛸ - " + player.getStepNum()
                + " - " + player.getShield()
                + "     ✿ - " + player.getPower()
                + " - " + player.getSpeed());

        double mx = StdDraw.mouseX();
        double my = StdDraw.mouseY();
        if (mx > LEFT_OFF && mx < LEFT_OFF + width
                && my > DOWN_OFF && my < DOWN_OFF + height) {
            int wx = (int) (mx - LEFT_OFF);
            int wy = (int) (my - DOWN_OFF);
            StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.92,
                    "You see " + world[wx][wy].description() + ".");
        }

        StdDraw.show();
    }

    private void drawMenu() {
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

    private void playWithKeyboardMenu() {
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
            round = 1;
            random = new Random(enterRandomSeed());
            newGame();
        }

        drawGame();
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter.initialize(C_WIDTH, C_HEIGHT, LEFT_OFF, DOWN_OFF);
        playWithKeyboardMenu();

        char c;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == 'm') {
                    playWithKeyboardMenu();
                    continue;
                }
                if (c == 'b') {
                    saveGame();
                    playWithKeyboardMenu();
                    continue;
                }
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
                if (gameState == 1) {
                    gameState = 0;
                    if (round == ROUND) {
                        drawText("YOU WIN!");
                        round = 1;
                        newGame();
                        drawGame();
                    } else {
                        round++;
                        newGame();
                        drawGame();
                    }
                }
                if (gameState == 2) {
                    gameState = 0;
                    drawText("GAME OVER!");
                    newGame();
                    drawGame();
                }
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

    private int playWithInputStringMenu(char[] chars, int i) {
        switch (chars[i]) {
            case 'q':
                System.exit(0);
                break;
            case 'l':
                loadGame();
                break;
            case 'n':
                String seedStr = "";
                i++;
                while (i < chars.length) {
                    if (chars[i] == 's') {
                        break;
                    }
                    if (Character.isDigit(chars[i])) {
                        seedStr += chars[i];
                    }
                    i++;
                }
                round = 1;
                random = new Random(Long.parseLong(seedStr));
                newGame();
                break;
            default:
                throw new RuntimeException("Please enter the input string correctly!"
                        + Arrays.toString(chars) + " " + chars[i] + " " + i);
        }

        i++;
        return i;
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
        int i = playWithInputStringMenu(chars, 0);

        while (i < chars.length) {
            if (chars[i] == 'm') {
                i = playWithInputStringMenu(chars, i + 1);
                continue;
            }
            if (chars[i] == 'b') {
                saveGame();
                i = playWithInputStringMenu(chars, i + 1);
                continue;
            }
            if (chars[i] == ':') {
                if (i + 1 > chars.length) {
                    break;
                }
                if (chars[i + 1] == 'q') {
                    saveGame();
                    break;
                }
            }

            playGame(chars[i]);
            if (gameState == 1) {
                gameState = 0;
                if (round == ROUND) {
                    round = 1;
                    newGame();
                } else {
                    round++;
                    newGame();
                }
            }
            if (gameState == 2) {
                gameState = 0;
                newGame();
            }
            i++;
        }

        if (shade.isOpen()) {
            return shade.getShade(world, player);
        }
        return world;
    }
}
