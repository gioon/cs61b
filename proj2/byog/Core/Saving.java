package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

public class Saving implements Serializable {

    Random random;
    TETile[][] world;
    Door door;
    LightSource lightSource;
    Player player;
    Guard guard;
    Shade shade;

    public Saving(Random random, TETile[][] world, Door door,
                  LightSource lightSource, Player player, Guard guard, Shade shade) {
        this.random = random;
        this.world = world;
        this.door = door;
        this.lightSource = lightSource;
        this.player = player;
        this.guard = guard;
        this.shade = shade;
    }
}
