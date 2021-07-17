package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class Player implements Serializable {

    Position playerPos;
    int health;
    TETile backTile;
    TETile playerTile, wallTile;
    Guard guard;
    Door door;

    public Player(Position playerPos, int health, TETile backTile,
                  TETile playerTile, TETile wallTile, Guard guard, Door door) {
        this.playerPos = playerPos;
        this.health = health;
        this.backTile = backTile;
        this.playerTile = playerTile;
        this.door = door;
        this.guard = guard;
        this.wallTile = wallTile;
    }

    public Position getPlayerPos() {
        return playerPos;
    }

    public void move(TETile[][] world, char c) {
        int nextX = playerPos.x;
        int nextY = playerPos.y;

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

        if (world[nextX][nextY].equals(door.doorTile)
                || world[nextX][nextY].equals(wallTile)) {
            return;
        }

        if (world[nextX][nextY].equals(door.unlockedDoorTile)) {
            System.out.println("YOU WIN");
            return;
        }

        world[playerPos.x][playerPos.y] = backTile;
        if (world[nextX][nextY].equals(guard.guardTile)) {
            backTile = guard.backTile;
            door.change(world);
        } else {
            backTile = world[nextX][nextY];
        }
        world[nextX][nextY] = playerTile;

        playerPos.x = nextX;
        playerPos.y = nextY;
    }
}
