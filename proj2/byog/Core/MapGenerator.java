package byog.Core;

import byog.TileEngine.TETile;

import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {

    /**
     * rooms connected by hallways
     * Your game should be able to handle any positive seed up to 9,223,372,036,854,775,807.
     * The world must be pseudorandomly generated.
     * At least some rooms should be rectangular.
     * Your game must be capable of generating hallways that include turns
     * (or equivalently, straight hallways that intersect).
     * The world should contain a random number of rooms and hallways.
     * The locations of the rooms and hallways should be random.
     * The width and height of rooms should be random.
     * The length of hallways should be random.
     *
     * 方案0：
     * room生成条件：
     * room在生成时不可越出world的边界（1/width-1/height-1/1）
     * room在生成时不可覆盖之前的room和hallway的边界(+1)
     * 流程：
     * 随机生成一个room
     * 在其周围random个格子处生成0-2个nextRoom
     * 用hallway连接room和这些nextRoom
     * 在这些nextRoom的周围random个格子处生成0-2个nextRoom
     * 继续用hallway连接
     * 如此循环，最后在连接后的room和hallway边界上依次铺上wall（如果wall覆盖到room则不覆盖）
     *
     * 方案1：
     * 铺设一条总的hallway，有多条分支hallway，在hallway上用nextPoint选取节点，铺一个room后，用nextPoint选取下一个，再铺
     *
     * 方案2：
     * 随机生成一个点，铺一个room，再随机生成另一个点，用hallway连接，铺另一个room
     *
     * 方案3：
     * 把hallway当成width=1的room，把不规则的形状当成room，room和hallway交替连接
     *
     * 方案4：
     * 随机生成N个room和hallway，把有相交的放到一起作为set，删掉没有相交的outlier
     * 把set里的room和hallway周围铺上wall，如果铺的时候不是NOTHING就不铺（换句话说，把周围非NOTHING的铺上wall）
     * room和hallway生成时，确保比WIDTH和HEIGHT小1，并且整个不越界
     *
     * 方案5：
     * 生成N个连续的room，用nextRoom确保连续
     * 每个room有一个door(branch)出口（结束的room没有）
     * door的求法：原room的
     *
     * 重点在于：
     * 1. 如何表示room？
     * 2. 如何生成room？
     * 3. nextRoom
     * 假设room在生成时最外层是wall内层是floor
     * 有随机N个col，后一个col在前一个col的范围内生成，采用RandomUtils.java里的随机数生成方法
     * 如果room超出WIDTH和HEIGHT，就重新生成
     * 用nextRoom生成room时，只要其对应的格子有一个不是NOTHING(overlap)，就重新生成
     *
     * 先由随机数确定room具体个数n
     * 用rooms保存所有生成的room
     * 用nextRoom（in随机）生成第一个room及其out（用overlap检验）
     * 用nextRoom生成下一个room及其out（由前一个room的out生成下一个room的in，用overlap检验）
     * 根据rooms，用addRooms在world上铺rooms，内部floor和最外层wall(Tileset)
     * 第一个的in和最后一个的out铺wall
     *
     * 先写生成room的方法，再写返回整个world的方法(generate)
     * 最终改进：
     * 为了保证美观，求所有的room的min/max weight/height，在此范围内draw
     * （Game.java里的WIDTH/HEIGHT为默认min/max，在一个范围内再缩小范围）
     *
     * 方案6：
     * MapParameter类存储所有参数
     * 点的生成、room里的floor的生成要设置maxWidth maxHeight（后期再根据random seed改变）
     * 点点相连，先不管wall，先生成room里的floor，全为rectangle
     * 以左下角为base生成room里的floor
     * 第一步：
     * 随机生成点A，再用next随机生成点B（或N个点B），据此在A和B（或N+1点）间，以点A为base生成room里的floor
     * 第二步：
     * 再在点B（或N个点B）上用next随机生成点C（或N个点C），这样递归生成room里的floor
     * 直到生成的点超出边界，停止生成room里的floor
     * 最后再铺wall，铺的时候除非碰到wall或floor，每个格子周围8个格子都要铺
     * room - addFloor - addWall
     *
     * 两个position组成一个room里的floor
     *
     * 方案7（方案6的基础上提出）：
     * room: rectangle hallway
     * room = floor + wall
     *
     * 先生成rectangle（因为比较通用）
     * hallway和rectangle交替生成，rectangle可以是width height均为1的，hallway必须x=x或y=y
     * 交替生成两种点，rectangle base生成N个hallway base，hallway base只能生成1个rectangle base
     * 当前rectangle base和后续（N个）hallway base生成当前rectangle
     * 当前hallway base和后续（1个）rectangle base生成当前hallway width/height=1
     *
     * 生成点时要判断overlap，生成hallway / rectangle时也要判断overlap
     * 据此，hallway有横、竖两种可能的生成方式，需判断是否overlap，若均overlap则停止生成hallway，该branch中止
     * overlap根据floor判断，由于没有wall，需要+1，以确保hallway不会在rectangle的边上
     *
     * 方案8（方案7的基础上提出）：
     * generate方法：
     * （base是rectangle或hallway的start）
     * （1）生成1个rectangle base（确保不要outOfBound）
     * （2）根据（1）的rectangle base生成rectangle（除第1个，都要判断overlap，如果overlap，停止）（确保不要outOfBound）
     * （3）根据（2）的rectangle，在其周围生成N个hallway base（确保不要outOfBound）
     * （4）根据（3）的每个hallway base生成hallway（判断overlap，如果overlap，停止）（确保不要outOfBound）
     * （5）根据（4）的每个hallway，在其周围生成1个rectangle base（确保不要outOfBound），重复（2）-（5）
     * （6）当全部停止时，调用drawFloor和drawWall对TETile[][] world进行操作
     * 注：
     * 边创建base，边创建rectangle hallway，这些是floor
     * 生成rectangle base / hallway base / rectangle / hallway时确保不要outOfBound
     * 生成rectangle / hallway后判断overlap
     *
     * 方法：overlap、drawFloor、drawWall
     * 类：Position、Rectangle、Hallway、Room
     *
     * 方案9（方案8的基础上提出）：
     * generate方法：
     * 1个rectangle可生成N个hallway，1个hallway只可生成1个rectangle
     * （1）生成1个rectangle（生成时确保不要outOfBound）
     * （2）根据（1）的rectangle生成1-4个hallway（生成时确保不要outOfBound，如果无法实现，停止）（生成后判断overlap，如果overlap，尝试attempt次后停止）
     * （3）根据（2）的每个hallway生成1个rectangle（生成时确保不要outOfBound，如果无法实现，停止）（生成后判断overlap，如果overlap，尝试attempt次后停止）
     * （4）根据（3）的每个rectangle生成1-4个hallway（生成时确保不要outOfBound，如果无法实现，停止）（生成后判断overlap，如果overlap，尝试attempt次后停止）
     *  重复（3）-（4）
     * （5）当全部停止时，调用drawBackground drawFloor drawWall对TETile[][] world进行操作
     * （6）随机选取其中1个wall作为door
     * 注：
     * 用base end表示rectangle hallway
     * 创建的rectangle hallway是floor
     * door不能在角落
     *
     * 方法：overlap、drawFloor、drawWall
     * 类：Position、Rectangle、Hallway、Room
     */

    /**
     * Create a Room class and keep a list of all existing rooms during world generation.
     * Write an overlap method for the Room class, and reject any generated room that overlaps an existing one.
     * If you need to return multiple things, make a class that has multiple fields.
     *
     * In my case, I wrote a MapGenerator class, and created a MapVisualTest class that called methods
     * in MapGenerator directly. This avoided need to collect a seed from the user.
     * Afterwards, I wrote playWithInputString to collect the seed from the input string
     * (e.g. “N1234S” gives seed 1234) and pass it to MapGenerator.
     * Our autograder for phase 1 will use playWithInputString only.
     *
     * Emergent approach: For each room, randomly generate neighbor rooms that branch off of the current room.
     * Since a hallway is just a width 1 room, this algorithm is capable of generating turning hallways.
     *
     */

    private int width, height, roomWidth, roomHeight;
    private TETile[][] world;

    private TETile nothing, floor, wall, locked_door;

    private ArrayList<Room> rooms;
    private Random random;

    private int ATTEMPT;

    public MapGenerator(int width, int height, long seed, TETile nothing, TETile floor, TETile wall, TETile locked_door) {
        this.width = width;
        this.height = height;
        this.world = new TETile[width][height];

        this.roomWidth = Math.min(width-2, width / 10);
        this.roomHeight = Math.min(height-2, height / 5);

        this.nothing = nothing;
        this.floor = floor;
        this.wall = wall;
        this.locked_door = locked_door;

        this.rooms = new ArrayList<Room>();
        this.random = new Random(seed);

        this.ATTEMPT = 20;
    }

    public boolean overlap(Room target, Room except) {
        for (Room room: rooms) {
            if (room.equals(except)) {
                continue;
            }
            if (target.maxX < room.minX - 2 || target.minX > room.maxX + 2 ||
                    target.maxY < room.minY - 2 || target.minY > room.maxY + 2) {
                continue;
            }
//            System.out.print(target);
//            System.out.print(" overlaps ");
//            System.out.println(room);
            return true;
        }
        return false;
    }

    public boolean outOfBound(Position p) {
        if (p.x <= 0 || p.x >= width - 1 || p.y <= 0 || p.y >= height - 1) {
            return true;
        }
        return false;
    }

//    public Rectangle generateFirst() {
//        Position firstBase = new Position(random.nextInt(width-2)+1, random.nextInt(height-2)+1);
//        Position firstEnd = new Position(random.nextInt(width-2)+1, random.nextInt(height-2)+1);
//        Rectangle firstRec = new Rectangle(firstBase, firstEnd);
//        rooms.add(firstRec);
//        return firstRec;
//    }

    public Rectangle generateFirst() {
        Position firstBase = new Position(random.nextInt(width-2)+1, random.nextInt(height-2)+1);
        Position firstEnd = new Position(
                Math.min(firstBase.x+random.nextInt(roomWidth),
                firstBase.x+random.nextInt(width-1-firstBase.x)),
                Math.min(firstBase.y+random.nextInt(roomHeight),
                firstBase.y+random.nextInt(height-1-firstBase.y)));

        Rectangle firstRec = new Rectangle(firstBase, firstEnd);
        rooms.add(firstRec);
        return firstRec;
    }

//    public ArrayList<Hallway> generateHallways(Rectangle rec) {
//        // (2)
//        int attempts = ATTEMPT;
//        ArrayList<Hallway> halls = new ArrayList<>();
//
//        while (attempts > 0 && halls.size() <= 1) {
//            attempts--;
//
//            Position base, end;
//            Hallway hall;
//
//            // up
//            if (rec.maxY <= height - 3) {
//                base = new Position(rec.minX+random.nextInt(rec.maxX-rec.minX+1), rec.maxY+1);
//                end = new Position(base.x, base.y+random.nextInt(height-1-base.y));
//                hall = new Hallway(base, end, 0);
//                if (!overlap(hall, rec)) {
//                    halls.add(hall);
//                    rooms.add(hall);
//                }
//            }
//
//            // down
//            if (rec.minY >= 2) {
//                base = new Position(rec.minX+random.nextInt(rec.maxX-rec.minX+1), rec.minY-1);
//                end = new Position(base.x, base.y-random.nextInt(base.y));
//                hall = new Hallway(base, end, 1);
//                if (!overlap(hall, rec)) {
//                    halls.add(hall);
//                    rooms.add(hall);
//                }
//            }
//
//            // left
//            if (rec.minX >= 2) {
//                base = new Position(rec.minX-1, rec.minY+random.nextInt(rec.maxY-rec.minY+1));
//                end = new Position(base.x-random.nextInt(base.x), base.y);
//                hall = new Hallway(base, end, 2);
//                if (!overlap(hall, rec)) {
//                    halls.add(hall);
//                    rooms.add(hall);
//                }
//            }
//
//            // right
//            if (rec.maxX <= width - 3) {
//                base = new Position(rec.maxX+1, rec.minY+random.nextInt(rec.maxY-rec.minY+1));
//                end = new Position(base.x+random.nextInt(width-1-base.x), base.y);
//                hall = new Hallway(base, end, 3);
//                if (!overlap(hall, rec)) {
//                    halls.add(hall);
//                    rooms.add(hall);
//                }
//            }
//        }
//        return halls;
//    }

    public ArrayList<Hallway> generateHallways(Rectangle rec) {
        // (2)
        int attempts = ATTEMPT;
        ArrayList<Hallway> halls = new ArrayList<>();

        while (attempts > 0 && halls.size() <= 1) {
            attempts--;

            Position base, end;
            Hallway hall;

            // up
            if (rec.maxY <= height - 3) {
                base = new Position(rec.minX+random.nextInt(rec.maxX-rec.minX+1), rec.maxY+1);
                end = new Position(base.x,
                        Math.min(base.y+random.nextInt(roomHeight), base.y+random.nextInt(height-1-base.y)));
                hall = new Hallway(base, end, 0);
                if (!overlap(hall, rec)) {
                    halls.add(hall);
                    rooms.add(hall);
                }
            }

            // down
            if (rec.minY >= 2) {
                base = new Position(rec.minX+random.nextInt(rec.maxX-rec.minX+1), rec.minY-1);
                end = new Position(base.x,
                        Math.max(base.y-random.nextInt(roomHeight), base.y-random.nextInt(base.y)));
                hall = new Hallway(base, end, 1);
                if (!overlap(hall, rec)) {
                    halls.add(hall);
                    rooms.add(hall);
                }
            }

            // left
            if (rec.minX >= 2) {
                base = new Position(rec.minX-1, rec.minY+random.nextInt(rec.maxY-rec.minY+1));
                end = new Position(Math.max(base.x-random.nextInt(roomWidth),
                        base.x-random.nextInt(base.x)), base.y);
                hall = new Hallway(base, end, 2);
                if (!overlap(hall, rec)) {
                    halls.add(hall);
                    rooms.add(hall);
                }
            }

            // right
            if (rec.maxX <= width - 3) {
                base = new Position(rec.maxX+1, rec.minY+random.nextInt(rec.maxY-rec.minY+1));
                end = new Position(Math.min(base.x+random.nextInt(roomWidth),
                        base.x+random.nextInt(width-1-base.x)), base.y);
                hall = new Hallway(base, end, 3);
                if (!overlap(hall, rec)) {
                    halls.add(hall);
                    rooms.add(hall);
                }
            }
        }
        return halls;
    }

//    public ArrayList<Rectangle> generateRectangles(ArrayList<Hallway> halls) {
//        ArrayList<Rectangle> recs = new ArrayList<>();
//        Position base, end;
//        Rectangle rec;
//        for (Hallway hall: halls) {
//            switch (hall.direction) {
//                case 0: // up
//                    if (hall.end.y >= height - 2) {
//                        continue;
//                    }
//                    base = new Position(hall.end.x - random.nextInt(hall.end.x), hall.end.y + 1);
//                    end = new Position(hall.end.x + random.nextInt(width - 1 - hall.end.x),
//                            base.y + random.nextInt(height - 1 - base.y));
//                    break;
//                case 1: // down
//                    if (hall.end.y <= 1) {
//                        continue;
//                    }
//                    base = new Position(hall.end.x - random.nextInt(hall.end.x), hall.end.y - 1);
//                    end = new Position(hall.end.x + random.nextInt(width - 1 - hall.end.x),
//                            base.y - random.nextInt(base.y));
//                    break;
//                case 2: // left
//                    if (hall.end.x <= 1) {
//                        continue;
//                    }
//                    base = new Position(hall.end.x - 1, hall.end.y - random.nextInt(hall.end.y));
//                    end = new Position(base.x - random.nextInt(base.x),
//                            hall.end.y + random.nextInt(height - 1 - hall.end.y));
//                    break;
//                case 3: // right
//                default:
//                    if (hall.end.x >= width - 2) {
//                        continue;
//                    }
//                    base = new Position(hall.end.x + 1, hall.end.y - random.nextInt(hall.end.y));
//                    end = new Position(base.x + random.nextInt(width - 1 - base.x),
//                            hall.end.y + random.nextInt(height - 1 - hall.end.y));
//            }
//
//            rec = new Rectangle(base, end);
//            if (!overlap(rec, hall)) {
//                recs.add(rec);
//                rooms.add(rec);
//            }
//        }
//        return recs;
//    }

    public ArrayList<Rectangle> generateRectangles(ArrayList<Hallway> halls) {
        ArrayList<Rectangle> recs = new ArrayList<>();
        Position base, end;
        Rectangle rec;
        for (Hallway hall: halls) {
            switch (hall.direction) {
                case 0: // up
                    if (hall.end.y >= height - 2) {
                        continue;
                    }
                    base = new Position(Math.max(hall.end.x-random.nextInt(roomWidth),
                            hall.end.x-random.nextInt(hall.end.x)), hall.end.y+1);
                    end = new Position(Math.min(hall.end.x+random.nextInt(roomWidth),
                            hall.end.x+random.nextInt(width-1-hall.end.x)),
                            Math.min(base.y+random.nextInt(roomHeight), base.y+random.nextInt(height-1-base.y)));
                    break;
                case 1: // down
                    if (hall.end.y <= 1) {
                        continue;
                    }
                    base = new Position(Math.max(hall.end.x-random.nextInt(roomWidth),
                            hall.end.x-random.nextInt(hall.end.x)), hall.end.y-1);
                    end = new Position(Math.min(hall.end.x+random.nextInt(roomWidth),
                            hall.end.x+random.nextInt(width-1-hall.end.x)),
                            Math.max(base.y-random.nextInt(roomHeight), base.y-random.nextInt(base.y)));
                    break;
                case 2: // left
                    if (hall.end.x <= 1) {
                        continue;
                    }
                    base = new Position(hall.end.x-1,
                            Math.max(hall.end.y-random.nextInt(roomHeight), hall.end.y-random.nextInt(hall.end.y)));
                    end = new Position(Math.max(base.x-random.nextInt(roomWidth), base.x-random.nextInt(base.x)),
                            Math.min(hall.end.y+random.nextInt(roomHeight),
                            hall.end.y+random.nextInt(height-1-hall.end.y)));
                    break;
                case 3: // right
                default:
                    if (hall.end.x >= width - 2) {
                        continue;
                    }
                    base = new Position(hall.end.x+1,
                            Math.max(hall.end.y-random.nextInt(roomHeight), hall.end.y-random.nextInt(hall.end.y)));
                    end = new Position(Math.min(base.x+random.nextInt(roomWidth),
                            base.x+random.nextInt(width-1-base.x)),
                            Math.min(hall.end.y+random.nextInt(roomHeight),
                            hall.end.y+random.nextInt(height-1-hall.end.y)));
            }

            rec = new Rectangle(base, end);
            if (!overlap(rec, hall)) {
                recs.add(rec);
                rooms.add(rec);
            }
        }
        return recs;
    }

    public void generateHelper(Rectangle rec) {
        // (2)
//        System.out.println("Step 2: Generating hallways");
        ArrayList<Hallway> halls = generateHallways(rec);
        // (3)
//        System.out.println("Step 3: Generateing rectangles");
        ArrayList<Rectangle> recs = generateRectangles(halls);
        // (4)
//        System.out.println("Step 4: Next loop");
        for (Rectangle r: recs) {
            generateHelper(r);
        }
    }

    public void initialize() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = nothing;
            }
        }
    }

    public void drawFloor() {
        for (Room room: rooms) {
            for (int x = room.minX; x <= room.maxX; x++) {
                for (int y = room.minY; y <= room.maxY; y++) {
                    world[x][y] = floor;
                }
            }
        }
    }

    public void drawWall() {
        for (Room room: rooms) {
            for (int x = room.minX-1, y = room.minY-1; x <= room.maxX+1; x++) {
                if (!world[x][y].equals(floor)) {
                    world[x][y] = wall;
                }
            }
            for (int x = room.minX-1, y = room.maxY+1; x <= room.maxX+1; x++) {
                if (!world[x][y].equals(floor)) {
                    world[x][y] = wall;
                }
            }
            for (int x = room.minX-1, y = room.minY; y <= room.maxY; y++) {
                if (!world[x][y].equals(floor)) {
                    world[x][y] = wall;
                }
            }
            for (int x = room.maxX+1, y = room.minY; y <= room.maxY; y++) {
                if (!world[x][y].equals(floor)) {
                    world[x][y] = wall;
                }
            }
        }
    }

    public void drawDoor() {
        while (true) {
            random = new Random();
            Room room = rooms.get(random.nextInt(rooms.size()));
            if (room instanceof Rectangle) {
                int doorX, doorY;
                switch (random.nextInt(4)) {
                    case 0: // up
                        doorX = room.minX+random.nextInt(room.maxX-room.minX+1);
                        doorY = room.maxY+1;
                        break;
                    case 1: // down
                        doorX = room.minX+random.nextInt(room.maxX-room.minX+1);
                        doorY = room.minY-1;
                        break;
                    case 2: // left
                        doorX = room.minX-1;
                        doorY = room.minY+random.nextInt(room.maxY-room.minY+1);
                        break;
                    case 3: // right
                    default:
                        doorX = room.maxX+1;
                        doorY = room.minY+random.nextInt(room.maxY-room.minY+1);
                        break;
                }
                if (world[doorX][doorY] == wall) {
                    world[doorX][doorY] = locked_door;
                    break;
                }
            }
        }
    }

    public TETile[][] generate() {
        // world: 0~width 0~height
        // floor: 1~width-1 1~height-1

        // (1)
//        System.out.println("Step 1: Generating firstRec");
        Rectangle firstRec = generateFirst();

        // (2)-(4)
        generateHelper(firstRec);

        // (5)
//        System.out.println("Step 5: Drawing");
        initialize();
        drawFloor();
        drawWall();

        // (6)
//        System.out.println("Step 6: Adding the door");
        drawDoor();

//        System.out.println("Finished");
        return world;
    }

    public TETile[][] getWorld() {
        return world;
    }
}
