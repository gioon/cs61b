package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class Guard implements Serializable {

    Position guardPos;
    TETile backTile;
    TETile guardTile;

    public Guard(Position guardPos, TETile backTile, TETile guardTile) {
        this.guardPos = guardPos;
        this.backTile = backTile;
        this.guardTile = guardTile;
    }
}
