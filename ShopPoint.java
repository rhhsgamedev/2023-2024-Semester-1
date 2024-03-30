/**
 QuestPoint
 Name: Vincent Lam
 Description: A subclass of Being representing an interactable point with a shop NPC.
 Date: June 11, 2023
 **/

import java.io.FileNotFoundException;

public class ShopPoint extends Being {
    private ShopChar shopNpc;
    private InteractButton button;
    private Game sys;
    private int npcPathX;
    private int npcPathY;

    /**
     * A constructor for the ShopPoint class. Contains the position data for both the ShopPoint and the associated ShopChar.
     * @param x The x position of the Being as an Integer.
     * @param y The y position of the Being as an Integer.
     * @param npcPathX The x position of the shopkeeper to pathfind towards once called.
     * @param npcPathY The y position of the shopkeeper to pathfind towards once called.
     * @param shopX The x position of the shopkeeper as an Integer.
     * @param shopY The y position of the shopkeeper as an Integer.
     * @param sys The Game instance.
     */

    ShopPoint(int x, int y, int npcPathX, int npcPathY, int shopX, int shopY, Game sys) {
        super(x, y, 0, 0, "ShopPoint", sys, false, false);

        // adding shop npc
        this.shopNpc = new ShopChar(shopX, shopY, sys);
        sys.getCurrentMap().getAllBeings().add(shopNpc);

        this.sys = sys;

        // pathfind coords for the shopkeeper to walk to
        this.npcPathX = npcPathX;
        this.npcPathY = npcPathY;

        // button to activate the pathfinding
//        this.button = new InteractButton(x, y, sys, this, "Call " + shopNpc.getBeingName() + "?");
        sys.getCurrentMap().getAllBeings().add(button);
    }

    /**
     * update
     * An abstract method inherited from Being that updates its current state.
     * @throws FileNotFoundException
     */
    public void update() throws FileNotFoundException {
//        if (button.hasInteracted()) {
//            // shopkeeper pathfinds towards the counter
//            int[] pathfindCoords = new int[2];
//            pathfindCoords[0] = npcPathX;
//            pathfindCoords[1] = npcPathY;
//            shopNpc.setPathfindCoords(pathfindCoords);
//            button.finishInteract();
//
//            sys.playSound("Bell");
//            sys.playSound("Greeting");
//        }
    }

    @Override
    public void interact() {

    }
}
