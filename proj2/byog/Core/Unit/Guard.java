package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Guard implements Serializable {
    private Position guardPos;
    private TETile backTile;
    private TETile guardTile;

    public Guard(Position guardPos, TETile backTile, TETile guardTile) {
        this.guardPos = guardPos;
        this.backTile = backTile;
        this.guardTile = guardTile;
    }

    public Position getGuardPos() {
        return guardPos;
    }

    public TETile getBackTile() {
        return backTile;
    }

    public TETile getGuardTile() {
        return guardTile;
    }

    public void setBackTile(TETile backTile) {
        this.backTile = backTile;
    }
}
