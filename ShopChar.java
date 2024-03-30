import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ShopChar extends Movable {
    private static int DEFAULT_SPEED = 2;
    private int DIRECTION_CHANGE_TICK = 30;
    private int UNFREEZE_TICK = 50;
    private int currentDirectionTick = DIRECTION_CHANGE_TICK;
    private int currentUnfreezeTick = UNFREEZE_TICK;

    /**
     * A constructor for the ShopChar class. Takes in position.
     * @param x The x position of the Being as an integer.
     * @param y The y position of the Being as an integer.
     * @param sys The Game instance.
     */
    ShopChar(int x, int y, Game sys) {
        super(x, y, sys.CHAR_HITBOX_WIDTH, sys.CHAR_HITBOX_HEIGHT, "Shopkeeper", sys, DEFAULT_SPEED, true, true);

        // set shopChar uiElement
        BufferedImage newSprite = (BufferedImage) sys.getAllCharGridSprites().get(getBeingName() + " " + getAnimationState() + " " + getBeingAnimationFrame());
        ImageElement newElement = new ImageElement(x + (getHitbox().width - sys.CHAR_DIMENSIONS) / 2, y + (getHitbox().height - sys.CHAR_DIMENSIONS) / 2, true, sys.getCurrentMap(), newSprite, sys.CHAR_DIMENSIONS, sys.CHAR_DIMENSIONS);
        setSpriteElement(newElement);
        sys.getAllScreenElements().add(newElement);
        newElement.setVisible(true);
    }

    /**
     * checkCollisions
     * An abstract method inherited from Movable that restricts the movement of the Being when colliding with hitboxes. If colliding into the player, it starts a battle.
     * @param area The Grid instance.
     * @param newPosition The future position of the Movable as an Integer array.
     * @return True if the Movable's future hitbox collides with any collidable hitboxes, and false if it doesn't.
     */
    public boolean checkCollisions(Grid area, int[] newPosition) {
        Rectangle futureHitbox = new Rectangle(newPosition[0], newPosition[1], getHitbox().width, getHitbox().height);
        HashSet<Being> allBeings = area.getAllBeings();
        TranslucentBoundary hideCharacter = null;

        for (Being currentBeing: allBeings) {
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
        if (getPathfindingCoords() == null) {
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
        }
        move();
    }

    @Override
    public void interact() {

    }

    /**
     * finishPathfinding
     * An abstract method inherited from Movable that runs if the Movable has succesfully traversed to pathfindCoords.
     * @throws IOException
     * @throws InterruptedException
     */
    public void finishPathfinding() throws IOException, InterruptedException {
        changeSpeed(0);
        updateAnimationState("Forward");

        // Bring up the store for the user
        Store newStoreNPC = new Store();
        newStoreNPC.storeStart();
    }
}
