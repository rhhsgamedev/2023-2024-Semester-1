/**
 Boundary
 Name: Vincent Lam
 Description: A subclass of Being representing a collidable invisible boundary on the grid.
 Date: June 11, 2023
 **/
public class Boundary extends Being {

    /**
     * A constructor for the Boundary class. Has the necessary info for its position and hitbox.
     * @param x The x position of the Being as an integer.
     * @param y The y position of the Being as an integer.
     * @param width The width of the Being's hitbox as an integer.
     * @param height The height of the Being's hitbox as an integer.
     */
    Boundary(int x, int y, int width, int height, Game sys) {
        super(x, y, width, height, "Boundary", sys, true, false);
    }

    /**
     * update
     * An abstract method inherited from Being that updates its state.
     */
    public void update() {}

    @Override
    public void interact() {

    }
}
