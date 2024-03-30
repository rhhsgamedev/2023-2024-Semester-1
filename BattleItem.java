/**
 BattleItem
 Name: Vincent Lam
 Description: A subclass of Item, used for applying buffs to Mangats in battle.
 Date: June 11, 2023
 **/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BattleItem extends Item {

    private Scanner input;
    private double healthBuff;
    private double attackBuff;
    private File itemInfo = new File("ItemInfo.txt");

    /**
     * A constructor for the BattleItem class. Takes in the item name as a String and loads the necessary information from itemInfo.
     * @param itemName The name of the item as a String.
     * @throws FileNotFoundException
     */
    BattleItem(String itemName) throws FileNotFoundException {
        super(itemName);
        this.input = new Scanner(itemInfo);
        boolean foundInfo = false;

        while ((input.hasNextLine()) && (!foundInfo)) {
            String nextAddition = input.nextLine();
            if (itemName.equals(nextAddition)) {
                foundInfo = true;
                // skip over the item's type (only used in MangatSpawnArea to differentiate spawning BattleItems and CatchItems)
                input.nextLine();
                healthBuff = Double.parseDouble(input.nextLine());
                attackBuff = Double.parseDouble(input.nextLine());
                setDesc(input.nextLine());
                setSellPrice(Integer.parseInt(input.nextLine()));
            }
        }
    }

    /**
     * getHealthBuff
     * Getter method for the healthBuff attribute.
     * @returns The HealthBuff attribute as a double.
     */
    public double getHealthBuff() {
        return healthBuff;
    }

    /**
     * getAttackBuff
     * Getter method for the attackBuff attribute.
     * @returns The attackBuff attribute as a double.
     */
    public double getAttackBuff() {
        return attackBuff;
    }
}
