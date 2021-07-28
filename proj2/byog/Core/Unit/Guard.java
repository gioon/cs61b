package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;

public class Guard implements Serializable {
    private TETile backTile, guardTile;

    private ArrayList<Position> guards;
    private ArrayList<TETile> backTiles;

    public Guard(TETile backTile, TETile guardTile) {
        this.backTile = backTile;
        this.guardTile = guardTile;

        guards = new ArrayList<>();
        backTiles = new ArrayList<>();
    }

    private int getIndex(Position guard) {
        int i = 0;
        while (i < guards.size()) {
            if (guards.get(i).equals(guard)) {
                break;
            }
            i++;
        }
        return i;
    }

    public boolean isClear() {
        return guards.size() == 0;
    }

    public void addGuard(Position guard) {
        guards.add(guard);
        backTiles.add(backTile);
    }

    public void delGuard(Position guard) {
        int i = getIndex(guard);
        if (i < guards.size()) {
            guards.remove(i);
            backTiles.remove(i);
        }
    }

    public void setOneBackTile(Position guard, TETile tile) {
        int i = getIndex(guard);
        if (i < guards.size()) {
            backTiles.set(i, tile);
        }
    }

    public TETile getOneBackTile(Position guard) {
        int i = getIndex(guard);
        if (i < guards.size()) {
            return backTiles.get(i);
        }
        return backTile;
    }

    public TETile getGuardTile() {
        return guardTile;
    }
}
