import java.awt.image.BufferedImage;

public class VFX {
    private Game sys;
    private Battle battle;
    private String name;
    private ImageElement element;
    private BattleChar anchor;
    private int xOffset;
    private int yOffset;
    private int xStart;
    private int yStart;
    private int frameIndex;
    private int frameDelay;
    private boolean finished;
    private int currentFrameDelay;
    private double scale;
    private boolean flipped;
    private boolean followChar;
    VFX(Game sys, String name, int frameDelay, double scale, BattleChar anchor, int xOffset, int yOffset, boolean followChar) {
        this.sys = sys;
        this.name = name;
        this.anchor = anchor;
        this.xOffset = xOffset;
        this.followChar = followChar;
        this.yOffset = yOffset;
        this.frameIndex = 0;
        this.frameDelay = frameDelay;
        this.currentFrameDelay = 0;
        this.finished = false;
        this.scale = scale;
        this.battle = sys.getCurrentBattle();
        this.flipped = anchor.isFlipped();
        this.followChar = followChar;
        this.xStart = anchor.getX();
        this.yStart = anchor.getY();

        BufferedImage img = battle.getVfxSprites().get(name + frameIndex);

        this.element = new ImageElement(anchor.getX() + xOffset, anchor.getY() + yOffset, true, battle, img, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale));
        if (flipped) {
            element.setFlip(true);
            element.setElementX(anchor.getX() + (-xOffset) - (element.getElementWidth()));
        } else {
            element.setElementX(anchor.getX() + xOffset);
        }

        sys.getAllScreenElements().add(element);
        element.setVisible(true);
    }

    VFX(Game sys, String name, int frameDelay, double scale, BattleChar anchor, int xOffset, int yOffset, boolean followChar, float transparency) {
        this.sys = sys;
        this.name = name;
        this.anchor = anchor;
        this.xOffset = xOffset;
        this.followChar = followChar;
        this.yOffset = yOffset;
        this.frameIndex = 0;
        this.frameDelay = frameDelay;
        this.currentFrameDelay = 0;
        this.finished = false;
        this.scale = scale;
        this.battle = sys.getCurrentBattle();
        this.flipped = anchor.isFlipped();
        this.followChar = followChar;
        this.xStart = anchor.getX();
        this.yStart = anchor.getY();

        BufferedImage img = battle.getVfxSprites().get(name + frameIndex);

        this.element = new ImageElement(anchor.getX() + xOffset, anchor.getY() + yOffset, true, battle, img, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale));
        this.element.setTransparency(transparency);
        if (flipped) {
            element.setFlip(true);
            element.setElementX(anchor.getX() + (-xOffset) - (element.getElementWidth()));
        } else {
            element.setElementX(anchor.getX() + xOffset);
        }
        sys.getAllScreenElements().add(element);
        element.setVisible(true);
    }

    public void update() {
        if (!finished) {
            if (currentFrameDelay == frameDelay) {
                if (battle.getVfxSprites().get(name + (this.frameIndex + 1)) == null) {
                    finished = true;
                    element.setVisible(false);
                    sys.getAllScreenElements().remove(element);
                } else {
                    frameIndex += 1;
                    BufferedImage img = battle.getVfxSprites().get(name + this.frameIndex);
                    element.changeImage(img, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale));
                }
                currentFrameDelay = 0;
            }

            currentFrameDelay += 1;

            if (followChar) {
                if (flipped) {
                    element.setElementX(anchor.getX() + (-xOffset) - (element.getElementWidth()));
                } else {
                    element.setElementX(anchor.getX() + xOffset);
                }
                element.setElementY(anchor.getY() + yOffset);
            } else {
                if (flipped) {
                    element.setElementX(xStart + (-xOffset) - (element.getElementWidth()));
                } else {
                    element.setElementX(xStart + xOffset);
                }
            }

        }
    }

    public boolean isFinished() {
        return finished;
    }
}
