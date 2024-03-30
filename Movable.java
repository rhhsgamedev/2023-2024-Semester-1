/**
 Movable
 Name: Vincent Lam
 Description: A subclass of Being representing Beings that are able to change position and sprite.
 Date: June 11, 2023
 **/

import java.io.IOException;
import java.util.Random;
abstract class Movable extends Being {
    private Random rand = new Random();

    // movement
    private int speed;
    private boolean[] movementDirection;

    // animation
    private int animationSpeed = 15;
    private int currentTick = animationSpeed;

    // pathfinding
    private int[] pathfindCoords;

    /**
     * A constructor for the Movable class. Has the necessary info for its position, hitbox, sprite, and collision.
     * @param x The x position of the Being as an integer.
     * @param y The y position of the Being as an integer.
     * @param width The width of the Being's hitbox as an integer.
     * @param height The height of the Being's hitbox as an integer.
     * @param beingName The name of the Being as a String.
     * @param sys The Game instance.
     * @param speed The movement speed of the Being.
     * @param canCollide A boolean for whether the Being's hitbox can restrict movement for Movable instances walking into them.
     */
    Movable(int x, int y, int width, int height, String beingName, Game sys, int speed, boolean canCollide, boolean canInteract) {
        super(x, y, width, height, beingName, sys, canCollide, canInteract);
        this.speed = speed;
        this.movementDirection = new boolean[4];
    }

    /**
     * move
     * Updates the Being's coordinates and sprite depending on the direction it is facing.
     */

    public void move() throws IOException, InterruptedException, CloneNotSupportedException {
        if (speed > 0) {
            // vertical
            boolean moved = false;
            boolean verticalTrans = false;
            int[] newPosition = new int[2];
            newPosition[0] = getX();
            newPosition[1] = getY();

            if (pathfindCoords == null) {

                // regular random movement

                // vertical
                if (movementDirection[0]) {
                    newPosition[1] -= speed;
                    verticalTrans = true;
                    moved = true;
                    updateAnimationState("Back");
                } else if (movementDirection[1]) {
                    newPosition[1] += speed;
                    verticalTrans = true;
                    moved = true;
                    updateAnimationState("Forward");
                }
                // horizontal
                if (movementDirection[2]) {
                    newPosition[0] += speed;
                    moved = true;
                    if (!verticalTrans) {
                        updateAnimationState("Right");
                    }
                } else if (movementDirection[3]) {
                    newPosition[0] -= speed;
                    moved = true;
                    if (!verticalTrans) {
                        updateAnimationState("Left");
                    }
                }
            } else {

                // pathfinding movement
                int destX = pathfindCoords[0];
                int destY = pathfindCoords[1];

                // if below destination, move up
                if (getY() > destY) {
                    newPosition[1] -= speed;
                    verticalTrans = true;
                    moved = true;
                    updateAnimationState("Back");
                    // if above destination, move down
                } else if (getY() < destY) {
                    newPosition[1] += speed;
                    verticalTrans = true;
                    moved = true;
                    updateAnimationState("Forward");
                }

                // if to the left of destination, move right
                if (getX() < destX) {
                    newPosition[0] += speed;
                    moved = true;
                    if (!verticalTrans) {
                        updateAnimationState("Right");
                    }
                    // if to the right of destination, move left
                } else if (getX() > destX) {
                    newPosition[0] -= speed;
                    moved = true;
                    if (!verticalTrans) {
                        updateAnimationState("Left");
                    }
                }

                // if reached pathfind coords, stop pathfinding
                if ((newPosition[0] == getX()) && (newPosition[1] == getY())) {
                    pathfindCoords = null;
                    finishPathfinding();
                }
            }
            // If no movement, do not update sprite
            if (!moved) {
                setBeingAnimationFrame(0);
                checkCollisions(sys.getCurrentMap(), newPosition);
            } else {
                // check for collisions
                boolean collided = checkCollisions(sys.getCurrentMap(), newPosition);
                if (collided == false) {
                    setPosition(newPosition);
                }

                // update sprite action
                currentTick--;
                if (currentTick <= 0) {
                    currentTick = animationSpeed;
                    if (isLastAnimationFrame()) {
                        setBeingAnimationFrame(0);
                    } else {
                        setBeingAnimationFrame(getBeingAnimationFrame() + 1);

                        // play walking sounds for player only
                        if (this instanceof PlayerChar) {
                            if ((getBeingAnimationFrame() == 1) || (getBeingAnimationFrame() == 3)) {
                                sys.playSound("footstep");
                            }
                        }
                    }
                }
            }
        } else {
            setBeingAnimationFrame(0);
        }
    }

    /**
     * goRandomDirection
     * Sets the Being to face a random direction.
     */
    public void goRandomDirection() {
        int randDirect = rand.nextInt(0,5);
        setMovementDirection(randDirect);
    }

    /**
     * updateAnimationState
     * Transitions to a new animation state.
     * @param newAnimationState The new animation state as a String.
     */
    public void updateAnimationState(String newAnimationState) {
        if (!newAnimationState.equals(getAnimationState())) {
            setAnimationState(newAnimationState);
            currentTick = animationSpeed;
            setBeingAnimationFrame(0);
        }
    }

    /**
     * finishPathfinding
     * An abstract method inherited from Movable that runs if the Movable has succesfully traversed to pathfindCoords.
     */
    abstract void finishPathfinding() throws IOException, InterruptedException;

    /**
     * update
     * An abstract method inherited from Being that updates its current state.
     */
    abstract void update() throws IOException, InterruptedException, CloneNotSupportedException;

    /**
     * setPathfindCoords
     * Setter method for the Movable's coordinates to automatically move towards.
     * @param coords The new coordinates as an integer array.
     */
    public void setPathfindCoords(int[] coords) {
        pathfindCoords = coords;
    }

    /**
     * changeAnimationSpeed
     * Changes the animation speed of the Movable.
     * @param newSpeed The new tickrate of the Being's animation as an integer.
     */
    public void changeAnimationSpeed(int newSpeed) {
        animationSpeed = newSpeed;
    }

    /**
     * changeSpeed
     * Changes the walkspeed of the Movable.
     * @param newSpeed The new walkspeed of the Movable as an Integer.
     */
    public void changeSpeed(int newSpeed) {
        speed = newSpeed;
    }

    /**
     * getSpeed
     * Getter method for the walkspeed of the Movable.
     * @return The walkspeed of the Movable as an Integer.
     */

    public int getSpeed() {
        return speed;
    }

    /**
     * checkCollisions
     * An abstract method inherited from Movable that restricts the movement of the Being when colliding with hitboxes. If colliding into the player, it starts a battle.
     * @param area The Grid instance.
     * @param newPosition The future position of the Movable as an Integer array.
     * @return True if the Movable's future hitbox collides with any collidable hitboxes, and false if it doesn't.
     */
    abstract boolean checkCollisions(Grid area, int[] newPosition) throws CloneNotSupportedException;

    /**
     * getPathfindingCoords
     * Getter method for the coordinates of the Movable to automatically move towards.
     * @return The pathfindCoords as an integer array.
     */
    public int[] getPathfindingCoords() {
        return pathfindCoords;
    }

    /**
     * getMovementDirection
     * Getter method for the movement direction of the Being.
     * @return The direction as a in a boolean array.
     */
    public boolean[] getMovementDirection() {
        return movementDirection;
    }

    /**
     * setMovementDirection
     * Setter method for the movement direction of the Being in one direction.
     * @param direction The new direction as an Integer.
     */
    public void setMovementDirection(int direction) {
        switch (direction) {
            // up
            case 0:
                movementDirection[0] = true;
                movementDirection[1] = false;
                movementDirection[2] = false;
                movementDirection[3] = false;
                break;
            // down
            case 1:
                movementDirection[0] = false;
                movementDirection[1] = true;
                movementDirection[2] = false;
                movementDirection[3] = false;
                break;
            // right
            case 2:
                movementDirection[0] = false;
                movementDirection[1] = false;
                movementDirection[2] = true;
                movementDirection[3] = false;
                break;
            // left
            case 3:
                movementDirection[0] = false;
                movementDirection[1] = false;
                movementDirection[2] = false;
                movementDirection[3] = true;
                break;
            // stationary
            case 4:
                movementDirection[0] = false;
                movementDirection[1] = false;
                movementDirection[2] = false;
                movementDirection[3] = false;
                break;
        }
    }

    /**
     * overrideMovementDirection
     * Overwrites the movementDirection boolean array for player movement.
     * @param newDirections The new boolean array for movementDirection.
     */
    public void overrideMovementDirection(boolean[] newDirections) {
        movementDirection = newDirections;
    }

    /**
     * isIdle
     * Whether the Being is moving in a direction, or is stationary.
     * @return True if stationary, false if moving
     */
    public boolean isIdle() {
        if (!(movementDirection[0]) && !(movementDirection[1]) && !(movementDirection[2]) && !(movementDirection[3])) {
            return true;
        }
        return false;
    }
}
