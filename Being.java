/**
 Being
 Name: Vincent Lam
 Description: A class representing an entity located in the allBeings ArrayList (in Grid).
 Date: June 11, 2023
 **/

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

abstract class Being {
    private String beingName;
    private String animationState;
    private int beingAnimationFrame;
    private ImageElement spriteElement;
    private int x;
    private int y;
    private Rectangle hitbox;
    public Game sys;
    private boolean canCollide;
    private boolean canInteract;
    private InteractButton button;

    /**
     * A constructor for the Being class. Has the necessary info for its position, hitbox, sprite, and colission.
     * @param x The x position of the Being as an integer.
     * @param y The y position of the Being as an integer.
     * @param width The width of the Being's hitbox as an integer.
     * @param height The height of the Being's hitbox as an integer.
     * @param beingName The name of the Being as a String.
     * @param canCollide A boolean for whether the Being's hitbox can restrict movement for Movable instances walking into them.
     */
    Being(int x, int y, int width, int height, String beingName, Game sys, boolean canCollide, boolean canInteract) {
        // coordinates and bounding box
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(x, y, width, height);
        this.sys = sys;
        this.canCollide = canCollide;
        this.canInteract = canInteract;

        // set default sprite
        this.beingName = beingName;
        this.animationState = "Forward";
        this.beingAnimationFrame = 0;
    }

    /**
     * Update
     * An abstract method to update the state of the Being.
     * @throws FileNotFoundException
     */

    abstract void update() throws IOException, InterruptedException, CloneNotSupportedException;

    /**
     * getX
     * Getter method for the x position of the Being.
     * @returns The x position as an Integer.
     */

    public int getX() {
        return x;
    }

    /**
     * getY
     * Getter method for the y position of the Being.
     * @returns The y position as an Integer.
     */

    public int getY() {
        return y;
    }

    /**
     * setPosition
     * Setter method for the coordinates (both x and y) of the Being.
     * @param newPosition The new position of the Being as an Integer array.
     */
    public void setPosition(int[] newPosition) {
        x = newPosition[0];
        y = newPosition[1];
        hitbox.setLocation(newPosition[0], newPosition[1]);
        getSpriteElement().setElementX(newPosition[0] + (hitbox.width - getSpriteElement().getElementWidth()) / 2);
        getSpriteElement().setElementY(newPosition[1] + (hitbox.height - getSpriteElement().getElementHeight()) / 2);

        if (this instanceof PlayerChar) {
            Rectangle interactHitbox = ((PlayerChar) this).getInteractHitbox();
            interactHitbox.setLocation((int) (newPosition[0] - ((interactHitbox.getWidth() - hitbox.getWidth()) / 2)), (int) (newPosition[1] - ((interactHitbox.getHeight() - hitbox.getHeight()) / 2)));
        }
        if (button != null) {
            button.getElement().setElementX((int) (button.getOrigin().getHitbox().getX() + button.getXOffset()));
            button.getElement().setElementY((int) (button.getOrigin().getHitbox().getY() + button.getYOffset()));
        }
    }

    /**
     * getSpriteElement
     * Getter method for the sprite of the Being.
     * @returns The sprite of the Being as an ImageElement.
     */
    public ImageElement getSpriteElement() {
        return spriteElement;
    }

    /**
     * AnimationState
     * Getter method for the animation state of the Being.
     * @returns The animation state of the Being as a String.
     */
    public String getAnimationState() {
        return animationState;
    }

    /**
     * setAnimationState
     * Setter method for the animation state of the Being.
     * @param newState The new animation state as a String.
     */
    public void setAnimationState(String newState) {
        animationState = newState;
        updateSpriteElement();
    }

    /**
     * getBeingAnimationFrame
     * Getter method for the frame number of the Being's current animation.
     * @returns The frame number as an Integer.
     */
    public int getBeingAnimationFrame() {
        return beingAnimationFrame;
    }

    /**
     * setBeingAnimationFrame
     * Setter method for the frame number of the Being's current animation.
     * @param newAnimFrame The new frame number as an integer.
     */

    public void setBeingAnimationFrame(int newAnimFrame) {
        beingAnimationFrame = newAnimFrame;
        updateSpriteElement();
    }

    /**
     * isLastAnimationFrame
     * Whether the Being's current animation frame is the last in its animation.
     * @returns False if a frame comes after the current one, and true if no following frame exists.
     */
    public boolean isLastAnimationFrame() {
        if (sys.getAllCharGridSprites().get(beingName + " " + animationState + " " + (beingAnimationFrame + 1)) != null) {
                return false;
        }
        return true;
    }

    /**
     * updateSpriteElement
     * Updates the image of the Being's spriteElement.
     */
    public void updateSpriteElement() {
        BufferedImage newSprite = (BufferedImage) sys.getAllCharGridSprites().get(beingName + " " + animationState + " " + beingAnimationFrame);
        this.spriteElement.changeImage(newSprite);
    }

    /**
     * setSpriteElement
     * Setter method for the spriteElement of the Being.
     * @param newElement The new element as an ImageElement.
     */
    public void setSpriteElement(ImageElement newElement) {
        this.spriteElement = newElement;
    }

    /**
     * getHitbox
     * Getter method for the hitbox of the Being.
     * @returns The hitbox attribute as a Rectangle.
     */

    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * getBeingName
     * Getter method for the name of the Being.
     * @returns The beingName attribute as a String.
     */

    public String getBeingName() {
        return beingName;
    }

    /**
     * hasCollision
     * Whether the Being has collision physics with its hitbox with other Beings.
     * @returns The canCollide attribute as a boolean.
     */
    public boolean hasCollision() {
        return canCollide;
    }

    public boolean isInteractable() {
        return canInteract;
    }

    public abstract void interact();

    public InteractButton getInteractButton() {
        return button;
    }

    public void setInteractButton(InteractButton button) {
        this.button = button;
    }
}
