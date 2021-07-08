package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

public class Saving implements Serializable {

    Random random;
    TETile[][] world;
    Position doorCoord;
    Player player;

    public Saving(Random random, TETile[][] world, Position doorCoord, Player player) {
        this.random = random;
        this.world = world;
        this.doorCoord = doorCoord;
        this.player = player;
    }
}
