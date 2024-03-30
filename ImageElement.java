/**
 ImageElement
 Name: Vincent Lam
 Description: A subclass of uiElement used for displaying images.
 Date: June 11, 2023
 **/

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageElement extends uiElement {
    private BufferedImage image;
    private int width;
    private int height;
    private boolean flipped;

    /**
     * A constructor for the ImageElement class. Takes in position, grid adjustability, a BufferedImage, and specificied dimensions for its image.
     * @param x The x position of the uiElement.
     * @param y The y position of the uiElement.
     * @param gridAdjustable A boolean value for whether the uiElement changes position according to the camera.
     * @param parent The parent of the uiElement.
     * @param image The BufferedImage of the ImageElement.
     * @param width The width of the image.
     * @param height The height of the image.
     */
    ImageElement(int x, int y, boolean gridAdjustable, Object parent, BufferedImage image, int width, int height) {
        super(x, y, gridAdjustable, parent);
        this.width = width;
        this.height = height;
        this.image = image;
        this.flipped = false;
    }

    /**
     * A constructor for the ImageElement class. Takes in position, grid adjustability, and a BufferedImage. Assumes the images dimensions are as is.
     * @param x The x position of the uiElement.
     * @param y The y position of the uiElement.
     * @param gridAdjustable A boolean value for whether the uiElement changes position according to the camera.
     * @param parent The parent of the uiElement.
     * @param image The BufferedImage of the ImageElement.
     */

    ImageElement(int x, int y, boolean gridAdjustable, Object parent, BufferedImage image) {
        super(x, y, gridAdjustable, parent);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.image = image;
    }

    /**
     * getElementWidth
     * Getter method for the width of the image.
     * @return The width of the image as an integer.
     */

    public int getElementWidth() {
        return width;
    }

    /**
     * getElementHeight
     * Getter method for the height of the image.
     * @return The height of the image as an integer.
     */
    public int getElementHeight() {
        return height;
    }

    /**
     * changeImage
     * Changes the image of the ImageElement.
     * @return The new image of the ImageElement.
     */
    public void changeImage(BufferedImage newImage) {
        image = newImage;
    }
    public void changeImage(BufferedImage newImage, int width, int height) {
        image = newImage;
        this.width = width;
        this.height = height;
    }

    /**
     * getImage
     * Getter method for the image of the ImageElement.
     * @return The image of the ImageElement as a BufferedImage.
     */
    public BufferedImage getImage() {
        return image;
    }

    public void toggleGrayScale(boolean toggle) {
        if (image == null) {
            System.out.println("no image provided!!!");
            return;
        }
        if (toggle) {
            int rgb=0, r=0, g=0, b=0;
            for (int y=0; y<height; y++) {
                for (int x=0; x<width; x++) {

                    // if not transparent
                    int pixel = image.getRGB(x,y);
                    if((pixel>>24) != 0x00 ) {
                        rgb = (int)(image.getRGB(x, y));
                        r = ((rgb >> 16) & 0xFF);
                        g = ((rgb >> 8) & 0xFF);
                        b = (rgb & 0xFF);
                        rgb = (int)((r+g+b)/3);
                        rgb = (255<<24) | (rgb<<16) | (rgb<<8) | rgb;
                        image.setRGB(x,y,rgb);
                    }
                }
            }
            return;
        }
        image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlip(boolean flipped) {
        this.flipped = flipped;
    }
}
