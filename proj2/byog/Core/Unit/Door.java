package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.Core.Map.World;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Door implements Serializable {
    private Position doorPos;
    private TETile unlockedDoorTile;

    private boolean opened = false;

    public Door(Position doorPos, TETile unlockedDoorTile) {
        this.doorPos = doorPos;
        this.unlockedDoorTile = unlockedDoorTile;
    }

    public boolean isOpen() {
        return opened;
    }

    public void open(World world) {
        world.set(doorPos, unlockedDoorTile);
        opened = true;
    }

    public Position getDoorPos() {
        return doorPos;
    }
}
