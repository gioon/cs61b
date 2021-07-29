package byog.Core.Unit;

import byog.Core.Feature.LightSource;
import byog.Core.Feature.Shade;
import byog.Core.Map.Position;
import byog.Core.Map.World;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Player implements Serializable {
    private Position playerPos;
    private int stepNum, power, speed, health, shieldThresh, powerAward, speedUp;
    private TETile backTile, playerTile, wallTile, nailTile;
    private boolean shield, hardMode;

    private Door door;
    private LightSource lightSource; // hard mode
    private Guard guard;
    private Flower flower;
    private Portal portal;
    private Shade shade; // hard mode

    private boolean checkClear; // hard mode

    public Player(Position playerPos, TETile backTile,
                  TETile playerTile, TETile wallTile, TETile nailTile, boolean hardMode) {
        this.playerPos = playerPos;
        this.backTile = backTile;
        this.playerTile = playerTile;
        this.wallTile = wallTile;
        this.nailTile = nailTile;
        this.hardMode = hardMode;

        this.stepNum = 0;
        this.shield = false;
        this.power = 0;
        this.speed = 1;

        this.checkClear = true; // hard mode
    }

    public void setAttribute(int h, int st, int pa, int su) {
        this.health = h;
        this.shieldThresh = st;
        this.powerAward = pa;
        this.speedUp = su;
    }

    public void setUnit(Door d, LightSource l, Guard g, Flower f, Portal pt, Shade s) {
        this.door = d;
        this.lightSource = l; // hard mode
        this.guard = g;
        this.flower = f;
        this.portal = pt;
        this.shade = s; // hard mode
    }

    public void setBackTile(TETile backTile) {
        this.backTile = backTile;
    }

    public Position getPlayerPos() {
        return playerPos;
    }

    public TETile getBackTile() {
        return backTile;
    }

    public int getHealth() {
        return health;
    }

    public int getStepNum() {
        return stepNum;
    }

    public String getShield() {
        if (shield) {
            return "⛊";
        } else {
            return "⛉";
        }
    }

    public int getPower() {
        return power;
    }

    public String getSpeed() {
        if (speed == speedUp) {
            return "☻";
        } else {
            return "☺";
        }
    }

    public void changeSpeed() {
        if (speed == 1 && power > 0) {
            speed = speedUp;
        } else {
            speed = 1;
        }
    }

    private void changePower() {
        if (speed != 1) {
            power--;
            if (power == 0) {
                speed = 1;
            }
        }
    }

    private int injure() {
        health--;
        if (health <= 0) {
            return 2;
        }
        return 0;
    }

    public int move(World world, char c) {
        for (int i = 0; i < speed; i++) {
            int nextX = playerPos.getX();
            int nextY = playerPos.getY();
            switch (c) {
                case 'w':
                    nextY++;
                    break;
                case 'a':
                    nextX--;
                    break;
                case 's':
                    nextY--;
                    break;
                case 'd':
                    nextX++;
                    break;
                default:
                    return 0;
            }

            Position p = new Position(nextX, nextY);
            if (world.get(p).equals(wallTile)) {
                changePower();
                return 0;
            }
            if (world.get(p).equals(nailTile)) {
                changePower();
                if (!shield) {
                    return injure();
                }
                shield = false;
                return 0;
            }
            if (p.equals(door.getDoorPos())) {
                if (door.isOpen()) {
                    return 1;
                }
                changePower();
                return 0;
            }

            int gi = guard.getIndex(p);
            int fi = flower.getIndex(p);
            if (gi >= 0) {
                if (!shield) {
                    return injure();
                }
                shield = false;
                stepNum--;
                world.set(p, guard.getOneBackTile(gi));
                guard.delGuard(gi);
            } else if (fi >= 0) {
                power += powerAward;
                world.set(p, flower.getOneBackTile(fi));
                flower.delFlower(fi);
            } else if (p.equals(portal.getPortal1()) && portal.getPortal2() != null) {
                p = portal.getPortal2();
            } else if (p.equals(portal.getPortal2())) {
                p = portal.getPortal1();
            }

            // now we can move forward / transport
            world.set(playerPos, backTile);
            backTile = world.get(p);
            world.set(p, playerTile);
            playerPos = p;
            // the above statements shouldn't be put after moveHelper - hard mode
            // player - changeTile - "playerPos = p;", otherwise the light would be wrong
            // lightSource would call lightSource - changeTile - world.set(p, t);
            // instead of changeTile - player.setBackTile
            moveHelper(world);
        }
        changePower();
        return 0;
    }

    private void moveHelper(World world) {
        if (hardMode) {
            // hard mode
            if (checkClear && guard.isClear()) {
                checkClear = false;
                door.open(world);
                shade.change();
                lightSource.turnOffAll(world);
            }
        } else {
            // easy mode
            if (guard.isClear()) {
                door.open(world);
            }
        }

        if (!shield) {
            stepNum++;
        }
        if (stepNum == shieldThresh) {
            shield = true;
            stepNum = 0;
        }
    }
}
