package byog.Core.Map;

import java.io.Serializable;

public class Rect implements Serializable {
    private Position base, end;
    private int minX, maxX, minY, maxY;

    public Rect(Position b, Position e) {
        base = b;
        end = e;

        int baseX = base.getX();
        int baseY = base.getY();
        int endX = end.getX();
        int endY = end.getY();
        minX = Math.min(baseX, endX);
        maxX = Math.max(baseX, endX);
        minY = Math.min(baseY, endY);
        maxY = Math.max(baseY, endY);
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public Position getBase() {
        return base;
    }

    public Position getEnd() {
        return end;
    }
}
