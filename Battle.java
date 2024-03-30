import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Battle {
    private Game sys;
    private File mapInfo;
    private Tile[][] tileMap;
    private HashMap<String, BufferedImage> mapBackgrounds;
    private HashMap<String, BufferedImage> mapSprites;
    private HashMap<String, BufferedImage> charSprites;
    private HashMap<String, BufferedImage> vfxSprites;
    private HashMap<String, HashMap<String, int[]>> ALL_MOVES;
    private Scanner sc;
    private static File moveData = new File("MoveInfo.txt");
    private Scanner moveDataSc;
    private String mapName;
    private int mapLength;
    private int mapHeight;
    private BattleChar player;

    // Camera
    private float camX;
    private float camY;
    private float worldX;
    private int shakeDuration;
    private float worldY;
    private static int CAM_SMOOTHING = 20;
    private int tweenOutLaunch;
    private int xZoomOffset = 0;
    private int yZoomOffset = 0;
    private int shakeX;
    private int shakeY;
    private double scaling;
    public int shakeFactor;
    private HashSet<ImageElement> backdrops;
    private HashSet<BattleChar> chars;
    private HashSet<VFX> visibleVFX;
    private HashSet<Hitbox> hitboxes;
    private TextElement launchIndicator;
    private TextElement launchIndicator2;
    public int comboCount;
    public int comboDamage;
    public int lastHitDamage;
    public BattleChar currentEnemy;
    public int hitstop;

    Battle(Game sys, File mapInfo) throws IOException {
        this.sys = sys;
        this.mapInfo = mapInfo;
        this.sc = new Scanner(mapInfo);
        this.ALL_MOVES = new HashMap<>();
        this.moveDataSc = new Scanner(moveData);
        this.mapBackgrounds = new HashMap<>();
        this.mapSprites = new HashMap<>();
        this.charSprites = new HashMap<>();
        this.backdrops = new HashSet<>();
        this.visibleVFX = new HashSet<>();
        this.chars = new HashSet<>();
        this.vfxSprites = new HashMap<>();
        this.hitboxes = new HashSet<>();
        this.scaling = 1;
        this.comboCount = 0;
        this.comboDamage = 0;
        this.lastHitDamage = 0;
        this.currentEnemy = null;
        this.hitstop = 0;
        this.shakeDuration = 0;
        this.shakeX = 0;
        this.shakeY = 0;
        this.tweenOutLaunch = 0;
        this.shakeFactor = 20;
    }

    public void start() throws IOException {
        // loading map

        String line;
        String[] sp;
        this.mapName = sc.nextLine();
        line = sc.nextLine();
        sp = line.split(" ");

        this.mapLength = Integer.parseInt(sp[0]);
        this.mapHeight = Integer.parseInt(sp[1]);
        this.tileMap = new Tile[mapHeight][mapLength];
        line = sc.nextLine();
        sp = line.split(" ");
        int playerX = Integer.parseInt(sp[0]);
        int playerY = Integer.parseInt(sp[1]);

        // loading char sprites
        File[] chars = new File("CHAR_BATTLE_SPRITES").listFiles();
        for (int i = 0; i < chars.length; i++) {
            String charName = chars[i].getName();
            File[] anims = new File("CHAR_BATTLE_SPRITES/" + charName).listFiles();

            System.out.println(charName);
            if (!charName.equals(".DS_Store")) {
                for (int k = 0; k < anims.length; k++) {
                    String animName = anims[k].getName();
                    File[] frames = new File("CHAR_BATTLE_SPRITES/" + charName + "/" + animName).listFiles();

                    if (!animName.equals(".DS_Store")) {
                        for (int p = 0; p < frames.length; p++) {
                            String frameName = frames[p].getName();
                            frameName = frameName.substring(0, frameName.indexOf("."));
                            charSprites.put(charName + animName + frameName, ImageIO.read(frames[p]));
                        }
                    }
                }
            }
        }

        // loading backgrounds
        File[] backgrounds = new File("BATTLEMAP_SPRITES/" + mapName + "/Backgrounds").listFiles();
        for (int i = 0; i < backgrounds.length; i++) {
            String bgName = backgrounds[i].getName();
            mapBackgrounds.put(bgName.substring(0, bgName.indexOf(".")), ImageIO.read(backgrounds[i]));
        }
        // create backgrounds
        for (int i = 0; i < backgrounds.length; i++) {
            ImageElement bg = new ImageElement(0, 0, false, this, mapBackgrounds.get(String.valueOf(i)), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            sys.getAllScreenElements().add(bg);
            bg.setVisible(true);
        }

        // loading vfx sprites
        File[] vfxs = new File("VFX_SPRITES").listFiles();
        for (int i = 0; i < vfxs.length; i++) {
            String vfxName = vfxs[i].getName();

            if (!vfxName.equals(".DS_Store")) {
                File[] sprites = new File("VFX_SPRITES/" + vfxName).listFiles();

                for (int p = 0; p < sprites.length; p++) {
                    String frameName = sprites[p].getName();
                    frameName = frameName.substring(0, frameName.indexOf("."));
                    vfxSprites.put(vfxName + frameName, ImageIO.read(sprites[p]));
                }
            }
        }

        // loading map sprites
        File[] sprites = new File("BATTLEMAP_SPRITES/" + mapName + "/Sprites").listFiles();
        for (int i = 0; i < sprites.length; i++) {
            String spriteName = sprites[i].getName();
            mapSprites.put(spriteName.substring(0, spriteName.indexOf(".")), ImageIO.read(sprites[i]));
        }

        // loading move data
        while (moveDataSc.hasNextLine()) {
            // char name
            HashMap<String, int[]> characterInfo = new HashMap();
            ALL_MOVES.put(moveDataSc.nextLine(), characterInfo);
            // char moves
            String current = moveDataSc.nextLine();
            while (!current.equals("- - -")) {
                String moveName = current;
                String[] moveString = moveDataSc.nextLine().split(" ");
                int[] moveInfo = new int[moveString.length];
                for (int i = 0; i < moveString.length; i++) {
                    moveInfo[i] = Integer.parseInt(moveString[i]);
                }
                characterInfo.put(moveName, moveInfo);
                current = moveDataSc.nextLine();
            }
        }

        // creating tiles
        int tileIndex = 0;
        for (int tileY = 0; tileY < mapHeight; tileY++) {
            line = sc.nextLine();
            sp = line.split(" ");
            for (int tileX = 0; tileX < mapLength; tileX++) {
                if (!sp[tileX].equals("-")) {
                    ImageElement element = new ImageElement(tileX * Tile.TILE_SIZE, tileY * Tile.TILE_SIZE, true, this, mapSprites.get(sp[tileX]), Tile.TILE_SIZE, Tile.TILE_SIZE);
                    sys.getAllScreenElements().add(element);
                    element.setVisible(true);
                    Tile t = new Tile(sys, mapName, element, tileIndex);
                    tileMap[tileY][tileX] = t;

                    tileIndex += 1;
                }
            }
        }

        // create player
        this.player = new BattlePlayer(sys, playerX, playerY, "Yumi");
        this.chars.add(this.player);

        BattleNPC test = new BattleNPC(sys, 900, 400, "Arcueid");
        this.chars.add(test);

        BattleNPC test2 = new BattleNPC(sys, 1000, 400, "Arcueid");
        this.chars.add(test2);

        BattleNPC test3 = new BattleNPC(sys, 1100, 400, "Arcueid");
        this.chars.add(test3);

//        this.launchIndicator = new ImageElement(-200, 600, false, this, sys.getAllUiImages().get("launchIndicator"), 200, 50);
        this.launchIndicator = new TextElement(-200, 600, false, this, "LAUNCH!", sys.battleFont, Color.black);
        this.launchIndicator2 = new TextElement(-195, 600, false, this, "LAUNCH!", sys.battleFont, Color.red);
        launchIndicator.setVisible(true);
        launchIndicator2.setVisible(true);
        sys.getAllScreenElements().add(this.launchIndicator);
        sys.getAllScreenElements().add(this.launchIndicator2);

        System.out.println("STARTED BATTLE");
    }

    public void update() {
        float transX = (float) (player.getHitbox().getCenterX() - this.camX) / CAM_SMOOTHING;
        float transY = (float) (player.getHitbox().getCenterY() - this.camY) / CAM_SMOOTHING;
        updateCamera(this.camX + transX, this.camY + transY);

        worldX = ((sys.SCREEN_WIDTH / 2) - camX);
        worldY = ((sys.SCREEN_HEIGHT / 2) - camY);

        if (shakeDuration > 0) {
            shakeX = (int) (Math.random() * shakeFactor) - (shakeFactor / 2);
            shakeY = (int) (Math.random() * shakeFactor) - (shakeFactor / 2);
            shakeDuration -= 1;
        } else {
            shakeX = 0;
            shakeY = 0;
        }

        if (tweenOutLaunch > 0) {
            tweenOutLaunch -= 1;
            if (tweenOutLaunch == 0) {
                sys.addTween(new Tween(this.launchIndicator, 30, 600, -200, 600, 40, Tween.IN_OUT));
                sys.addTween(new Tween(this.launchIndicator2, 35, 600, -195, 600, 40, Tween.IN_OUT));
            }
        }

        if (hitstop == 0) {
            for (BattleChar c : chars) {
                c.attack();
                c.move(tileMap);
                c.update();
            }

            HashSet<Hitbox> temp = new HashSet<>();
            for (Hitbox h : hitboxes) {
                h.update(chars);
                if (h.isFinished()) {
                    temp.add(h);
                }
            }
            for (Hitbox h : temp) {
                hitboxes.remove(h);
            }

            for (VFX v : visibleVFX) {
                v.update();
            }
        } else {
            hitstop -= 1;
        }

    }

    public void updateCamera(float newX, float newY) {
        this.camX = newX;
        this.camY = newY;
    }
    public float getCamX() {
        return camX;
    }

    public float getCamY() {
        return camY;
    }

    public float getWorldX() {
        return worldX + shakeX;
    }

    public float getWorldY() {
        return worldY + shakeY;
    }

    public int getXZoomOffset() {
        return xZoomOffset;
    }

    public int getYZoomOffset() {
        return yZoomOffset;
    }

    public double getScaling() {
        return scaling;
    }

    public void setScaling(double scaling) {
        this.scaling = scaling;
    }

    public HashMap<String, BufferedImage> getCharSprites() {
        return charSprites;
    }

    public int getMapLength() {
        return mapLength;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public BattleChar getPlayer() {
        return player;
    }

    public Tile[][] getTileMap() {
        return tileMap;
    }

    public void setXZoomOffset(int xZoomOffset) {
        this.xZoomOffset = xZoomOffset;
    }

    public void setYZoomOffset(int yZoomOffset) {
        this.yZoomOffset = yZoomOffset;
    }

    public HashMap<String, BufferedImage> getVfxSprites() {
        return vfxSprites;
    }

    public HashSet<VFX> getVisibleVFX() {
        return visibleVFX;
    }

    public HashSet<Hitbox> getHitboxes() {
        return hitboxes;
    }

    public HashSet<BattleChar> getChars() {
        return chars;
    }

    public int[] getMoveData(String charName, String moveName) {
        return ALL_MOVES.get(charName).get(moveName);
    }
    public void setHitstop(int hitstop) {
        this.hitstop = hitstop;
    }
    public void setShakeDuration(int shakeDuration) {
        this.shakeDuration = shakeDuration;
    }
    public void launchScreenEffect() {
        this.shakeFactor = 20;
        setHitstop(25);
        setShakeDuration(20);
        sys.addTween(new Tween(this.launchIndicator, -200, 600, 30, 600, 30, Tween.OUT));
        sys.addTween(new Tween(this.launchIndicator2, -205, 600, 35, 600, 30, Tween.OUT));
        tweenOutLaunch = 60;
    }

    public void hitScreenEffect() {
        this.shakeFactor = 10;
        setHitstop(5);
        setShakeDuration(5);
    }
}