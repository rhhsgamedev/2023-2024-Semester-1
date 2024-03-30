/**
 ItemBeing
 Name: Vincent Lam
 Description: A subclass of Being on the grid representing a spawn area for ItemBeings.
 Date: June 11, 2023
 **/

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.Random;

public class ItemSpawnArea extends Being {
    private Random rand = new Random();
    private String itemName;
    private int itemType;
    private int spawnChance;
    private Scanner input;
    private File itemInfo = new File("ItemInfo.txt");

    /**
     * A constructor for the ItemSpawnArea class. Takes in position, dimensions, the item to spawn, and the chance of it occuring every update.
     * @param x The x position of the spawn area bounds as an Integer.
     * @param y The y position of the spawn area bounds as an Integer.
     * @param width The width of the spawn area bounds as an Integer.
     * @param height The height position of the spawn area bounds as an Integer.
     * @param sys The Game instance.
     * @param itemName The name of the Item to spawn as a String.
     * @param itemType The type of the Item spawned as an Integer.
     * @param spawnChance The chance of an item to spawn in the bounds every update as an Integer.
     * @throws FileNotFoundException
     */
    ItemSpawnArea(int x, int y, int width, int height, Game sys, String itemName, int itemType, int spawnChance) throws FileNotFoundException {
        super(x, y, width, height, "ItemSpawnArea", sys, false, false);
        this.input = new Scanner(itemInfo);
        this.spawnChance = spawnChance;
        this.itemName = itemName;
        this.itemType = itemType;
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
            ItemBeing newBeing = new ItemBeing(randX, randY, sys, itemName, itemType);
            sys.getCurrentMap().getBeingsToBeAdded().add(newBeing);
        }
    }

    @Override
    public void interact() {

    }
}
