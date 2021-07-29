package byog.Core.Feature;

import byog.Core.Map.World;
import byog.Core.Unit.Player;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Shade implements Serializable {
    private int width, height;
    private World originWorld, shadeWorld;
    private boolean open, hardMode;

    private Player player;
    private LightSource lightSource;

    public Shade(int width, int height, TETile nothing,
                 Player player, LightSource lightSource, boolean hardMode) {
        this.width = width;
        this.height = height;
        this.open = false;
        this.player = player;
        this.lightSource = lightSource;
        this.hardMode = hardMode;

        originWorld = new World(width, height);
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                originWorld.set(x, y, nothing);
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void change() {
        open = !open;
    }

    public World getShadeWorld(World world) {
        shadeWorld = new World(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                shadeWorld.set(i, j, originWorld.get(i, j));
            }
        }
        // rectangle shade
//        int x = player.getPlayerPos().getX();
//        int y = player.getPlayerPos().getY();
//        for (int i = x - 5; i <= x + 5; i++) {
//            if (i >= 0 && i <= width - 1) {
//                for (int j = y - 5; j <= y + 5; j++) {
//                    if (j >= 0 && j <= height - 1) {
//                        shadeWorld.set(i, j, world.get(i, j));
//                    }
//                }
//            }
//        }
        // diamond shade
        int x = player.getPlayerPos().getX();
        int y = player.getPlayerPos().getY();
        int r = 5;
        for (int i = 0; i <= r; i++) {
            for (int j = 0; j <= r - i; j++) {
                int a = x + j;
                int b = x - j;
                diamondShadeHelper(world, a, b, y + i);
                diamondShadeHelper(world, a, b, y - i);
            }
        }


        if (hardMode) {
            lightSource.removeAllLightsShade(shadeWorld, world); // hard mode
        } else {
            lightSource.removeOneLightShade(shadeWorld, world); // easy mode
        }
        return shadeWorld;
    }

    private void diamondShadeHelper(World world, int a, int b, int c) {
        if (c >= 0 && c <= height - 1) {
            if (a >= 0 && a <= width - 1) {
                shadeWorld.set(a, c, world.get(a, c));
            }
            if (b >= 0 && b <= width - 1) {
                shadeWorld.set(b, c, world.get(b, c));
            }
        }
    }
}
