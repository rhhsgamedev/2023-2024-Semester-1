/**
 TeleportPoint
 Name: Vincent Lam
 Description: A subclass of Being representing a teleport point to another map usable by the player.
 Date: June 11, 2023
 **/

import java.io.FileNotFoundException;

public class TeleportPoint extends Being {
    private String mapName;
    private InteractButton button;
    private Game sys;
    private int destX;
    private int destY;

    /**
     * A constructor for the TeleportPoint class. Takes in position, and the destination in the next map.
     * @param x The x position of the Being as an Integer.
     * @param y The y position of the Being as an Integer.
     * @param destX The x position to teleport the player to in the new map.
     * @param destY The y position to teleport the player to in the new map.
     * @param mapName The name of the new map as a String.
     * @param sys The Game instance.
     */
    TeleportPoint(int x, int y, int destX, int destY, String mapName, Game sys) {
        super(x, y, 0, 0, "TeleportPoint", sys, false, false);
        this.mapName = mapName;
        this.sys = sys;
        this.destX = destX;
        this.destY = destY;

        // create teleport button
//        this.button = new InteractButton(x, y, sys, this, "To " + mapName + "?");
//        sys.getCurrentMap().getAllBeings().add(button);
    }

    /**
     * update
     * An abstract method inherited from Being that updates its current state.
     * @throws FileNotFoundException
     */
    public void update() throws FileNotFoundException {
//        if (button.hasInteracted()) {
//            // pass in starting coordinates to the Game changeMap method
//            int[] startingCoords = new int[2];
//            startingCoords[0] = destX;
//            startingCoords[1] = destY;
//            sys.changeMap(mapName, startingCoords);
//            button.finishInteract();
//        }
    }

    @Override
    public void interact() {

    }
}
