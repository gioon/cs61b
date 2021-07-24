package byog.Core.Feature;

import byog.Core.Map.Position;
import byog.Core.Map.Rect;
import byog.Core.Unit.Guard;
import byog.Core.Unit.Player;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;

public class LightSource implements Serializable {
    private TETile floorTile;
    private TETile bulbTile;
    private TETile[] lightTiles;

    private ArrayList<Position> bulbs;
    private ArrayList<Rect> rects;
    private Position[][][] lights; // lights -> light -> ps for each r
    private int switches;
    private int n; // lights on

    public LightSource(TETile floorTile, TETile bulbTile, TETile[] lightTiles) {
        this.floorTile = floorTile;
        this.bulbTile = bulbTile;
        this.lightTiles = lightTiles;

        bulbs = new ArrayList<>();
        rects = new ArrayList<>();
    }

    public void addBulb(Position bulb, Rect rect) {
        bulbs.add(bulb);
        rects.add(rect);
    }

    private void addSidePosition(ArrayList<Position> ps, int x, int y, Rect rect) {
        if (x >= rect.getMinX() && x <= rect.getMaxX()
                && y >= rect.getMinY() && y <= rect.getMaxY()) {
            ps.add(new Position(x, y));
        }
    }

    public void initialize() {
        switches = bulbs.size();
        lights = new Position[switches][][];
        n = 0;

        for (int i = 0; i < switches; i++) {
            int bulbX = bulbs.get(i).getX();
            int bulbY = bulbs.get(i).getY();
            Rect rect = rects.get(i);
            Position[][] light = new Position[lightTiles.length][];

            // r = 0
            light[0] = new Position[]{bulbs.get(i)};
            // 1 <= r <= lightTiles.length - 1
            for (int r = 1; r < lightTiles.length; r++) {
                ArrayList<Position> ps = new ArrayList<>();
                int xrMin = bulbX - r;
                int xrMax = bulbX + r;
                int yrMin = bulbY - r;
                int yrMax = bulbY + r;
                for (int j = 0; j <= r * 2; j++) {
                    // clockwise
                    addSidePosition(ps, xrMin + j, yrMax, rect);
                    addSidePosition(ps, xrMax, yrMax - j, rect);
                    addSidePosition(ps, xrMax - j, yrMin, rect);
                    addSidePosition(ps, xrMin, yrMin + j, rect);
                }

                light[r] = ps.toArray(new Position[0]);
            }

            lights[i] = light;
        }
    }

    private void changeTile(TETile[][] world, Position p, TETile tile, Player player, Guard guard) {
        if (world[p.getX()][p.getY()].equals(player.getPlayerTile())) {
            player.setBackTile(tile);
        } else if (world[p.getX()][p.getY()].equals(guard.getGuardTile())) {
            guard.setBackTile(tile);
        } else {
            world[p.getX()][p.getY()] = tile;
        }
    }

    public void change(TETile[][] world, Player player, Guard guard) {
        if (n == switches) {
            for (int i = 0; i < switches; i++) {
                Position[][] light = lights[i];

                // r = 0
                changeTile(world, light[0][0], bulbTile, player, guard);

                // 1 <= r <= lightTiles.length - 1
                for (int r = 1; r < lightTiles.length; r++) {
                    Position[] ps = light[r];
                    for (Position p : ps) {
                        changeTile(world, p, floorTile, player, guard);
                    }
                }
            }

            n = 0;
        } else {
            Position[][] light = lights[n];
            for (int r = 0; r < lightTiles.length; r++) {
                Position[] ps = light[r];
                for (Position p : ps) {
                    changeTile(world, p, lightTiles[r], player, guard);
                }
            }

            n++;
        }
    }
}
