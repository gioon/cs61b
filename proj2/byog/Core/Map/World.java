package byog.Core.Map;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class World implements Serializable {
    private TETile[][] world;
    private int width, height;

    public World(int width, int height) {
        world = new TETile[width][height];
        this.width = width;
        this.height = height;
    }

    public void set(int x, int y, TETile t) {
        world[x][y] = t;
    }

    public void set(Position p, TETile t) {
        world[p.getX()][p.getY()] = t;
    }

    public TETile get(int x, int y) {
        return world[x][y];
    }

    public TETile get(Position p) {
        return world[p.getX()][p.getY()];
    }

    public TETile[][] getState() {
        return world;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
