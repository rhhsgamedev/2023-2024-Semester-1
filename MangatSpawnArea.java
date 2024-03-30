/**
 MangatSpawnArea
 Name: Vincent Lam
 Description: A subclass of Being on the grid representing a spawn area for MangatBeings.
 Date: June 11, 2023
 **/

import java.io.FileNotFoundException;
import java.util.Random;
public class MangatSpawnArea extends Being {
    private Random rand = new Random();
    private String mangatName;
    private int spawnChance;

    /**
     * A constructor for the MangatSpawnArea class. Takes in position, dimensions, the MangatBeing to spawn, and the chance of it occuring every update.
     * @param x The x position of the spawn area bounds as an Integer.
     * @param y The y position of the spawn area bounds as an Integer.
     * @param width The width of the spawn area bounds as an Integer.
     * @param height The height position of the spawn area bounds as an Integer.
     * @param sys The Game instance.
     * @param mangatName The name of the MangatBeing as a String.
     * @param spawnChance The chance of a MangatBeing to spawn in the bounds every update as an Integer.
     */
    MangatSpawnArea(int x, int y, int width, int height, Game sys, String mangatName, int spawnChance) {
        super(x, y, width, height, "MangatSpawnArea", sys, false, false);
        this.spawnChance = spawnChance;
        this.mangatName = mangatName;
    }

    /**
     * update
     * An abstract method inherited from Being that updates its current state.
     * @throws FileNotFoundException
     */
    public void update() throws FileNotFoundException {
        // generate spawn chance for the current update
        int randChance = rand.nextInt(0, spawnChance);

        if (randChance == 0) {
            // spawn new entity
            int randX = rand.nextInt(getX(), getX() + getHitbox().width);
            int randY = rand.nextInt(getY(), getY() + getHitbox().height);
            MangatBeing enemyMangat = new MangatBeing(randX, randY, mangatName, sys);
            sys.getCurrentMap().getBeingsToBeAdded().add(enemyMangat);
        }
    }

    @Override
    public void interact() {

    }
}
