package byog.Core;

//import byog.TileEngine.TERenderer;
//import byog.TileEngine.TETile;
//import byog.TileEngine.Tileset;
//import org.junit.Test;
//
//import java.util.Random;
//
//public class MapVisualTest {
//
//    private static final int WIDTH = 80;
//    private static final int HEIGHT = 40;
//    private static final int SEED = 15;
//    private static final TETile NOTHING = Tileset.NOTHING;
//    private static final TETile FLOOR = Tileset.FLOOR;
//    private static final TETile WALL = Tileset.WALL;
//    private static final TETile LOCKED_DOOR = Tileset.LOCKED_DOOR;
//    public static final TETile BULB = Tileset.BULB;
//    private static final TETile PLAYER = Tileset.PLAYER;
//
//    @Test
//    public void testInitialized() {
//        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, new Random(SEED),
//                NOTHING, FLOOR, WALL, LOCKED_DOOR, BULB, PLAYER);
//        Rectangle firstRec = mg.generateFirst();
//        mg.initialize();
//        TETile[][] world = mg.getWorld();
//        System.out.println(TETile.toString(world));
//    }
//
//    @Test
//    public void testMapGeneratorLoop() {
//        for (int seed = 0; seed < 1000; seed++) {
//            MapGenerator mg = new MapGenerator(
//                    WIDTH, HEIGHT, new Random(SEED),
//                    NOTHING, FLOOR, WALL, LOCKED_DOOR, BULB, PLAYER);
//            try {
//                System.out.println("seed " + seed);
//                mg.generate();
////                TETile[][] world = mg.getWorld();
//            } catch (RuntimeException e) {
//                System.out.println(seed);
//            }
//        }
//    }
//
//    @Test
//    public void testMapGenerator() {
//        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, new Random(SEED),
//                NOTHING, FLOOR, WALL, LOCKED_DOOR, BULB, PLAYER);
//        mg.generate();
//        TETile[][] world = mg.getWorld();
//        System.out.println(TETile.toString(world));
//    }
//
//    public static void main(String[] args) {
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//
//        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, new Random(SEED),
//                NOTHING, FLOOR, WALL, LOCKED_DOOR, BULB, PLAYER);
//        mg.generate();
//        TETile[][] world = mg.getWorld();
//
//        ter.renderFrame(world);
//    }
//}
