import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

abstract class BattleChar {

    private int x;
    private int y;
    public String name;
    public Game sys;
    public ImageElement element;
    public HashMap<String, BufferedImage> charSprites;
    public Battle battle;

    // anim state
    public String animState;
    public String actionAnim;
    public int frameIndex;
    public int frameDelay;
    public int currentFrameDelay;
    public boolean canAirAttack;
    public int[] horiInput;
    public double[] velocity;
    public boolean flipped;
    public int airTime;
    public boolean doubleJump;
    public int loopBackTo;
    public int lightCombo;
    public int groundCombo;
    public int airCombo;
    public int lightTimer;
    public int currentLightTimer;
    public int stun;
    public boolean animCancel;
    public HashSet<ImageElement> afterimages;
    public boolean createAfterimages;
    public int afterimageTick;
    public int currentAfterimageTick;
    public int walkspeed;

    BattleChar(Game sys, int x, int y, String name) {
        this.sys = sys;
        this.battle = sys.getCurrentBattle();
        this.x = x;
        this.y = y;
        this.walkspeed = 5;
        this.canAirAttack = true;
        this.velocity = new double[2];
        this.name = name;
        this.animState = "idle";
        this.actionAnim = null;
        this.frameIndex = 0;
        this.frameDelay = 10;
        this.currentFrameDelay = 0;
        this.loopBackTo = -1;
        this.charSprites = sys.getCurrentBattle().getCharSprites();
        this.horiInput = new int[2];
        this.flipped = false;
        this.airTime = 0;
        this.lightCombo = 0;
        this.groundCombo = 0;
        this.airCombo = 0;
        this.lightTimer = 90;
        this.stun = 0;
        this.currentLightTimer = 0;
        this.animCancel = false;
        this.afterimages = new HashSet<>();
        this.createAfterimages = false;
        this.afterimageTick = 5;
        this.currentAfterimageTick = 0;
        this.doubleJump = false;

        BufferedImage img = charSprites.get(name + "idle0");
        this.element = new ImageElement(x - img.getWidth(), y - (img.getHeight() * 2), true, sys.getCurrentBattle(), charSprites.get(name + "idle0"), img.getWidth() * 2, img.getHeight() * 2);
        sys.getAllScreenElements().add(element);
        element.setVisible(true);
    }

    abstract void update();

    public void createAfterimage() {
        //
        ImageElement after = new ImageElement(element.getElementX(), element.getElementY(), true, battle, element.getImage(), element.getElementWidth(), element.getElementHeight());
        after.setFlip(this.isFlipped());
        sys.getAllScreenElements().add(after);
        afterimages.add(after);
        after.setVisible(true);
        after.setTransparency(0.5f);
    }

    abstract void attack();
    abstract void move(Tile[][] tileMap);

    public Rectangle getHitbox() {
        return new Rectangle(this.x - (element.getElementWidth() / 2), this.y - element.getElementHeight(), element.getElementWidth(), element.getElementHeight());
    }

    public void changeMoveAnim(String newAnim, int frameDelay) {
        if (!newAnim.equals(animState)) {
            createAfterimages = false;
            animCancel = false;
            animState = newAnim;
            actionAnim = null;
            currentFrameDelay = 0;
            this.frameDelay = frameDelay;
            this.loopBackTo = -1;
            frameIndex = 0;
            BufferedImage img = charSprites.get(name + animState + String.valueOf(this.frameIndex));
            element.changeImage(img, img.getWidth() * 2, img.getHeight() * 2);
        }
    }

    public void changeAttackAnim(String newAnim, int frameDelay) {
        if (!newAnim.equals(animState)) {
            createAfterimages = false;
            actionAnim = newAnim;
            animCancel = false;
            animState = null;
            currentFrameDelay = 0;
            this.frameDelay = frameDelay;
            this.loopBackTo = -1;
            frameIndex = 0;
            BufferedImage img = charSprites.get(name + actionAnim + String.valueOf(this.frameIndex));
            element.changeImage(img, img.getWidth() * 2, img.getHeight() * 2);
        }
    }

    public void changeAttackAnim(String newAnim, int frameDelay, int loopBackTo) {
        if (!newAnim.equals(animState)) {
            createAfterimages = false;
            actionAnim = newAnim;
            animCancel = false;
            animState = null;
            currentFrameDelay = 0;
            this.frameDelay = frameDelay;
            this.loopBackTo = loopBackTo;
            frameIndex = 0;
            BufferedImage img = charSprites.get(name + actionAnim + String.valueOf(this.frameIndex));
            element.changeImage(img, img.getWidth() * 2, img.getHeight() * 2);
        }
    }

    public void changeMoveAnim(String newAnim, int frameDelay, int loopBackTo) {
        if (!newAnim.equals(animState)) {
            this.loopBackTo = loopBackTo;
            animState = newAnim;
            currentFrameDelay = 0;
            animCancel = false;
            this.frameDelay = frameDelay;
            frameIndex = 0;
            BufferedImage img = charSprites.get(name + animState + String.valueOf(this.frameIndex));
            element.changeImage(img, img.getWidth() * 2, img.getHeight() * 2);
        }
    }

    public void createVFX(String name, int frameDelay, double scale, int xOffset, int yOffset, boolean followChar) {
        VFX v = new VFX(sys, name, frameDelay, scale, this, xOffset, yOffset, followChar);
        battle.getVisibleVFX().add(v);
    }

    public void createVFX(String name, int frameDelay, double scale, int xOffset, int yOffset, boolean followChar, float transparency) {
        VFX v = new VFX(sys, name, frameDelay, scale, this, xOffset, yOffset, followChar, transparency);
        battle.getVisibleVFX().add(v);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setStun(int stun) {
        this.stun = stun;
    }
    public void setWalkspeed(int walkspeed) {
        this.walkspeed = walkspeed;
    }
}
