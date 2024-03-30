/**
 RandomChar
 Name: Vincent Lam
 Description: A subclass of Being representing a random NPC on the grid.
 Date: June 11, 2023
 **/

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;

public class RandomChar extends Movable {
    private static int DEFAULT_SPEED = 2;
    private int DIRECTION_CHANGE_TICK = 30;
    private int UNFREEZE_TICK = 50;
    private int currentUnfreezeTick = UNFREEZE_TICK;
    private int currentDirectionTick = DIRECTION_CHANGE_TICK;
    private String message;

    /**
     * A constructor for the RandomChar class. Takes in position, and the name of the Being.
     * @param x The x position of the Being as an integer.
     * @param y The y position of the Being as an integer.
     * @param beingName The name of the Being as a String.
     * @param sys The Game instance.
     */
    RandomChar(int x, int y, String beingName, Game sys) {
        super(x, y, sys.CHAR_HITBOX_WIDTH, sys.CHAR_HITBOX_HEIGHT, beingName, sys, DEFAULT_SPEED, true, true);

        // set randomChar uiElement
        BufferedImage newSprite = (BufferedImage) sys.getAllCharGridSprites().get(beingName + " " + getAnimationState() + " " + getBeingAnimationFrame());
        ImageElement newElement = new ImageElement(x + (getHitbox().width - sys.CHAR_DIMENSIONS) / 2, y + (getHitbox().height - sys.CHAR_DIMENSIONS) / 2, true, sys.getCurrentMap(), newSprite, sys.CHAR_DIMENSIONS, sys.CHAR_DIMENSIONS);
        setSpriteElement(newElement);
        sys.getAllScreenElements().add(newElement);
        newElement.setVisible(true);

        // create interact button
        BufferedImage buttonSprite = sys.getAllUiImages().get("DialoguePrompt");
        ImageElement buttonElement = new ImageElement((int) (getHitbox().getCenterX() - (buttonSprite.getWidth() / 2)), getY() - 50 - (buttonSprite.getHeight() / 2), true, sys.getCurrentMap(), buttonSprite);
        InteractButton button = new InteractButton(buttonElement, this, 50, 50, sys);
        sys.getCurrentMap().getAllBeings().add(button);
        setInteractButton(button);
    }

    /**
     * A constructor for the RandomChar class. Takes in position, the name of the Being, and a specified speed.
     * @param x The x position of the Being as an integer.
     * @param y The y position of the Being as an integer.
     * @param beingName The name of the Being as a String.
     * @param sys The Game instance.
     */
    RandomChar(int x, int y, String beingName, Game sys, int speed) {
        super(x, y, sys.CHAR_HITBOX_WIDTH, sys.CHAR_HITBOX_HEIGHT, beingName, sys, speed, true, true);

        // set randomChar uiElement
        BufferedImage newSprite = (BufferedImage) sys.getAllCharGridSprites().get(beingName + " " + getAnimationState() + " " + getBeingAnimationFrame());
        ImageElement newElement = new ImageElement(x + (getHitbox().width - sys.CHAR_DIMENSIONS) / 2, y + (getHitbox().height - sys.CHAR_DIMENSIONS) / 2, true, sys.getCurrentMap(), newSprite, sys.CHAR_DIMENSIONS, sys.CHAR_DIMENSIONS);
        setSpriteElement(newElement);
        sys.getAllScreenElements().add(newElement);
        newElement.setVisible(true);

        // create interact button
        BufferedImage buttonSprite = sys.getAllUiImages().get("DialoguePrompt");
        ImageElement buttonElement = new ImageElement((int) (getHitbox().getCenterX() - (buttonSprite.getWidth() / 2)), getY() - 50 - (buttonSprite.getHeight() / 2), true, sys.getCurrentMap(), buttonSprite, buttonSprite.getWidth() / 3, buttonSprite.getHeight() / 3);
        InteractButton button = new InteractButton(buttonElement, this, 0, -90, sys);
        sys.getCurrentMap().getAllBeings().add(button);
        setInteractButton(button);
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
        HashSet<Being> allBeings = area.getAllBeings();
        TranslucentBoundary hideCharacter = null;

        for (Being currentBeing : allBeings) {
            if ((currentBeing != null) && (currentBeing != this) && (futureHitbox.intersects(currentBeing.getHitbox()))) {

                // hitbox collision
                if (currentBeing.hasCollision()) {
                    return true;

                    // hiding the sprite behind walls
                } else if (currentBeing instanceof TranslucentBoundary) {
                    hideCharacter = (TranslucentBoundary) currentBeing;
                }
            }
        }

        // translucent sprite
        if (hideCharacter != null) {
            getSpriteElement().setTransparency(hideCharacter.ALPHA_VALUE);
        } else {
            getSpriteElement().setTransparency(1);
        }

        return false;
    }

    /**
     * update
     * An abstract method inherited from Being that updates its current state.
     * @throws IOException
     * @throws InterruptedException
     */
    public void update() throws IOException, InterruptedException, CloneNotSupportedException {
        currentDirectionTick--;
        if (currentDirectionTick > 0) {
            currentUnfreezeTick--;
        }

        if (!(isIdle()) && (currentUnfreezeTick == 0)) {
            setMovementDirection(4);
            currentDirectionTick = DIRECTION_CHANGE_TICK;
            return;
        }
        if (currentDirectionTick <= 0) {
            currentDirectionTick = DIRECTION_CHANGE_TICK;
            goRandomDirection();
        }
        move();
    }

    public void interact() {

    }

    /**
     * finishPathfinding
     * An abstract method inherited from Movable that runs if the Movable has succesfully traversed to pathfindCoords.
     */
    public void finishPathfinding() {}

    public String getMessage() {
        return message;
    }
}
