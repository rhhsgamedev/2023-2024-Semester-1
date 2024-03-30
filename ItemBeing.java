/**
 Item
 Name: Vincent Lam
 Description: A subclass of Being on the grid representing an equippable Item.
 Date: June 11, 2023
 **/

import java.io.FileNotFoundException;

public class ItemBeing extends Being {
    static public int ITEM_IMAGE_DIMENSIONS = 100;
    private Item storedItem;

    /**
     * A constructor for the ItemBeing class.
     * @param x The x position of the Being as an Integer.
     * @param y The y position of the Being as an Integer.
     * @param sys The Game instance.
     * @param itemName The name of the stored item as a String.
     * @param itemType The type of the stored item as an Integer.
     * @throws FileNotFoundException
     */
    ItemBeing(int x, int y, Game sys, String itemName, int itemType) throws FileNotFoundException {
        super(x, y, ITEM_IMAGE_DIMENSIONS, ITEM_IMAGE_DIMENSIONS, itemName, sys, false, false);

        // create image
        ImageElement newElement = new ImageElement(x, y , true, sys.getCurrentMap(), sys.getAllItemSprites().get(itemName), ITEM_IMAGE_DIMENSIONS, ITEM_IMAGE_DIMENSIONS);
        setSpriteElement(newElement);
        sys.getAllScreenElements().add(newElement);
        newElement.setVisible(true);

        // note: itemType is only used in ItemSpawnArea
       if (itemType == 0) {
           this.storedItem = new BattleItem(itemName);
       } else if (itemType == 1) {
           this.storedItem = new CatchItem(itemName);
       }
    }

    /**
     * update
     * An abstract method inherited from Being that updates its current state.
     */
    public void update() {}

    @Override
    public void interact() {

    }

    /**
     * getStoredItem
     * Getter method for the stored item.
     * @return The stored item as an Item instance.
     */
    public Item getStoredItem() {
        return storedItem;
    }

}
