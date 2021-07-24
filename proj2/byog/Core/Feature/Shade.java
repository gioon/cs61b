package byog.Core.Feature;

import byog.Core.Unit.Player;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Shade implements Serializable {
    private int width, height;
    private TETile[][] origin, shade;
    private boolean open;

    public Shade(int width, int height, TETile nothing) {
        this.width = width;
        this.height = height;
        this.open = false;

        origin = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                origin[x][y] = nothing;
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void change() {
        open = !open;
    }

    public TETile[][] getShade(TETile[][] world, Player player) {
        shade = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            System.arraycopy(origin[i], 0, shade[i], 0, height);
        }

        int x = player.getPlayerPos().getX();
        int y = player.getPlayerPos().getY();
        for (int i = x - 5; i <= x + 5; i++) {
            if (i >= 0 && i <= width - 1) {
                for (int j = y - 5; j <= y + 5; j++) {
                    if (j >= 0 && j <= height - 1) {
                        shade[i][j] = world[i][j];
                    }
                }
            }
        }

        return shade;
    }
}
