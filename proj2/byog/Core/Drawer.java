package byog.Core;

import byog.Core.Feature.Shade;
import byog.Core.Map.World;
import byog.Core.Unit.Player;
import byog.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class Drawer {

    private static final int C_WIDTH = Parameters.C_WIDTH;
    private static final int C_HEIGHT = Parameters.C_HEIGHT;
    private static final int FONT_SIZE = Parameters.FONT_SIZE;

    private static final int MAX_WIDTH = Parameters.MAX_WIDTH;
    private static final int MAX_HEIGHT = Parameters.MAX_HEIGHT;
    private static final int LEFT_OFF = (C_WIDTH - MAX_WIDTH) / 2; // left offset - xOffset
    private static final int DOWN_OFF = (C_HEIGHT - MAX_HEIGHT) / 2; // down offset - yOffset
    // canvas的width和height包含offset和map的Width和height否则界面显示不全

    private static final Font font2 = new Font("Monaco", Font.BOLD, FONT_SIZE * 2);
    private static final Font font3 = new Font("Monaco", Font.BOLD, FONT_SIZE * 3);
    private static final Font font6 = new Font("Monaco", Font.BOLD, FONT_SIZE + 6);

    public static void initialize(TERenderer ter) {
        ter.initialize(C_WIDTH, C_HEIGHT, LEFT_OFF, DOWN_OFF);
    }

    public static void drawMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(font3);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.75, "CS61B: THE GAME");
        StdDraw.setFont(font2);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.52, "New Game (N)");
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.46, "Hard Game (H)");
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.40, "Load Game (L)");
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.34, "Quit (Q)");
        StdDraw.show();
    }

    public static void drawRandomSeed(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font2);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.75,
                "Please enter a random seed (S to stop)");
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.5, s + "_");
        StdDraw.show();
    }

    public static void drawEyecatch(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(font2);
        StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.5, s);
        StdDraw.show();
        StdDraw.pause(500);
    }

    public static void drawWorld(TERenderer ter, World world) {
        ter.renderFrame(world.getState());
    }

    public static void drawShadeWorld(TERenderer ter, World world, Shade shade) {
        ter.renderFrame(shade.getShadeWorld(world).getState());
    }

    public static void drawHUD(World world, Player player) {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(font6);
        StdDraw.text(C_WIDTH * 0.2, C_HEIGHT * 0.92,
                "❤ - " + player.getHealth()
                        + "     ⛸ - " + player.getStepNum()
                        + " - " + player.getShield()
                        + "     ✿ - " + player.getPower()
                        + " - " + player.getSpeed());

        double mx = StdDraw.mouseX();
        double my = StdDraw.mouseY();
        if (mx > LEFT_OFF && mx < LEFT_OFF + world.getWidth()
                && my > DOWN_OFF && my < DOWN_OFF + world.getHeight()) {
            int x = (int) (mx - LEFT_OFF);
            int y = (int) (my - DOWN_OFF);
            StdDraw.text(C_WIDTH * 0.5, C_HEIGHT * 0.92,
                    "You see " + world.get(x, y).description() + ".");
        }

        StdDraw.show();
    }
}
