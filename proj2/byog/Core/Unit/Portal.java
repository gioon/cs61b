package byog.Core.Unit;

import byog.Core.Map.Position;
import byog.Core.Map.World;
import byog.TileEngine.TETile;

import java.io.Serializable;

public class Portal implements Serializable {
    private Position p1, p2;
    private TETile backTile1, backTile2, portalTile;
    private int state; // 0: null; 1: p1; 2: p2

    private Player player;

    public Portal(TETile portalTile, Player player) {
        this.p1 = null;
        this.p2 = null;
        this.portalTile = portalTile;
        this.player = player;
        this.state = 0;
    }

    public void setPortal(World world) {
        if (state == 0) {
            p1 = player.getPlayerPos();
            backTile1 = player.getBackTile();
            player.setBackTile(portalTile);
            state = 1;
        } else if (state == 1) {
            if (player.getPlayerPos().equals(p1)) {
                return;
            }
            p2 = player.getPlayerPos();
            backTile2 = player.getBackTile();
            player.setBackTile(portalTile);
            state = 2;
        } else {
            if (player.getPlayerPos().equals(p1)) {
                player.setBackTile(backTile1);
                world.set(p2, backTile2);
            } else if (player.getPlayerPos().equals(p2)) {
                player.setBackTile(backTile2);
                world.set(p1, backTile1);
            } else {
                world.set(p1, backTile1);
                world.set(p2, backTile2);
            }
            p1 = null;
            p2 = null;
            backTile1 = null;
            backTile2 = null;
            state = 0;
        }
    }

    public void setBackTile1(TETile backTile1) {
        this.backTile1 = backTile1;
    }

    public void setBackTile2(TETile backTile2) {
        this.backTile2 = backTile2;
    }

    public Position getPortal1() {
        return p1;
    }

    public Position getPortal2() {
        return p2;
    }
}
