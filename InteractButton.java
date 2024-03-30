public class InteractButton extends Being {
    private ImageElement element;
    private int xOffset;
    private int yOffset;
    private Being origin;

    InteractButton(ImageElement element, Being origin, int xOffset, int yOffset, Game sys) {
        super(element.getElementX(), element.getElementY(), element.getElementWidth(), element.getElementHeight(), "InteractButton", sys, false, false);
        this.element = element;
        this.origin = origin;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        sys.getAllScreenElements().add(element);

//        this.description = description;

        // add text element for description
//        this.descriptionElement = new TextElement(x + 60, y + 25, true, sys.getCurrentMap(), description, sys.NORMAL_TEXT_FONT, Color.white);
//        sys.getAllScreenElements().add(descriptionElement);
//
//        // add image element for button
//        BufferedImage interactButton = sys.getAllUiImages().get("InteractButton");
//        ImageElement newElement = new ImageElement(x, y , true, sys.getCurrentMap(), interactButton, 50, 50);
//        setSpriteElement(newElement);
//        sys.getAllScreenElements().add(newElement);
    }

    /**
     * update
     * An abstract method to update the state of the Being.
     */

    public void update() {}

    @Override
    public void interact() {}

    public ImageElement getElement() {
        return element;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public Being getOrigin() {
        return origin;
    }
}
