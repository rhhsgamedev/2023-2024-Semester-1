/**
 Item
 Name: Vincent Lam
 Description: A class representing an article in the player's inventory.
 Date: June 11, 2023
 **/
abstract class Item {
    private String itemName;
    private int sellPrice;
    private String desc;

    /**
     * A constructor for the Item class.
     * @param itemName The name of the Item as a String, used to load its necessary info in its subclasses.
     */
    Item(String itemName) {
        this.itemName = itemName;
    }

    /**
     * getItemName
     * Getter method for the item's name.
     * @returns itemName The name of the Item as a String.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * setDesc
     * Setter method for the item's description.
     * @param newDesc The new description as a String.
     */
    public void setDesc(String newDesc) {
        desc = newDesc;
    }

    /**
     * getDesc
     * Getter method for the item's description.
     * @return The description of the item as a String.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * setSellPrice
     * Setter method for the item's sell price
     * @param newPrice The new sell price as an integer.
     */
    public void setSellPrice(int newPrice) {
        sellPrice = newPrice;
    }

    /**
     * getSellPrice
     * Getter method for the item's sell price
     * @return The Item's sell price as an Integer.
     */
    public int getSellPrice() {
        return sellPrice;
    }

    /**
     * equip
     * Adds the Item to the player's inventory.
     * @return The Game instance.
     */
    public void equip(Game sys) {
//        sys.getCurrentMap().addItem(this);
    }

}
