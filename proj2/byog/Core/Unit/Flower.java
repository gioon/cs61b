package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;

public class Flower implements Serializable {
    private TETile backTile, flowerTile;

    private ArrayList<Position> flowers;
    private ArrayList<TETile> backTiles;

    public Flower(TETile backTile, TETile flowerTile) {
        this.backTile = backTile;
        this.flowerTile = flowerTile;

        flowers = new ArrayList<>();
        backTiles = new ArrayList<>();
    }

    private int getIndex(Position flower) {
        int i = 0;
        while (i < flowers.size()) {
            if (flowers.get(i).equals(flower)) {
                break;
            }
            i++;
        }
        return i;
    }

    public void addFlower(Position flower) {
        flowers.add(flower);
        backTiles.add(backTile);
    }

    public void delFlower(Position flower) {
        int i = getIndex(flower);
        if (i < flowers.size()) {
            flowers.remove(i);
            backTiles.remove(i);
        }
    }

    public void setOneBackTile(Position flower, TETile tile) {
        int i = getIndex(flower);
        if (i < flowers.size()) {
            backTiles.set(i, tile);
        }
    }

    public TETile getOneBackTile(Position flower) {
        int i = getIndex(flower);
        if (i < flowers.size()) {
            return backTiles.get(i);
        }
        return backTile;
    }

    public TETile getFlowerTile() {
        return flowerTile;
    }
}
