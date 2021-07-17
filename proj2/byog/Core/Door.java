package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class Door implements Serializable {
    Position doorPos;
    TETile doorTile, unlockedDoorTile;

    public Door(Position doorPos, TETile doorTile, TETile unlockedDoorTile) {
        this.doorPos = doorPos;
        this.doorTile = doorTile;
        this.unlockedDoorTile = unlockedDoorTile;
    }

    public void change(TETile[][] world) {
        world[doorPos.x][doorPos.y] = unlockedDoorTile;
    }
}
