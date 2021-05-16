package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.junit.Test;

public class MapVisualTest {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static final int SEED = 15;
    private static final TETile NOTHING = Tileset.NOTHING;
    private static final TETile FLOOR = Tileset.FLOOR;
    private static final TETile WALL = Tileset.WALL;
    private static final TETile LOCKED_DOOR = Tileset.LOCKED_DOOR;

    @Test
    public void testInitialized() {
        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, SEED, NOTHING, FLOOR, WALL, LOCKED_DOOR);
        Rectangle firstRec = mg.generateFirst();
        mg.initialize();
        TETile[][] world = mg.getWorld();
        System.out.println(TETile.toString(world));
    }

    @Test
    public void testMapGeneratorLoop() {
        for (int seed = 0; seed < 1000; seed++) {
            MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, seed, NOTHING, FLOOR, WALL, LOCKED_DOOR);
            try {
                System.out.println("seed "+seed);
                TETile[][] world = mg.generate();

            } catch (Exception e) {
                System.out.println(seed);
            }
        }
    }

    @Test
    public void testMapGenerator() {
        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, SEED, NOTHING, FLOOR, WALL, LOCKED_DOOR);
        TETile[][] world = mg.generate();
        System.out.println(TETile.toString(world));
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, SEED, NOTHING, FLOOR, WALL, LOCKED_DOOR);
        TETile[][] world = mg.generate();

        ter.renderFrame(world);
    }
}
