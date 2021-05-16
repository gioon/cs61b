package byog.Core;

import byog.TileEngine.TETile;

import java.util.ArrayList;

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

    @Override
    public String toString() {
        return "Room{" +
                "base=" + base +
                ", end=" + end +
                ", minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                '}';
    }
}
