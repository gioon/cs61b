package byog.Core;

import byog.Core.Feature.LightSource;
import byog.Core.Feature.Shade;
import byog.Core.Map.World;
import byog.Core.Unit.Door;
import byog.Core.Unit.Flower;
import byog.Core.Unit.Guard;
import byog.Core.Unit.Player;
import byog.Core.Unit.Portal;

import java.io.Serializable;
import java.util.Random;

public class Saving implements Serializable {
    private boolean hardMode;
    private int round;
    private Random random;
    private World world;
    private Door door;
    private Player player;
    private LightSource lightSource;
    private Guard guard;
    private Flower flower;
    private Portal portal;
    private Shade shade;

    public void setHardMode(boolean hardMode) {
        this.hardMode = hardMode;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLightSource(LightSource lightSource) {
        this.lightSource = lightSource;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public void setShade(Shade shade) {
        this.shade = shade;
    }

    public boolean getHardMode() {
        return hardMode;
    }

    public int getRound() {
        return round;
    }

    public Random getRandom() {
        return random;
    }

    public World getWorld() {
        return world;
    }

    public Door getDoor() {
        return door;
    }

    public Player getPlayer() {
        return player;
    }

    public LightSource getLightSource() {
        return lightSource;
    }

    public Guard getGuard() {
        return guard;
    }

    public Flower getFlower() {
        return flower;
    }

    public Portal getPortal() {
        return portal;
    }

    public Shade getShade() {
        return shade;
    }
}
