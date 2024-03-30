/**
 PlayerChar
 Name: Vincent Lam
 Description: A subclass of Movable representing the character controlled by the player on the grid.
 Date: June 11, 2023
 **/

import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class PlayerChar extends Movable {
    private static int DEFAULT_SPEED = 2; // default movement speed
    private static int DEFAULT_TICK_TOTAL = 15; // default animation speed
    private static int SPRINT_SPEED = 6; // sprinting speed
    private static int SPRINT_TICK_TOTAL = 9; // sprinting animation speed
    private boolean sprinting = false;
    private Being closestInteract;
    private Rectangle interactHitbox;


    /**
     * A constructor for the PlayerChar class. Takes in position.
     * @param x The x position of the Being as an Integer.
     * @param y The y position of the Being as an Integer.
     * @param sys The Game instance.
     */
    PlayerChar(int x, int y, Game sys) {
        super(x, y, sys.CHAR_HITBOX_WIDTH, sys.CHAR_HITBOX_HEIGHT, "Player", sys, DEFAULT_SPEED, true, false);

        // set playerChar uiElement
        BufferedImage newSprite = (BufferedImage) sys.getAllCharGridSprites().get(getBeingName() + " " + getAnimationState() + " " + getBeingAnimationFrame());
        ImageElement newElement = new ImageElement(x + (getHitbox().width - sys.CHAR_DIMENSIONS) / 2, y + (getHitbox().height - sys.CHAR_DIMENSIONS) / 2, true, sys.getCurrentMap(), newSprite, sys.CHAR_DIMENSIONS, sys.CHAR_DIMENSIONS);
        setSpriteElement(newElement);
        sys.getAllScreenElements().add(newElement);
        newElement.setVisible(true);

        interactHitbox = new Rectangle((int) (getHitbox().x - ((Constants.INTERACT_HITBOX_WIDTH - getHitbox().getWidth()) / 2)), (int) (getHitbox().y - ((Constants.INTERACT_HITBOX_HEIGHT - getHitbox().getHeight()) / 2)), Constants.INTERACT_HITBOX_WIDTH, Constants.INTERACT_HITBOX_HEIGHT);
    }

    /**
     * checkCollisions
     * An abstract method inherited from Movable that restricts the movement of the Being when colliding with hitboxes.
     * @param area The Grid instance.
     * @param newPosition The future position of the Movable as an Integer array.
     * @return True if the Movable's future hitbox collides with any collidable hitboxes, and false if it doesn't.
     */
    public boolean checkCollisions(Grid area, int[] newPosition) {
        Rectangle futureHitbox = new Rectangle(newPosition[0], newPosition[1], getHitbox().width, getHitbox().height);
        Rectangle futureInteractHitbox = new Rectangle((int) (newPosition[0] - ((interactHitbox.getHeight() - getHitbox().getWidth()) / 2)), (int) (newPosition[1] - ((interactHitbox.getHeight() - getHitbox().getHeight())) / 2), interactHitbox.width, interactHitbox.height);
        HashSet<Being> allBeings = area.getAllBeings();
        TranslucentBoundary hideCharacter = null;

        for (Being currentBeing : allBeings) {
            if ((currentBeing != null) && (currentBeing != this)) {
                if (futureInteractHitbox.intersects(currentBeing.getHitbox())) {
                    if (currentBeing.isInteractable()) {
                        if (closestInteract == null) {
                            sys.playSound("Notif");
                        }
                        if (closestInteract != currentBeing) {
                            closestInteract = currentBeing;
                            System.out.println(closestInteract.getBeingName());
                        }
                    }
                }
                if (futureHitbox.intersects(currentBeing.getHitbox())) {
                    // hitbox collision
                    if (currentBeing.hasCollision()) {
                        return true;

                        // hiding the sprite behind walls
                    } else if (currentBeing instanceof TranslucentBoundary) {
                        hideCharacter = (TranslucentBoundary) currentBeing;
                    }
                }


                    // pick up item
//                } else if (currentBeing instanceof ItemBeing) {
//                    if (!sys.isInventoryFull()) {
//                        Item newItem = ((ItemBeing) currentBeing).getStoredItem();
//                        newItem.equip(sys);
//                        sys.playSound("InventoryEquip");
////                        allBeings.set(i, null);
//                        sys.getAllScreenElements().remove(currentBeing.getSpriteElement());
//                    }
//
//                    // show nearby interact buttons
//                } else if (currentBeing instanceof InteractButton) {
//                    InteractButton newButton = (InteractButton) currentBeing;
//                    if (closestButton != null) {
//                        closestButton.buttonVisibility(false);
//                    } else {
//                        sys.playSound("Notif");
//                    }
//                    closestButton = newButton;
//                    closestButton.buttonVisibility(true);
//                }
                }
        }

        // translucent sprite
        if (hideCharacter != null) {
            getSpriteElement().setTransparency(hideCharacter.ALPHA_VALUE);
        } else {
            getSpriteElement().setTransparency(1);
        }

        if (closestInteract != null) {
            if (!(futureInteractHitbox.intersects(closestInteract.getHitbox()))) {
                System.out.println("NULLED");
                ImageElement e = closestInteract.getInteractButton().getElement();
                if (e.getVisible()) {
                    e.setVisible(false);
                }
                closestInteract = null;
            }
        }

        // show closest button
//        if (closestButton != null) {
//            if (!futureHitbox.intersects(closestButton.getHitbox())) {
//                closestButton.buttonVisibility(false);
//                closestButton = null;
//            }
//        }

        return false;
    }

    /**
     * update
     * An abstract method inherited from Being that updates its current state.
     * @throws IOException
     * @throws InterruptedException
     */
    public void update() throws IOException, InterruptedException, CloneNotSupportedException {
        if (sys.getCurrentMap().getGridState() == sys.getCurrentMap().FREE_STATE) {
            if (getSpeed() != 0) {

                boolean[] newDirections = new boolean[4];
                newDirections[0] = sys.isKeyHeld("w");
                newDirections[1] = sys.isKeyHeld("s");
                newDirections[2] = sys.isKeyHeld("d");
                newDirections[3] = sys.isKeyHeld("a");
                overrideMovementDirection(newDirections);

                sprinting = sys.isKeyHeld("l");

                if (sprinting) {
                    changeAnimationSpeed(SPRINT_TICK_TOTAL);
                    changeSpeed(SPRINT_SPEED);
                } else {
                    changeAnimationSpeed(DEFAULT_TICK_TOTAL);
                    changeSpeed(DEFAULT_SPEED);
                }
            }
            if (sys.isKeyPressed("e") && (closestInteract != null)) {
                closestInteract.interact();
            }
        } else {
           setMovementDirection(4);
        }


        if (sys.isKeyHeld("u")) {
            int originalX = (int) ((getHitbox().getCenterX()  + sys.getWorldX()) * sys.getCurrentMap().getScaling());
            int originalY = (int) ((getHitbox().getCenterY()  + sys.getWorldY()) * sys.getCurrentMap().getScaling());
            sys.getCurrentMap().setScaling(sys.getCurrentMap().getScaling() + 0.02);
            int newX = (int) ((getHitbox().getCenterX()  + sys.getWorldX()) * sys.getCurrentMap().getScaling());
            int newY = (int) ((getHitbox().getCenterY()  + sys.getWorldY()) * sys.getCurrentMap().getScaling());

            sys.getCurrentMap().setXZoomOffset(sys.getCurrentMap().getXZoomOffset() + (originalX - newX));
            sys.getCurrentMap().setYZoomOffset(sys.getCurrentMap().getYZoomOffset() + (originalY - newY));

            System.out.println(sys.getCurrentMap().getXZoomOffset());
        }
        if (sys.isKeyHeld("o")) {
            int originalX = (int) ((getHitbox().getCenterX() + sys.getWorldX()) * sys.getCurrentMap().getScaling());
            int originalY = (int) ((getHitbox().getCenterY() + sys.getWorldY()) * sys.getCurrentMap().getScaling());
            sys.getCurrentMap().setScaling(sys.getCurrentMap().getScaling() - 0.02);
            int newX = (int) ((getHitbox().getCenterX() + sys.getWorldX()) * sys.getCurrentMap().getScaling());
            int newY = (int) ((getHitbox().getCenterY() + sys.getWorldY()) * sys.getCurrentMap().getScaling());

            sys.getCurrentMap().setXZoomOffset(sys.getCurrentMap().getXZoomOffset() + (originalX - newX));
            sys.getCurrentMap().setYZoomOffset(sys.getCurrentMap().getYZoomOffset() + (originalY - newY));

            System.out.println(sys.getCurrentMap().getXZoomOffset());
        }

        if (closestInteract != null) {

            ImageElement e = closestInteract.getInteractButton().getElement();
            if (!e.getVisible()) {
                System.out.println("MADE VISIBLE");
                System.out.println(e.getElementX());
                System.out.println(e.getElementY());
                e.setVisible(true);
            }
            if (sys.isKeyHeld("e") && (sys.getCurrentDialogue() == null)) {
                sys.createInteraction(new File("Dialogue.txt"));
            }
        }

        move();
    }

    @Override
    public void interact() {}

    /**
     * finishPathfinding
     * An abstract method inherited from Movable that runs if the Movable has succesfully traversed to pathfindCoords.
     */

    public void finishPathfinding() {}

    public Rectangle getInteractHitbox() {
        return interactHitbox;
    }
}
