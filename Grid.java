/**
 Grid
 Name: Vincent Lam
 Description: A class representing the map component of the
 Date: June 11, 2023
 **/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Grid {

    private Random rand = new Random();
    private Scanner input;
    private Game sys;
    private String mapName;
    private File mapFile;
    private ImageElement backgroundElement; // The map image
    // Managing all Beings
    private HashSet<Being> allBeings = new HashSet<Being>();
    private HashSet<Being> beingsToBeAdded = new HashSet<Being>();
    private PlayerChar player;

    // grid state
    private int gridState;
    public static int FREE_STATE = 2;
    public static int INVENTORY_STATE = 3;

    // Camera
    private float camX;
    private float camY;
    private float worldX;
    private float worldY;
    private static int CAM_SMOOTHING = 20;
    private int xZoomOffset = 0;
    private int yZoomOffset = 0;
    private double scaling = 1;

    /**
     * A constructor for the Grid class. Takes in the map file.
     * @param mapFile The text file of the map to load.
     * @param sys The Game instance.
     * @throws FileNotFoundException
     */
    Grid(File mapFile, Game sys) throws FileNotFoundException {
        this.sys = sys;
        this.mapFile = mapFile;
        this.input = new Scanner(this.mapFile);
    }

    /**
     * init
     * Initializes the necessary uiElements and Beings in the grid.
     * @param startingCoords The starting coordinates of the player character as an Integer array.
     * @throws FileNotFoundException
     */
    public void init(int[] startingCoords) throws FileNotFoundException {
        this.mapName = input.nextLine();
        System.out.println(mapName);

        // play music
        sys.getSound(mapName).loop(-1);

        // set map uiElements
        BufferedImage mapImage = (BufferedImage) sys.getAllMapImages().get(mapName);
        this.backgroundElement = new ImageElement(0, 0, true, this, mapImage, mapImage.getWidth(), mapImage.getHeight());
        sys.getAllScreenElements().add(backgroundElement);
        backgroundElement.setVisible(true);

        // create player entity
        String startingCoordsString = input.nextLine();
        if (startingCoords != null) {
            this.player = new PlayerChar(startingCoords[0], startingCoords[1], sys);
        } else {
            int startingX = Integer.parseInt(startingCoordsString.substring(0, startingCoordsString.indexOf(" ")));
            int startingY = Integer.parseInt(startingCoordsString.substring(startingCoordsString.indexOf(" ") + 1, startingCoordsString.length()));
            this.player = new PlayerChar(startingX, startingY, sys);
        }

        // add to Being list
        allBeings.add(player);

        // Reading the map information from the file
        while (input.hasNextLine()) {
            String nextAddition = input.nextLine();

            // Creating boundaries
            if ((nextAddition.contains("Bound: ")) || nextAddition.contains("Translucent: ")) {
                int x = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("x") + 1, nextAddition.indexOf("y") - 1));
                int y = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("y") + 1, nextAddition.indexOf("w") - 1));
                int w = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("w") + 1, nextAddition.indexOf("h") - 1));
                int h = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("h") + 1, nextAddition.length()));
                if (nextAddition.contains("Bound: ")) {
                    Boundary newBound = new Boundary(x, y, w, h, sys);
                    allBeings.add(newBound);
                } else {
                    TranslucentBoundary newBound = new TranslucentBoundary(x, y, w, h, sys);
                    allBeings.add(newBound);
                }

            // Random NPCs
            } else if (nextAddition.contains("RandomNPC: ")) {
                int x = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("x") + 1, nextAddition.indexOf("y") - 1));
                int y = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("y") + 1, nextAddition.length()));
                String name = "Random" + rand.nextInt(1, 5);
                RandomChar randomNpc = new RandomChar(x, y, name, sys, 2);
                allBeings.add(randomNpc);

            // Quest NPC
            } else if (nextAddition.contains("Quest: ")) {
                int x = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("x") + 1, nextAddition.indexOf("y") - 1));
                int y = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("y") + 1, nextAddition.length()));
                // yes this is hardcoded, we only had time for one NPC :(
//                QuestPoint newQuest = new QuestPoint(x, y, sys);
//                allBeings.add(newQuest);

            // Shop NPC
            } else if (nextAddition.contains("Shop: ")) {
                int x = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("x") + 1, nextAddition.indexOf("y") - 1));
                int y = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("y") + 1, nextAddition.indexOf("z") - 1));
                int npcPathX = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("z") + 1, nextAddition.indexOf("q") - 1));
                int npcPathY = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("q") + 1, nextAddition.indexOf("k") - 1));
                int shopX = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("k") + 1, nextAddition.indexOf("m") - 1));
                int shopY = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("m") + 1, nextAddition.length()));
                ShopPoint shop = new ShopPoint(x, y, npcPathX, npcPathY, shopX, shopY, sys);
                allBeings.add(shop);

            // Item Spawn Areas
            } else if (nextAddition.contains("Area: ")) {
                int x = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("x") + 1, nextAddition.indexOf("y") - 1));
                int y = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("y") + 1, nextAddition.indexOf("w") - 1));
                int w = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("w") + 1, nextAddition.indexOf("h") - 1));
                int h = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("h") + 1, nextAddition.indexOf(".") - 1));
                String itemName = nextAddition.substring(nextAddition.indexOf(".") + 1, nextAddition.indexOf("_") - 1);
                int itemType = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("_") + 1, nextAddition.indexOf("!") - 1));
                int chance = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("!") + 1, nextAddition.length()));
                ItemSpawnArea newArea = new ItemSpawnArea(x, y, w, h, sys, itemName, itemType, chance);
                allBeings.add(newArea);

            // Teleport Points
            } else if (nextAddition.contains("Teleport: ")) {
                int x = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("x") + 1, nextAddition.indexOf("y") - 1));
                int y = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("y") + 1, nextAddition.indexOf("z") - 1));
                int destX = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("z") + 1, nextAddition.indexOf("q") - 1));
                int destY = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("q") + 1, nextAddition.indexOf(".") - 1));
                String mapName = nextAddition.substring(nextAddition.indexOf(".") + 1, nextAddition.length());
                TeleportPoint newTeleportPoint = new TeleportPoint(x, y, destX, destY, mapName, sys);
                allBeings.add(newTeleportPoint);

            // Mangat Spawns
            } else if (nextAddition.contains("Mangat: ")) {
                int x = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("x") + 1, nextAddition.indexOf("y") - 1));
                int y = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("y") + 1, nextAddition.indexOf("w") - 1));
                int w = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("w") + 1, nextAddition.indexOf("h") - 1));
                int h = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("h") + 1, nextAddition.indexOf(".") - 1));
                String mangatName = nextAddition.substring(nextAddition.indexOf(".") + 1, nextAddition.indexOf("!") - 1);
                int chance = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("!") + 1, nextAddition.length()));
                MangatSpawnArea newArea = new MangatSpawnArea(x, y, w, h, sys, mangatName, chance);
                allBeings.add(newArea);

            // Enemy trainer spawns
            } else if (nextAddition.contains("Trainer")) {
                int x = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("x") + 1, nextAddition.indexOf("y") - 1));
                int y = Integer.parseInt(nextAddition.substring(nextAddition.indexOf("y") + 1, nextAddition.length()));
                String name = "Trainer" + nextAddition.substring(7, 8);
                EnemyChar enemyNpc = new EnemyChar(x, y, name, sys);
                allBeings.add(enemyNpc);
            }
        }

        // set grid state to free player movement
        gridState = FREE_STATE;

    }

    /**
     * updateMap
     * Updates the state of the Grid.
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public void updateMap() throws IOException, InterruptedException, CloneNotSupportedException {
        // tween the camera to its actual position
        float transX = (float) (player.getHitbox().getCenterX() - this.camX) / CAM_SMOOTHING;
        float transY = (float) (player.getHitbox().getCenterY() - this.camY) / CAM_SMOOTHING;
        updateCamera(this.camX + transX, this.camY + transY);

        worldX = ((sys.SCREEN_WIDTH / 2) - camX);
        worldY = ((sys.SCREEN_HEIGHT / 2) - camY);

        // update all Beings
        for (Being thing : allBeings) {
            if (thing != null) {
                thing.update();
            }
        }

        // add pending Beings after previous Beings have been updated to avoid error
        for (Being thing : beingsToBeAdded) {
            allBeings.add(thing);
        }
        beingsToBeAdded.clear();

        // update harpal balance uiElement
//        harpalBalanceElement.setText(String.valueOf(sys.getHarpals()));
    }

    /**
     * updateCamera
     * Updates the camera position.
     * @param newX The x coordinate of the new camera position.
     * @param newY The y coordinate of the new camera position.
     */
    public void updateCamera(float newX, float newY) {
        this.camX = newX;
        this.camY = newY;
    }

    /**
     * getWorldX
     * Getter method for the worldX attribute;
     * @return Returns the worldX attribute as an Integer.
     */
   public int getWorldX() {
        return (int) worldX;
   }

    /**
     * getWorldY
     * Getter method for the worldY attribute;
     * @return Returns the worldY attribute as an Integer.
     */
   public int getWorldY() {
        return (int) worldY;
   }

    /**
     * getAllBeings
     * Getter method for the ArrayList of all beings.
     * @return The ArrayList of all beings.
     */
    public HashSet<Being> getAllBeings() {
        return allBeings;
    }

    /**
     * getBeingsToBeAdded
     * Getter method for the ArrayList of all beings to be added.
     * @return The ArrayList of all beings to be added.
     */
    public HashSet<Being> getBeingsToBeAdded() {
        return beingsToBeAdded;
    }

    /**
     * getMapName
     * Getter method for the name of the current map.
     * @return The name of the current map as a String.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * addItem
     * Adds a new Item to the player's inventory if not full.
     * @param newItem The new item to be added as an Item.
     * @return True if successfully added, and false if the inventory is too full.
     */
//    public boolean addItem(Item newItem) {
//        Item[][] allItems = sys.getInventoryItems();
//        for (int i = 0; i < allItems[0].length; i++) {
//            for (int k = 0; k < allItems.length; k++) {
//
//                if (allItems[k][i] == null) {
//                    allItems[k][i] = newItem;
//
//                    if (inventoryItemUis[k][i] == null) {
//                        reloadItemSlot(k, i);
//                        return true;
//                    }
//
//                    inventoryItemUis[k][i].changeImage(sys.getAllItemSprites().get(newItem.getItemName()));
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * resetInventoryDesc
     * Resets the description uiElements.
     */

//    public void resetInventoryDesc() {
//        itemTitleElement.setText("Item Name");
//        itemDescElement.setText("Description");
//        thirdItemAttribute.setText("");
//        fourthItemAttribute.setText("");
//        sellQTextElement.setVisible(false);
//        sellQElement.setVisible(false);
//    }


    /**
     * reloadMangatSlot
     * Re-add the ImageElement and TextElement for the slot in the Mangat inventory.
     * @param index The index of the slot to be reloaded.
     */

    /**
     * reloadItemSlot
     * Re-add the ImageElement and TextElement for the slot in the Item inventory.
     * @param column The column of the slot to be reloaded.
     * @param row The row of the slot to be reloaded.
     */
//    public void reloadItemSlot(int column, int row) {
//        ImageElement newIcon = new ImageElement(sys.ITEM_SLOT_X_START + (column * (sys.SLOT_SIZE + sys.ITEM_SLOT_GAP)), sys.ITEM_SLOT_Y_START + (row * (sys.SLOT_SIZE + sys.ITEM_SLOT_GAP)), false, this, sys.getAllItemSprites().get(sys.getInventoryItems()[column][row].getItemName()), sys.SLOT_SIZE, sys.SLOT_SIZE);
//        inventoryItemUis[column][row] = newIcon;
//        sys.getAllScreenElements().add(newIcon);
//    }

    /**
     * getPlayer
     * Getter method for the player attribute.
     * @return The player attribute as a PlayerChar.
     */
    public PlayerChar getPlayer() {
        return player;
    }

    public double getScaling() {
        return scaling;
    }

    /**
     * getGridState
     * Getter method for the gridState attribute.
     * @return The gridState attribute as an Integer.
     */

    public int getGridState() {
        return gridState;
    }

    public void setXZoomOffset(int newX) {
        xZoomOffset = newX;
    }

    public void setYZoomOffset(int newY) {
        yZoomOffset = newY;
    }
    public int getXZoomOffset() {
        return xZoomOffset;
    }

    public int getYZoomOffset() {
        return yZoomOffset;
    }

    public void setScaling(double scaling) {
        this.scaling = scaling;
    }

    public void delete() {
        sys.stopSound(getMapName());
        HashSet<uiElement> gridElements = new HashSet<uiElement>();
        for (uiElement e : sys.getAllScreenElements()) {
            if (e.getParent() instanceof Grid) {
                gridElements.add(e);
            }
        }
        for (uiElement e : gridElements) {
            sys.getAllScreenElements().remove(e);
        }
    }
}
