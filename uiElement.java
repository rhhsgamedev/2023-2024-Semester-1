import java.awt.image.BufferedImage;

abstract class uiElement {

    private int x;
    private int y;
    private boolean gridAdjustable;
    private boolean visible;
    private float transparency;
    private Object parent;

    uiElement(int x, int y, boolean gridAdjustable, Object parent) {
        this.x = x;
        this.y = y;
        this.gridAdjustable = gridAdjustable;
        this.visible = false;
        this.transparency = 1;
        this.parent = parent;
    }

    public void setElementX(int newX) {
        x = newX;
    }

    public void setElementY(int newY) {
        y = newY;
    }

    public int getElementX() {
        return x;
    }
    public int getElementY() {
        return y;
    }
    public boolean getVisible() {
        return visible;
    }
    public void setVisible(boolean newVisible) {
        visible = newVisible;
    }
    public boolean isGridAdjustable() {
        return gridAdjustable;
    }
    public float getTransparency() {
        return transparency;
    }
    public void setTransparency(float newTransparency) {
        transparency = newTransparency;
    }

    public Object getParent() {
        return parent;
    }
}
