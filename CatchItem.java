/**
 CatchItem
 Name: Vincent Lam
 Description: A subclass of Item reperesenting articles used to capture Mangats.
 Date: June 11, 2023
 **/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CatchItem extends Item {

    private Scanner input;
    private int chance;
    private File itemInfo = new File("ItemInfo.txt");

    /**
     * A constructor for the CatchItem class.
     * @param itemName The name of the Item as a String, used to load its necessary info in its subclasses.
     * @throws FileNotFoundException
     */
    CatchItem(String itemName) throws FileNotFoundException {
        super(itemName);
        this.input = new Scanner(itemInfo);

        boolean foundInfo = false;
        while ((input.hasNextLine()) && (!foundInfo)) {
            String nextAddition = input.nextLine();
            if (itemName.equals(nextAddition)) {
                foundInfo = true;
                // skip over the item's type (only used in ItemSpawnArea to differentiate both subclasses of items)
                input.nextLine();
                chance = Integer.parseInt(input.nextLine());
                setDesc(input.nextLine());
                setSellPrice(Integer.parseInt(input.nextLine()));
            }
        }
    }

    /**
     * getChance
     * Getter method for the spawn chance of the CatchItem.
     * @return The chance as an integer.
     */
    public int getChance() {
        return chance;
    }
}
