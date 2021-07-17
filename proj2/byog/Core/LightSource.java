package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

public class LightSource implements Serializable {
    TETile FLOOR = Tileset.FLOOR;
    TETile BULB = Tileset.BULB;
    TETile[] LIGHTS = Tileset.LIGHTS;

    ArrayList<Position> bulbs;
    ArrayList<Room> rooms;
    Position[][][] lights; // lights -> light -> ps for each r
    int switches;
    int n; // lights on

    public LightSource() {
        bulbs = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    public void addBulb(Position bulb, Room room) {
        bulbs.add(bulb);
        rooms.add(room);
    }

    private void addSidePosition(ArrayList<Position> ps, int x, int y, Room room) {
        if (x >= room.minX && x <= room.maxX
                && y >= room.minY && y <= room.maxY) {
            ps.add(new Position(x, y));
        }
    }

    public void initialize() {
        switches = bulbs.size();
        lights = new Position[switches][][];
        n = 0;

        for (int i = 0; i < switches; i++) {
            int bulbX = bulbs.get(i).x;
            int bulbY = bulbs.get(i).y;
            Room room = rooms.get(i);
            Position[][] light = new Position[LIGHTS.length][];

            // r = 0
            light[0] = new Position[]{bulbs.get(i)};
            // 1 <= r <= LIGHTS.length - 1
            for (int r = 1; r < LIGHTS.length; r++) {
                ArrayList<Position> ps = new ArrayList<>();
                int xrMin = bulbX - r;
                int xrMax = bulbX + r;
                int yrMin = bulbY - r;
                int yrMax = bulbY + r;
                for (int j = 0; j <= r * 2; j++) {
                    // clockwise
                    addSidePosition(ps, xrMin + j, yrMax, room);
                    addSidePosition(ps, xrMax, yrMax - j, room);
                    addSidePosition(ps, xrMax - j, yrMin, room);
                    addSidePosition(ps, xrMin, yrMin + j, room);
                }

                light[r] = ps.toArray(new Position[0]);
            }

            lights[i] = light;
        }
    }

    private void changeTile(TETile[][] world, Position p, TETile tile, Player player, Guard guard) {
        if (world[p.x][p.y] == player.playerTile) {
            player.backTile = tile;
        } else if (world[p.x][p.y] == guard.guardTile) {
            guard.backTile = tile;
        } else {
            world[p.x][p.y] = tile;
        }
    }

    public void change(TETile[][] world, Player player, Guard guard) {
        if (n == switches) {
            for (int i = 0; i < switches; i++) {
                Position[][] light = lights[i];

                // r = 0
                changeTile(world, light[0][0], BULB, player, guard);

                // 1 <= r <= LIGHTS.length - 1
                for (int r = 1; r < LIGHTS.length; r++) {
                    Position[] ps = light[r];
                    for (Position p : ps) {
                        changeTile(world, p, FLOOR, player, guard);
                    }
                }
            }

            n = 0;
        } else {
            Position[][] light = lights[n];
            for (int r = 0; r < LIGHTS.length; r++) {
                Position[] ps = light[r];
                for (Position p : ps) {
                    changeTile(world, p, LIGHTS[r], player, guard);
                }
            }

            n++;
        }
    }
}
