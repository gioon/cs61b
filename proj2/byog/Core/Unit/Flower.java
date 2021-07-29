package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;

public class Flower implements Serializable {
    private TETile backTile;

    private ArrayList<Position> flowers;
    private ArrayList<TETile> backTiles;

    public Flower(TETile backTile) {
        this.backTile = backTile;

        flowers = new ArrayList<>();
        backTiles = new ArrayList<>();
    }

    public int getIndex(Position p) {
        for (int i = 0; i < flowers.size(); i++) {
            if (flowers.get(i).equals(p)) {
                return i;
            }
        }
        return -1;
    }

    public void addFlower(Position p) {
        flowers.add(p);
        backTiles.add(backTile);
    }

    public void delFlower(int i) {
        flowers.remove(i);
        backTiles.remove(i);
    }

    public void setOneBackTile(int i, TETile t) {
        backTiles.set(i, t);
    }

    public TETile getOneBackTile(int i) {
        return backTiles.get(i);
    }
}
