/**
 TextElement
 Name: Vincent Lam
 Description: A subclass of uiElement used for displaying text.
 Date: June 11, 2023
 **/

import java.awt.Font;
import java.awt.Color;
public class TextElement extends uiElement{
    private String text;
    private Font elementFont;
    private Color textColor;

    /**
     * A constructor for the TextElement class. Takes in position, grid adjustability, and customizable text.
     * @param x The x position of the uiElement.
     * @param y The y position of the uiElement.
     * @param gridAdjustable A boolean value for whether the uiElement changes position according to the camera.
     * @param parent The parent of the uiElement.
     * @param text The text of the TextElement as a String.
     * @param font The font of the text as a Font instance.
     * @param textColor The color of the text as a Color instance.
     */

    TextElement(int x, int y, boolean gridAdjustable, Object parent, String text, Font font, Color textColor) {
        super(x, y, gridAdjustable, parent);
        this.text = text;
        this.elementFont = font;
        this.textColor = textColor;
    }

    /**
     * getFont
     * Getter method for the elementFont attribute.
     * @return The elementFont attribute as a Font instance.
     */
    public Font getFont() {
        return elementFont;
    }

    /**
     * getText
     * Getter method for the element's text.
     * @return The element's text as a String.
     */
    public String getText() {
        return text;
    }

    /**
     * getColor
     * Getter method for the element's text color.
     * @return The text color as a Color instance.
     */
    public Color getColor() {
        return textColor;
    }

    /**
     * setText
     * Setter method for the element's text.
     * @param newText The new text as a String.
     */
    public void setText(String newText) {
        text = newText;
    }
}
