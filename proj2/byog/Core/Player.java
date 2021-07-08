package byog.Core;

import java.io.Serializable;

public class Player implements Serializable {

    Position playerCoord;
    int health;

    public Player(Position playerCoord, int health) {
        this.playerCoord = playerCoord;
        this.health = health;
    }

    public void move(int nextX, int nextY) {
        playerCoord.x = nextX;
        playerCoord.y = nextY;
    }
}
