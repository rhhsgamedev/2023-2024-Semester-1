/**
 TranslucentBoundary
 Name: Vincent Lam
 Description: A subclass of Being representing areas where the player sprite is hidden when behind objects.
 Date: June 11, 2023
 **/
public class TranslucentBoundary extends Being {
    public float ALPHA_VALUE = 0.5f; // The transparency of a Being once in its bounds
    TranslucentBoundary(int x, int y, int width, int height, Game sys) {
        super(x, y, width, height, "Translucent", sys, false, false);
    }

    /**
     * update
     * An abstract method to update the state of the Being.
     */
    public void update() {}

    @Override
    public void interact() {

    }
}
