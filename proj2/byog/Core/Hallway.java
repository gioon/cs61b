package byog.Core;

public class Hallway extends Room {

    int direction;

    public Hallway(Position b, Position e, int d) {
        super(b, e);
        direction = d;
    }
}
