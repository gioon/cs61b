package byog.Core;

public class Room {

    public Position base, end;
    public int minX, maxX, minY, maxY;

    public Room(Position b, Position e) {
        base = b;
        end = e;
        evalRange();
    }

    protected void evalRange() {
        int baseX = base.x;
        int baseY = base.y;
        int endX = end.x;
        int endY = end.y;
        minX = Math.min(baseX, endX);
        maxX = Math.max(baseX, endX);
        minY = Math.min(baseY, endY);
        maxY = Math.max(baseY, endY);
    }
}
