package byog.Core.Feature;

import byog.Core.Map.Position;
import byog.Core.Map.Rect;
import byog.Core.Map.World;
import byog.Core.Unit.Flower;
import byog.Core.Unit.Guard;
import byog.Core.Unit.Player;
import byog.Core.Unit.Portal;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;

public class LightSource implements Serializable {
    private TETile floorTile, bulbTile;
    private TETile[] lightTiles;

    private ArrayList<Position> bulbs;
    private ArrayList<Rect> rects;
    private Position[][][] lights; // lights -> light -> ps for each r
    private int switches, lightsOn;

    private Player player;
    private Guard guard;
    private Flower flower;
    private Portal portal;

    public LightSource(TETile floorTile, TETile bulbTile, TETile[] lightTiles) {
        this.floorTile = floorTile;
        this.bulbTile = bulbTile;
        this.lightTiles = lightTiles;

        bulbs = new ArrayList<>();
        rects = new ArrayList<>();
    }

    public void addBulb(Position p, Rect r) {
        bulbs.add(p);
        rects.add(r);
    }

    private void addSidePosition(ArrayList<Position> ps, int x, int y, Rect r) {
        if (x >= r.getMinX() && x <= r.getMaxX()
                && y >= r.getMinY() && y <= r.getMaxY()) {
            ps.add(new Position(x, y));
        }
    }

    public void initialize() {
        switches = bulbs.size();
        lights = new Position[switches][][];
        lightsOn = 0;

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

    public void setUnit(Player p, Guard g, Flower f, Portal pt) {
        this.player = p;
        this.guard = g;
        this.flower = f;
        this.portal = pt;
    }

    private void changeTile(World world, Position p, TETile t) {
        int gi = guard.getIndex(p);
        if (gi >= 0) {
            guard.setOneBackTile(gi, t);
            return;
        }

        int fi = flower.getIndex(p);
        if (fi >= 0) {
            flower.setOneBackTile(fi, t);
            return;
        }

        if (p.equals(portal.getPortal1())) {
            portal.setBackTile1(t);
            return;
        }
        if (p.equals(portal.getPortal2())) {
            portal.setBackTile2(t);
            return;
        }

        if (p.equals(player.getPlayerPos())) {
            // player should be checked after portal 1 and 2
            player.setBackTile(t);
            return;
        }

        world.set(p, t);
    }

    private void turnOnOne(World world, Position[][] light) {
        for (int r = 0; r < lightTiles.length; r++) {
            Position[] ps = light[r];
            for (Position p : ps) {
                changeTile(world, p, lightTiles[r]);
            }
        }
    }

    // hard mode
    public void turnOnAll(World world) {
        for (int i = 0; i < switches; i++) {
            turnOnOne(world, lights[i]);
        }
    }

    public void turnOffAll(World world) {
        for (int i = 0; i < switches; i++) {
            Position[][] light = lights[i];

            // r = 0
            changeTile(world, light[0][0], bulbTile);

            // 1 <= r <= lightTiles.length - 1
            for (int r = 1; r < lightTiles.length; r++) {
                Position[] ps = light[r];
                for (Position p : ps) {
                    changeTile(world, p, floorTile);
                }
            }
        }
    }

    // easy mode
    public void changeWorld(World world) {
        if (lightsOn == switches) {
            turnOffAll(world);
            lightsOn = 0;
        } else {
            turnOnOne(world, lights[lightsOn]);
            lightsOn++;
        }
    }

    // easy mode
    public void removeOneLightShade(World shadeWorld, World world) {
        // shadeWorld is new each time
        removeShadeHelper(shadeWorld, world, lightsOn);
    }

    // hard mode
    public void removeAllLightsShade(World shadeWorld, World world) {
        // shadeWorld is new each time
        removeShadeHelper(shadeWorld, world, switches);
    }

    private void removeShadeHelper(World shadeWorld, World world, int n) {
        for (int i = 0; i < n; i++) {
            Position[][] light = lights[i];
            for (int r = 0; r < lightTiles.length; r++) {
                Position[] ps = light[r];
                for (Position p : ps) {
                    shadeWorld.set(p, world.get(p));
                }
            }
        }
    }
}
