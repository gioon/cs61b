package byog.Core.Map;

public class Hallway extends Rect {
    private int direction;

    public Hallway(Position b, Position e, int d) {
        super(b, e);
        direction = d;
    }

    public int getDirection() {
        return direction;
    }
}
