package byog.Core;

import java.util.Random;

public class Hallway extends Room {
    public int direction;

    public Hallway(Position b, Position e, int d) {
        super(b, e);
        direction = d;
    }
}
