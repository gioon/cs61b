package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Door implements Serializable {
    private Position doorPos;
    private TETile lockedDoorTile, unlockedDoorTile;

    public Door(Position doorPos, TETile lockedDoorTile, TETile unlockedDoorTile) {
        this.doorPos = doorPos;
        this.lockedDoorTile = lockedDoorTile;
        this.unlockedDoorTile = unlockedDoorTile;
    }

    public void change(TETile[][] world) {
        world[doorPos.getX()][doorPos.getY()] = unlockedDoorTile;
    }

    public Position getDoorPos() {
        return doorPos;
    }

    public TETile getLockedDoorTile() {
        return lockedDoorTile;
    }

    public TETile getUnlockedDoorTile() {
        return unlockedDoorTile;
    }
}
