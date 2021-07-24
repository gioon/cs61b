package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Player implements Serializable {
    private Position playerPos;
    private int health;
    private TETile backTile, playerTile, wallTile;

    public Player(Position playerPos, int health, TETile backTile,
                  TETile playerTile, TETile wallTile) {
        this.playerPos = playerPos;
        this.health = health;
        this.backTile = backTile;
        this.playerTile = playerTile;
        this.wallTile = wallTile;
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

    public void setBackTile(TETile backTile) {
        this.backTile = backTile;
    }

    public void move(TETile[][] world, Door door, Guard guard, char c) {
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
                return;
        }

        if (world[nextX][nextY].equals(door.getLockedDoorTile())
                || world[nextX][nextY].equals(wallTile)) {
            return;
        }

        if (world[nextX][nextY].equals(door.getUnlockedDoorTile())) {
//            System.out.println("YOU WIN");
            return;
        }

        world[playerPos.getX()][playerPos.getY()] = backTile;
        if (world[nextX][nextY].equals(guard.getGuardTile())) {
            backTile = guard.getBackTile();
            door.change(world);
        } else {
            backTile = world[nextX][nextY];
        }
        world[nextX][nextY] = playerTile;

        playerPos = new Position(nextX, nextY);
    }
}
