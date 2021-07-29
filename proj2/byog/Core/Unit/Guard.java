package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;

public class Guard implements Serializable {
    private TETile backTile;

    private ArrayList<Position> guards;
    private ArrayList<TETile> backTiles;

    public Guard(TETile backTile) {
        this.backTile = backTile;

        guards = new ArrayList<>();
        backTiles = new ArrayList<>();
    }

    public int getIndex(Position p) {
        for (int i = 0; i < guards.size(); i++) {
            if (guards.get(i).equals(p)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isClear() {
        return guards.size() == 0;
    }

    public void addGuard(Position p) {
        guards.add(p);
        backTiles.add(backTile);
    }

    public void delGuard(int i) {
        guards.remove(i);
        backTiles.remove(i);
    }

    public void setOneBackTile(int i, TETile t) {
        backTiles.set(i, t);
    }

    public TETile getOneBackTile(int i) {
        return backTiles.get(i);
    }
}
