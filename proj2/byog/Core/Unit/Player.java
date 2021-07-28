package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Player implements Serializable {
    private Position playerPos;
    private int stepNum, power, speed, health, shieldThresh, powerAward, speedUp;
    private TETile backTile, playerTile, wallTile, nailTile;
    private boolean shield;

    private Door door;
    private Guard guard;
    private Flower flower;
    private Portal portal;

    public Player(Position playerPos, TETile backTile,
                  TETile playerTile, TETile wallTile, TETile nailTile) {
        this.playerPos = playerPos;
        this.backTile = backTile;
        this.playerTile = playerTile;
        this.wallTile = wallTile;
        this.nailTile = nailTile;

        this.shield = false;
        this.stepNum = 0;
        this.power = 0;
        this.speed = 1;
    }

    public void setAttribute(int h, int st, int pa, int su) {
        this.health = h;
        this.shieldThresh = st;
        this.powerAward = pa;
        this.speedUp = su;
    }

    public void setUnit(Door d, Guard g, Flower f, Portal pt) {
        this.door = d;
        this.guard = g;
        this.flower = f;
        this.portal = pt;
    }

    public void setBackTile(TETile backTile) {
        this.backTile = backTile;
    }

    public Position getPlayerPos() {
        return playerPos;
    }

    public int getHealth() {
        return health;
    }

    public TETile getPlayerTile() {
        return playerTile;
    }

    public TETile getBackTile() {
        return backTile;
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

    public int move(TETile[][] world, char c) {
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

            if (world[nextX][nextY].equals(door.getLockedDoorTile())
                    || world[nextX][nextY].equals(wallTile)) {
                changePower();
                return 0;
            }
            if (world[nextX][nextY].equals(door.getUnlockedDoorTile())) {
                return 1;
            }
            if (world[nextX][nextY].equals(nailTile)) {
                changePower();
                if (!shield) {
                    return injure();
                }
                shield = false;
                return 0;
            }

            world[playerPos.getX()][playerPos.getY()] = backTile;
            Position p = new Position(nextX, nextY);
            if (world[nextX][nextY].equals(guard.getGuardTile())) {
                if (!shield) {
                    return injure();
                }
                shield = false;
                stepNum--;
                backTile = guard.getOneBackTile(p);
                guard.delGuard(p);
            } else if (world[nextX][nextY].equals(flower.getFlowerTile())) {
                power += powerAward;
                backTile = flower.getOneBackTile(p);
                flower.delFlower(p);
            } else if (p.equals(portal.getPortal1()) && portal.getPortal2() != null) {
                nextX = portal.getPortal2().getX();
                nextY = portal.getPortal2().getY();
                backTile = world[nextX][nextY];
            } else if (p.equals(portal.getPortal2())) {
                nextX = portal.getPortal1().getX();
                nextY = portal.getPortal1().getY();
                backTile = world[nextX][nextY];
            } else {
                backTile = world[nextX][nextY];
            }

            if (guard.isClear()) {
                door.change(world);
            }
            world[nextX][nextY] = playerTile;
            playerPos = new Position(nextX, nextY);
            if (!shield) {
                stepNum++;
            }
            if (stepNum == shieldThresh) {
                shield = true;
                stepNum = 0;
            }
        }
        changePower();
        return 0;
    }
}
