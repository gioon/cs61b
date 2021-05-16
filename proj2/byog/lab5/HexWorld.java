package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static class Position {
        public int x, y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Range {
        public int left, right, up, down;
        public Range(int l, int r, int u, int d) {
            this.left = l;
            this.right = r;
            this.up = u;
            this.down = d;
        }
    }

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static int HexWidth;

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(4);
        switch (tileNum) {
            case 0: return Tileset.FLOWER;
            case 1: return Tileset.GRASS;
            case 2: return Tileset.MOUNTAIN;
            case 3: return Tileset.SAND;
            default: return Tileset.MOUNTAIN;
        }
    }

    private static void setHexWidth(int size) {
        HexWidth = size + (size - 1) * 2;
    }

//    private static void addRow(TETile[][] world, int x, int y, TETile t, int begin, int end) {
//        for (int col = begin; col < end; col++) {
//            world[x + col][y] = t;
////            world[x + col][y] = TETile.colorVariant(t, 255, 255, 255, RANDOM);
//        }
//    }
//
//    public static void addHexagon(TETile[][] world, Position p, int size, TETile t) {
//        for (int row = 0; row < size; row++) {
//            addRow(world, p.x, p.y + row, t, size - 1 - row, size * 2 - 1 + row);
//            addRow(world, p.x, p.y + row + size, t, row, size * 3 - 2 - row);
//        }
//    }
//
//    @Test
//    public void testAddRow() {
//        TETile[][] world = new TETile[WIDTH][HEIGHT];
//        for (int x = 0; x < WIDTH; x += 1) {
//            for (int y = 0; y < HEIGHT; y += 1) {
//                world[x][y] = Tileset.NOTHING;
//            }
//        }
//        addRow(world, 3, 3, Tileset.FLOWER, 2, 3);
//        assertEquals(world[3][3],  Tileset.NOTHING);
//        assertEquals(world[4][3],  Tileset.NOTHING);
//        assertEquals(world[5][3],  Tileset.FLOWER);
//        assertEquals(world[6][3],  Tileset.NOTHING);
//    }

    private static int start(int size, int row) {
        int start;
        if (row < size) {
            start = size - 1 - row;
        } else {
            start = row - size;
        }
        return start;
    }

    private static int width(int size, int row) {
        int width;
        if (row < size) {
            width = size + row * 2;
        } else {
//            int HexWidth = size + (size - 1) * 2;
//            width = HexWidth - (row - size) * 2;
            width = size * 5 - 2 - row * 2;
        }
        return width;
    }

    private static void addRow(TETile[][] world, Position p,
                               int start, int row, int width, TETile t) {
        for (int offset = 0; offset < width; offset++) {
            world[p.x + start + offset][p.y + row] = t;
//            world[p.x + start + offset][p.y + row] = TETile.colorVariant(t, 255, 255, 255, RANDOM);
        }
    }

    public static void addHexagon(TETile[][] world, Position p, int size, TETile t) {
        if (size < 2) {
            System.out.println("No such hexagon!");
            return;
        }

        t = randomTile();
        for (int row = 0; row < size * 2; row++) {
            int start = start(size, row);
            int width = width(size, row);
            addRow(world, p, start, row, width, t);
        }
    }

//    private static Position getNextPosition(Position p, int size) {
//        int HexWidth = size + (size - 1) * 2;
//        int nextX = p.x + HexWidth + size;
//        int nextY = p.y + size;
//
//        if (nextX >= WIDTH) { // out of range
//            nextX = HexWidth - size + 1;
//        } else if (nextX + HexWidth > WIDTH) {
//            nextX = 0;
//        } else {
//            nextY = p.y;
//        }
//        if (nextY + size * 2 > HEIGHT) { // out of range
//            return null;
//        }
//        return new Position(nextX, nextY);
//    }

//    public static void tesselate(TETile[][] world, int size, TETile t) {
//        Position p = new Position(0, 0);
//        addHexagon(world, p, size, t);
//        Object obj = getNextPosition(p, size);
//        while (obj != null) {
//            p = (Position) obj;
//            addHexagon(world, p, size, t);
//            obj = getNextPosition(p, size);
//        }
//    }

    private static Position getReference(Range range, int size) {
        Position ref = new Position(
                (range.right - range.left) / 2 // center
                - (HexWidth + size * 2)
                - (size + HexWidth) * (size - 2), 0);
        return ref;
    }

    private static boolean outOfRange(Position p, Range range, int size) {
        if ((p.x < range.left) ||
                (p.x + HexWidth) > range.right ||
                (p.y + size * 2) > range.up) {
            return true;
        }
        return false;
    }

    private static int tOddStart(int size, int row) {
        int tStart;
        if (row < size) { // size 3: row 0 - index 2, row 1 - index 1, row 2 - index 0
            tStart = size - 1 - row;
        } else { // size 3: row 3 - index 1, row 4 - index 2
            tStart = row - size + 1;
        }
        return tStart;
    }

    private static int tOddWidth(int size, int row) {
        int tWidth;
        if (row < size) { // size 3: row 0 - 1, row 1 - 3, row 2 - 5
            tWidth = row * 2 + 1;
        } else { // size 3: row 3 - 3, row 4 - 1   1 3 5 3 1
            tWidth = (size * 2 - row - 2) * 2 + 1;
        }
        return tWidth;
    }

    private static void tAddOddRow(TETile[][] world, Range range, Position ref,
                                   int tStart, int row, int tWidth, int size, TETile t) {
        for (int offset = 0; offset < tWidth; offset++) {
            Position p = new Position(
                    ref.x + (tStart + offset) * (size + HexWidth),
                    ref.y + row * size * 2
            );
            if (outOfRange(p, range, size)) {
                continue;
            }
            addHexagon(world, p, size, t);
        }
    }

    private static int tEvenStart(int size, int row) {
        int tStart;
        if (row < size - 1) { // size 3: row 0 - index 1, row 1 - index 0
            tStart = size - 2 - row;
        } else { // size 3: row 2 - index 0, row 3 - index 1
            tStart = row - size + 1;
        }
        return tStart;
    }

    private static int tEvenWidth(int size, int row) {
        int tWidth;
        if (row < size - 1) { // size 3: row 0 - 2, row 1 - 4
            tWidth = row * 2 + 2;
        } else { // size 3: row 2 - 4, row 3 - 2
            tWidth = (size * 2 - row - 2) * 2;
        }
        return tWidth;
    }

    private static void tAddEvenRow(TETile[][] world, Range range, Position ref,
                                   int tStart, int row, int tWidth, int size, TETile t) {
        for (int offset = 0; offset < tWidth; offset++) {
            Position p = new Position(
                    ref.x + (tStart + offset) * (size + HexWidth) + (HexWidth - size + 1),
                    ref.y + size + row * size * 2
            );
            if (outOfRange(p, range, size)) {
                continue;
            }
            addHexagon(world, p, size, t);
        }
    }

    public static void tesselate(
            TETile[][] world, Range range, int size, TETile t) {
        if (size < 2) {
            System.out.println("No such hexagon!");
            return;
        }

        setHexWidth(size);
        Position ref = getReference(range, size);

        // max row: size * 2 - 1
        // row count: ((size * 2 - 1) - 1) * 2 + 1 = size * 4 - 3
        // 2-3 (3 - 1) * 2 + 1 = 5
        // 3-5 (5 - 1) * 2 + 1 =9
        for (int row = 0; row < size * 2 - 1; row++) {
            int tOddStart = tOddStart(size, row);
            int tOddWidth = tOddWidth(size, row);
            tAddOddRow(world, range, ref, tOddStart, row, tOddWidth, size, t);
        }
        for (int row = 0; row < size * 2 - 2; row++) {
            int tEvenStart = tEvenStart(size, row);
            int tEvenWidth = tEvenWidth(size, row);
            tAddEvenRow(world, range, ref, tEvenStart, row, tEvenWidth, size, t);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

//        Position p = new Position(0, 0);
//        addHexagon(world, p, 3, Tileset.FLOWER);

//        tesselate(world, 2, randomTile());
//        tesselate(world, 2, Tileset.FLOWER);

        tesselate(world, new Range(0, 20, 30, 0), 3, Tileset.FLOWER);
//        tesselate(world, new Range(0, 30, 30, 0), 3, randomTile());

        ter.renderFrame(world);
    }
}
