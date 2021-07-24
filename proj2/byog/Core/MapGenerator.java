package byog.Core;

import byog.Core.Feature.LightSource;
import byog.Core.Feature.Shade;
import byog.Core.Map.Hallway;
import byog.Core.Map.Rect;
import byog.Core.Map.Room;
import byog.Core.Unit.Door;
import byog.Core.Unit.Guard;
import byog.Core.Unit.Player;
import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {
    private final TETile NOTHING = Parameters.NOTHING;
    private final TETile FLOOR = Parameters.FLOOR;
    private final TETile WALL = Parameters.WALL;
    private final TETile LOCKED_DOOR = Parameters.LOCKED_DOOR;
    private final TETile UNLOCKED_DOOR = Parameters.UNLOCKED_DOOR;
    private final TETile PLAYER = Parameters.PLAYER;
    private final TETile BULB = Parameters.BULB;
    private final TETile[] LIGHTS = Parameters.LIGHTS;
    private final TETile GUARD = Parameters.GUARD;
    private final int ATTEMPT = Parameters.ATTEMPT;
    private final int HEALTH = Parameters.HEALTH;

    private final int width;
    private final int height;
    private final int rectWidth;
    private final int rectHeight;

    private Random random;
    private TETile[][] world;
    private Door door;
    private Player player;
    private LightSource lightSource;
    private Guard guard;
    private Shade shade;

    private ArrayList<Rect> rects;

    public MapGenerator(int width, int height, Random random) {
        this.width = width;
        this.height = height;
        this.rectWidth = Math.min(width - 2, width / 5);
        this.rectHeight = Math.min(height - 2, height / 3);

        this.random = random;
        this.world = new TETile[width][height];

        this.rects = new ArrayList<>();
    }

    private boolean notOverlap(Rect target, Rect except) {
        for (Rect rect : rects) {
            if (rect.equals(except)) {
                continue;
            }
            if (target.getMaxX() < rect.getMinX() - 2
                    || target.getMinX() > rect.getMaxX() + 2
                    || target.getMaxY() < rect.getMinY() - 2
                    || target.getMinY() > rect.getMaxY() + 2) {
                continue;
            }
            return false;
        }
        return true;
    }

    private Room genFirst() {
        Position firstBase = new Position(
                random.nextInt(width - 2) + 1,
                random.nextInt(height - 2) + 1);
        Position firstEnd = new Position(
                Math.min(firstBase.getX() + random.nextInt(rectWidth),
                        firstBase.getX() + random.nextInt(width - 1 - firstBase.getX())),
                Math.min(firstBase.getY() + random.nextInt(rectHeight),
                        firstBase.getY() + random.nextInt(height - 1 - firstBase.getY())));

        Room firstRoom = new Room(firstBase, firstEnd);
        rects.add(firstRoom);
        return firstRoom;
    }

    private ArrayList<Hallway> genHallways(Room room) {
        // (2)
        int attempts = ATTEMPT;
        ArrayList<Hallway> halls = new ArrayList<>();
        
        int minX = room.getMinX();
        int maxX = room.getMaxX();
        int minY = room.getMinY();
        int maxY = room.getMaxY();

        while (attempts > 0 && halls.size() < 1) {
            attempts--;

            Position base, end;
            Hallway hall;

            // up
            if (maxY <= height - 3) {
                base = new Position(
                        minX + random.nextInt(maxX - minX + 1),
                        maxY + 1);
                end = new Position(base.getX(),
                        Math.min(base.getY() + random.nextInt(rectHeight),
                                base.getY() + random.nextInt(height - 1 - base.getY())));
                hall = new Hallway(base, end, 0);
                if (notOverlap(hall, room)) {
                    halls.add(hall);
                }
            }

            // down
            if (minY >= 2) {
                base = new Position(
                        minX + random.nextInt(maxX - minX + 1),
                        minY - 1);
                end = new Position(base.getX(),
                        Math.max(base.getY() - random.nextInt(rectHeight),
                                base.getY() - random.nextInt(base.getY())));
                hall = new Hallway(base, end, 1);
                if (notOverlap(hall, room)) {
                    halls.add(hall);
                }
            }

            // left
            if (minX >= 2) {
                base = new Position(minX - 1,
                        minY + random.nextInt(maxY - minY + 1));
                end = new Position(Math.max(base.getX() - random.nextInt(rectWidth),
                        base.getX() - random.nextInt(base.getX())), base.getY());
                hall = new Hallway(base, end, 2);
                if (notOverlap(hall, room)) {
                    halls.add(hall);
                }
            }

            // right
            if (maxX <= width - 3) {
                base = new Position(maxX + 1,
                        minY + random.nextInt(maxY - minY + 1));
                end = new Position(Math.min(base.getX() + random.nextInt(rectWidth),
                        base.getX() + random.nextInt(width - 1 - base.getX())), base.getY());
                hall = new Hallway(base, end, 3);
                if (notOverlap(hall, room)) {
                    halls.add(hall);
                }
            }

            rects.addAll(halls);
        }
        return halls;
    }

    private ArrayList<Room> genRooms(ArrayList<Hallway> halls) {
        ArrayList<Room> rooms = new ArrayList<>();
        Position base, end;
        Room room;
        
        for (Hallway hall: halls) {
            int eX = hall.getEnd().getX();
            int eY = hall.getEnd().getY();
            
            switch (hall.getDirection()) {
                case 0: // up
                    if (eY >= height - 2) {
                        continue;
                    }
                    base = new Position(Math.max(eX - random.nextInt(rectWidth),
                            eX - random.nextInt(eX)), eY + 1);
                    end = new Position(Math.min(eX + random.nextInt(rectWidth),
                            eX + random.nextInt(width - 1 - eX)),
                            Math.min(base.getY() + random.nextInt(rectHeight),
                                    base.getY() + random.nextInt(height - 1 - base.getY())));
                    break;
                case 1: // down
                    if (eY <= 1) {
                        continue;
                    }
                    base = new Position(Math.max(eX - random.nextInt(rectWidth),
                            eX - random.nextInt(eX)), eY - 1);
                    end = new Position(Math.min(eX + random.nextInt(rectWidth),
                            eX + random.nextInt(width - 1 - eX)),
                            Math.max(base.getY() - random.nextInt(rectHeight),
                                    base.getY() - random.nextInt(base.getY())));
                    break;
                case 2: // left
                    if (eX <= 1) {
                        continue;
                    }
                    base = new Position(eX - 1,
                            Math.max(eY - random.nextInt(rectHeight),
                                    eY - random.nextInt(eY)));
                    end = new Position(Math.max(base.getX() - random.nextInt(rectWidth),
                            base.getX() - random.nextInt(base.getX())),
                            Math.min(eY + random.nextInt(rectHeight),
                                    eY + random.nextInt(height - 1 - eY)));
                    break;
                case 3: // right
                default:
                    if (eX >= width - 2) {
                        continue;
                    }
                    base = new Position(eX + 1,
                            Math.max(eY - random.nextInt(rectHeight),
                                    eY - random.nextInt(eY)));
                    end = new Position(Math.min(base.getX() + random.nextInt(rectWidth),
                            base.getX() + random.nextInt(width - 1 - base.getX())),
                            Math.min(eY + random.nextInt(rectHeight),
                                    eY + random.nextInt(height - 1 - eY)));
            }

            room = new Room(base, end);
            if (notOverlap(room, hall)) {
                rooms.add(room);
                rects.add(room);
            }
        }
        return rooms;
    }

    private void genHelper(Room room) {
        // (2)
//        System.out.println("Step 2: Generating hallways");
        ArrayList<Hallway> halls = genHallways(room);
        // (3)
//        System.out.println("Step 3: Generating rooms");
        ArrayList<Room> rooms = genRooms(halls);
        // (4)
//        System.out.println("Step 4: Next loop");
        for (Room r: rooms) {
            genHelper(r);
        }
    }

    private void initialize() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = NOTHING;
            }
        }
    }

    private void genFloor() {
        for (Rect rect : rects) {
            for (int x = rect.getMinX(); x <= rect.getMaxX(); x++) {
                for (int y = rect.getMinY(); y <= rect.getMaxY(); y++) {
                    world[x][y] = FLOOR;
                }
            }
        }
    }

    private void genWall() {
        for (Rect rect : rects) {
            int minX = rect.getMinX();
            int maxX = rect.getMaxX();
            int minY = rect.getMinY();
            int maxY = rect.getMaxY();
            
            for (int x = minX - 1, y = minY - 1; x <= maxX + 1; x++) {
                if (!world[x][y].equals(FLOOR)) {
                    world[x][y] = WALL;
                }
            }
            for (int x = minX - 1, y = maxY + 1; x <= maxX + 1; x++) {
                if (!world[x][y].equals(FLOOR)) {
                    world[x][y] = WALL;
                }
            }
            for (int x = minX - 1, y = minY; y <= maxY; y++) {
                if (!world[x][y].equals(FLOOR)) {
                    world[x][y] = WALL;
                }
            }
            for (int x = maxX + 1, y = minY; y <= maxY; y++) {
                if (!world[x][y].equals(FLOOR)) {
                    world[x][y] = WALL;
                }
            }
        }
    }

    private void genDoor(Room room) {
        int doorX, doorY;
        int minX = room.getMinX();
        int maxX = room.getMaxX();
        int minY = room.getMinY();
        int maxY = room.getMaxY();
        
        while (true) {
            switch (random.nextInt(4)) {
                case 0: // up
                    doorX = minX + random.nextInt(maxX - minX + 1);
                    doorY = maxY + 1;
                    break;
                case 1: // down
                    doorX = minX + random.nextInt(maxX - minX + 1);
                    doorY = minY - 1;
                    break;
                case 2: // left
                    doorX = minX - 1;
                    doorY = minY + random.nextInt(maxY - minY + 1);
                    break;
                case 3: // right
                default:
                    doorX = maxX + 1;
                    doorY = minY + random.nextInt(maxY - minY + 1);
                    break;
            }
            if (world[doorX][doorY].equals(WALL)) {
                // avoid floor
                world[doorX][doorY] = LOCKED_DOOR;
                door = new Door(new Position(doorX, doorY), LOCKED_DOOR, UNLOCKED_DOOR);
                break;
            }
        }
    }

    private void genPlayer(Room room) {
        int playerX, playerY;
        playerX = room.getMinX()
                + random.nextInt(room.getMaxX() - room.getMinX() + 1);
        playerY = room.getMinY()
                + random.nextInt(room.getMaxY() - room.getMinY() + 1);
        world[playerX][playerY] = PLAYER;
        player = new Player(new Position(playerX, playerY), HEALTH, FLOOR, PLAYER, WALL);
    }

    private void genLightSource() {
        lightSource = new LightSource(FLOOR, BULB, LIGHTS);
        for (Rect rect : rects) {
            int minX = rect.getMinX();
            int maxX = rect.getMaxX();
            int minY = rect.getMinY();
            int maxY = rect.getMaxY();
            
            if (rect instanceof Room
                    && (maxX - minX) > 2 && (maxY - minY) > 2) {
                int bulbX, bulbY;
                while (true) {
                    bulbX = minX + random.nextInt(maxX - minX + 1);
                    bulbY = minY + random.nextInt(maxY - minY + 1);
                    if (world[bulbX][bulbY].equals(FLOOR)) {
                        // avoid player
                        world[bulbX][bulbY] = BULB;
                        lightSource.addBulb(new Position(bulbX, bulbY), rect);
                        break;
                    }
                }
            }
        }
        lightSource.initialize();
    }

    private void genGuard(Room room) {
        int guardX, guardY;
        while (true) {
            guardX = room.getMinX()
                    + random.nextInt(room.getMaxX() - room.getMinX() + 1);
            guardY = room.getMinY()
                    + random.nextInt(room.getMaxY() - room.getMinY() + 1);
            if (world[guardX][guardY].equals(FLOOR)) {
                world[guardX][guardY] = GUARD;
                guard = new Guard(new Position(guardX, guardY), FLOOR, GUARD);
                break;
            }
        }
    }

    private void genShade() {
        shade = new Shade(width, height, NOTHING);
    }
    
    private Room getLastRoom() {
        int i = rects.size() - 1;
        while (!(rects.get(i) instanceof Room) && i > 0) {
            i--;
        }
        return (Room) rects.get(i);
    }

    public void generate() {
        // world: 0~width 0~height
        // floor: 1~width-1 1~height-1

        // (1)
//        System.out.println("Step 1: Generating firstRoom");
        Room firstRoom = genFirst();

        // (2)-(4)
        genHelper(firstRoom);

        // (5)
//        System.out.println("Step 5: Drawing");
        initialize();
        genFloor();
        genWall();

        Room lastRoom = getLastRoom();
        
        // (6)
//        System.out.println("Step 6: Adding the door");
        genDoor(lastRoom);

        // (7)
//        System.out.println("Step 8: Adding the player");
        genPlayer(firstRoom);

        // (8)
        genLightSource();
//        System.out.println("Step 7: Adding the lightSource");

        // (9)
//        System.out.println("Step 9: Adding the guard");
        genGuard(lastRoom);

        // (10)
//        System.out.println("Step 10: Adding the shade");
        genShade();

//        System.out.println("Finished");
    }

    public TETile[][] getWorld() {
        return world;
    }
    public Door getDoor() {
        return door;
    }
    public Player getPlayer() {
        return player;
    }
    public LightSource getLightSource() {
        return lightSource;
    }
    public Guard getGuard() {
        return guard;
    }
    public Shade getShade() {
        return shade;
    }
}
