/**
 EnemyChar
 Name: Vincent Lam
 Description: A subclass of Movable reperesenting enemy trainers that battle the player.
 Date: June 11, 2023
 **/

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class EnemyChar extends Movable {
    private static int DEFAULT_SPEED = 5;
    private int DIRECTION_CHANGE_TICK = 30;
    private int currentDirectionTick = DIRECTION_CHANGE_TICK;
    private boolean trackingPlayer;

    // detection hitbox to move towards the nearest player
    private Rectangle fov;
    private int viewX;
    private int viewY;
    private int FOV_RADIUS = 600;


    /**
     * A constructor for the EnemyChar class. Takes in position and the name of the Being.
     * @param x The x position of the Being as an Integer.
     * @param y The y position of the Being as an Integer.
     * @param beingName The name of the Being as a String.
     * @param sys The game instance.
     */
    EnemyChar(int x, int y, String beingName, Game sys) {
        super(x, y, sys.CHAR_HITBOX_WIDTH, sys.CHAR_HITBOX_HEIGHT, beingName, sys, DEFAULT_SPEED, true, false);

        // set randomChar uiElement
        BufferedImage newSprite = (BufferedImage) sys.getAllCharGridSprites().get(beingName + " " + getAnimationState() + " " + getBeingAnimationFrame());
        ImageElement newElement = new ImageElement(x + (getHitbox().width - sys.CHAR_DIMENSIONS) / 2, y + (getHitbox().height - sys.CHAR_DIMENSIONS) / 2, true, sys.getCurrentMap(), newSprite, sys.CHAR_DIMENSIONS, sys.CHAR_DIMENSIONS);
        setSpriteElement(newElement);
        sys.getAllScreenElements().add(newElement);
        newElement.setVisible(true);

        // field of view hitbox
        this.viewX = getX() + (getHitbox().width - FOV_RADIUS) / 2;
        this.viewY = getY() + (getHitbox().height - FOV_RADIUS) / 2;
        this.fov = new Rectangle(viewX, viewY, FOV_RADIUS, FOV_RADIUS);

        // trainer Mangat presets
//        this.mangatList = new Mangat[6];
//        if (beingName.equals("Trainer1")) {
//            mangatList[0] = new Cloyster();
//            mangatList[1] = new Pikachu();
//            mangatList[2] = new Eevee();
//        } else if (beingName.equals("Trainer2")) {
//            mangatList[0] = new Pikachu();
//            mangatList[1] = new Charmander();
//            mangatList[2] = new Machop();
//            mangatList[3] = new Onyx();
//        } else if (beingName.equals("Trainer3")) {
//            mangatList[0] = new Squirtle();
//            mangatList[1] = new Bulbasaur();
//            mangatList[2] = new Bulbasaur();
//            mangatList[3] = new Gastly();
//            mangatList[4] = new Eevee();
//            mangatList[5] = new Onyx();
//        }
//        this.mangatList = new Mangat[6];
//        if (beingName.equals("Trainer1")) {
//            mangatList[0] = new Cloyster();
//        } else if (beingName.equals("Trainer2")) {
//            mangatList[0] = new Pikachu();
//        } else if (beingName.equals("Trainer3")) {
//            mangatList[0] = new Squirtle();
//        }
    }

    /**
     * checkCollisions
     * An abstract method inherited from Movable that restricts the movement of the Being when colliding with hitboxes. If colliding into the player, it starts a battle.
     * @param area The Grid instance.
     * @param newPosition The future position of the Movable as an Integer array.
     * @return True if the Movable's future hitbox collides with any collidable hitboxes, and false if it doesn't.
     */
    public boolean checkCollisions(Grid area, int[] newPosition) throws CloneNotSupportedException {
        Rectangle futureHitbox = new Rectangle(newPosition[0], newPosition[1], getHitbox().width, getHitbox().height);
        HashSet<Being> allBeings = area.getAllBeings();
        TranslucentBoundary hideCharacter = null;

        for (Being currentBeing : allBeings) {
            if ((currentBeing != null) && (currentBeing != this) && (futureHitbox.intersects(currentBeing.getHitbox()))) {
                // hitbox collision
                if (currentBeing.hasCollision()) {

                    // start battle if collided with player
                    if (currentBeing instanceof PlayerChar) {
                        int[] savedCharPosition = new int[2];
                        savedCharPosition[0] = currentBeing.getX();
                        savedCharPosition[1] = currentBeing.getY();
//                        sys.startBattle(this);
                    }
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

        // update fov hitbox
        this.viewX = getX() + (getHitbox().width - FOV_RADIUS) / 2;
        this.viewY = getY() + (getHitbox().height - FOV_RADIUS) / 2;
        fov.move(viewX, viewY);

        // check for player
        boolean previouslyTrackingPlayer = trackingPlayer;
        PlayerChar foundPlayer = checkForPlayer();

        if (foundPlayer != null) {
            // if found, pathfind towards the player's current position
            trackingPlayer = true;
            if (!previouslyTrackingPlayer) {
                sys.playSound(getBeingName() + "_Warning");
            }
            int[] playerCoords = new int[2];
            playerCoords[0] = foundPlayer.getX() + (foundPlayer.getHitbox().width / 2);
            playerCoords[1] = foundPlayer.getY() + (foundPlayer.getHitbox().height / 2);
            setPathfindCoords(playerCoords);
        } else {
            trackingPlayer = false;
        }

        // default movement
        if (currentDirectionTick <= 0) {
            currentDirectionTick = DIRECTION_CHANGE_TICK;
            goRandomDirection();
        }
        move();
    }

    @Override
    public void interact() {

    }

    /**
     * checkForPlayer
     * Whether a PlayerChar is within the EnemyChar's current detection hitbox.
     * @return The PlayerChar instance if it is within the detection hitbox, and null if not.
     */

    public PlayerChar checkForPlayer() {
        // check for player in fov
        HashSet<Being> allBeings = sys.getCurrentMap().getAllBeings();
        for (Being currentBeing : allBeings) {
            if ((currentBeing != null) && (currentBeing != this) && (this.fov.intersects(currentBeing.getHitbox())) && (currentBeing instanceof PlayerChar)) {
                return (PlayerChar) currentBeing;
            }
        }
        return null;
    }

    /**
     * finishPathfinding
     * An abstract method inherited from Movable that runs if the Movable has succesfully traversed to pathfindCoords.
     */
    public void finishPathfinding() {}

    /**
     * getMangatList
     * Getter method for the mangatList attribute.
     * @returns The MangatList attribute as an array of Mangats.
     */
}
