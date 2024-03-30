/**
 Game
 Name: Vincent Lam
 Description: The main module for updating all the game's components.
 Date: June 11, 2023
 **/

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioSystem;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Game {
    private Random rand = new Random();
    // initializing screen
    private JFrame screen;
    public GraphicsPanel canvas;

    // branches of the game state
    private Battle currentBattle;
    private Dialogue currentDialogue;
    private Grid currentMap;
    private boolean changingMap;

    // constants
    public static int CHAR_HITBOX_WIDTH = 70;
    public static int CHAR_HITBOX_HEIGHT = 20;
    public static int CHAR_DIMENSIONS = 100;
    public static int MANGAT_DIMENSIONS = 150;
    public static int SCREEN_WIDTH = 1280;
    public static int SCREEN_HEIGHT = 720;
    public static Font dialogueFont;
    public static Font battleFont;
    static {
        try {
            dialogueFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("nine0.ttf")).deriveFont(Font.PLAIN, 24);
            battleFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("nine0.ttf")).deriveFont(Font.BOLD, 45);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // transitioning between maps
    private ImageElement diagonalBlack;
    private int MAP_TRANSITION_FADE = 30;
    private int mapTransitionFrame = 0;

    // map info
    private String currentMapName;
    private int[] mapStartingCoords;

    // game loop and user input
    private boolean running;
    private HashSet<String> heldKeys = new HashSet<>();
    private HashSet<String> pressedKeys = new HashSet<String>();

    // Empty dictionaries for preloaded BufferedImages
    private HashSet<uiElement> topZOrder = new HashSet<>();
    private HashMap<String, BufferedImage> allCharGridImages = new HashMap<>();
    private HashMap<String, BufferedImage> allMapImages = new HashMap<>();
    private HashMap<String, Clip> allSounds = new HashMap<>();
    private HashMap<String, BufferedImage> allUiImages = new HashMap<>();
    private HashMap<String, BufferedImage> allItemSprites = new HashMap<>();
    private HashMap<String, BufferedImage> allCharDialogueSprites = new HashMap<>();
    private ArrayList<uiElement> allVisibleElements = new ArrayList<>();

    // inventory storage
    private Item[][] inventoryItems = new Item[3][3];

    // all mangat names

    // tweens
    private HashSet<Tween> allTweens = new HashSet<>();

    // inventory slot positions
    public static int ITEM_SLOT_X_START = 87;
    public static int ITEM_SLOT_Y_START = 306;
    public static int MANGAT_SLOT_X_START = 990;
    public static int MANGAT_SLOT_Y_START = 130;
    public static int SLOT_SIZE = 100;
    public static int ITEM_SLOT_GAP = 25;
    public static int MANGAT_SLOT_GAP = 20;

    // harpals

    private int harpals;

    // key handler
    private KeyHandler keyH = new KeyHandler(this);

    /**
     * A constructor for the Game class. Creates the screen, input listener, and preloads all assets.
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     * @throws InterruptedException
     */

    Game() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        // JFrame window
        this.screen = new JFrame("Game Window");
        screen.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setTitle("Venture M");
        screen.setResizable(false);

        // Graphics panel
        canvas = new GraphicsPanel(this);
        canvas.addKeyListener(keyH);
        screen.add(canvas);

        screen.pack();
        screen.setLocationRelativeTo(null);
        screen.setVisible(true);

        harpals = 0;
        changingMap = false;

        // NOTE: .DS_Store is a weird artifact found when I deleted images before and I couldn't get rid of them for some reason.

        // Loading map images
        File[] allMaps = new File("MAPS").listFiles();
        for (int i = 0; i < allMaps.length; i++) {
            String mapName = allMaps[i].getName();
            if (!mapName.equals(".DS_Store")) {
                mapName = mapName.substring(0, mapName.indexOf("."));
                allMapImages.put(mapName, ImageIO.read(allMaps[i]));
            }
        }

        // Loading character sprites
        File[] allChars = new File("CHAR_GRID_SPRITES").listFiles();

        // Character
        for (int i = 0; i < allChars.length; i++) {
            String charName = allChars[i].getName();
            if (!charName.equals(".DS_Store")) {
                File[] charSprites = new File("CHAR_GRID_SPRITES/" + charName).listFiles();

                // Movement direction
                for (int y = 0; y < charSprites.length; y++) {
                    String directionName = charSprites[y].getName();
                    if (!directionName.equals(".DS_Store")) {
                        File[] directionSprites = new File("CHAR_GRID_SPRITES/" + charName + "/" + directionName).listFiles();

                        // Animation frames
                        for (int z = 0; z < directionSprites.length; z++) {
                            String frameName = directionSprites[z].getName();
                            if (!frameName.equals(".DS_Store")) {
                                frameName = frameName.substring(0, frameName.indexOf("."));
                                allCharGridImages.put(charName + " " + directionName + " " + frameName, ImageIO.read(directionSprites[z]));
                            }
                        }
                    }
                }
            }
        }

        // Loading char dialogue sprites assets
        File[] allCharDialogueImgs = new File("DIALOGUE_SPRITES").listFiles();

        for (int i = 0; i < allCharDialogueImgs.length; i++) {

            String charName = allCharDialogueImgs[i].getName();

            if (!charName.equals(".DS_Store")) {
                File[] charSprites = new File("DIALOGUE_SPRITES/" + charName).listFiles();

                // Background images
                for (int y = 0; y < charSprites.length; y++) {

                    String imgName = charSprites[y].getName();

                    if (!imgName.equals(".DS_Store")) {
                        imgName = imgName.substring(0, imgName.indexOf("."));
                        this.allCharDialogueSprites.put(charName + " " + imgName, ImageIO.read(charSprites[y]));
                    }
                }
            }
        }


//        // Loading cutscene assets
//        File[] allCutsceneAssets = new File("CUTSCENE_ASSETS").listFiles();
//
//        // Asset types
//        for (int i = 0; i < allCutsceneAssets.length; i++) {
//            String assetType = allCutsceneAssets[i].getName();
//
//            // Backgrounds folder
//            if (assetType.equals("Backgrounds")) {
//                if (!assetType.equals(".DS_Store")) {
//                    File[] assetDirectory = new File("CUTSCENE_ASSETS/" + assetType).listFiles();
//                    if (!assetDirectory.equals(".DS_Store")) {
//
//                        // Background images
//                        for (int z = 0; z < assetDirectory.length; z++) {
//                            String assetImage = assetDirectory[z].getName();
//                            if (!assetImage.equals(".DS_Store")) {
//                                assetImage = assetImage.substring(0, assetImage.indexOf("."));
//                                this.allCutsceneAssets.put(assetType + " " + assetImage, ImageIO.read(assetDirectory[z]));
//                            }
//                        }
//                    }
//                }
//
//            // Character folder
//            } else if (assetType.equals("Characters")) {
//                if (!assetType.equals(".DS_Store")) {
//                    File[] assetDirectory = new File("CUTSCENE_ASSETS/" + assetType).listFiles();
//
//                    // character Names
//                    for (int y = 0; y < assetDirectory.length; y++) {
//                        String assetCatagory = assetDirectory[y].getName();
//                        if (!assetCatagory.equals(".DS_Store")) {
//                            File[] assetInnerCatagory = new File("CUTSCENE_ASSETS/" + assetType + "/" + assetCatagory).listFiles();
//
//                            // Character sprites
//                            for (int z = 0; z < assetInnerCatagory.length; z++) {
//                                String assetImage = assetInnerCatagory[z].getName();
//                                if (!assetImage.equals(".DS_Store")) {
//                                    assetImage = assetImage.substring(0, assetImage.indexOf("."));
//                                    this.allCutsceneAssets.put(assetType + " " + assetCatagory + " " + assetImage, ImageIO.read(assetInnerCatagory[z]));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

        // Loading UI images
        File[] allUis = new File("MAIN_UI").listFiles();
        for (int i = 0; i < allUis.length; i++) {
            String uiName = allUis[i].getName();
            uiName = uiName.substring(0, uiName.indexOf("."));
            allUiImages.put(uiName, ImageIO.read(allUis[i]));
        }

//        // Loading item sprites
//        File[] allItemFiles = new File("ITEM_SPRITES").listFiles();
//        for (int i = 0; i < allItemFiles.length; i++) {
//            String itemName = allItemFiles[i].getName();
//            itemName = itemName.substring(0, itemName.indexOf("."));
//            allItemSprites.put(itemName, ImageIO.read(allItemFiles[i]));
//        }

        // Loading sounds and music
        File[] allSoundFiles = new File("SOUNDS").listFiles();
        for (int i = 0; i < allSoundFiles.length; i++) {

            String soundName = allSoundFiles[i].getName();
            soundName = soundName.substring(0, soundName.indexOf("."));
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(allSoundFiles[i]);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            allSounds.put(soundName, clip);

        }

    }

    /**
     * start
     * Starts the update method and creates a new Grid instance.
     * @throws IOException
     */
    public void start() throws IOException, InterruptedException {
        // cutscene system
//
//        Cutscene cutsceneStart = new Cutscene();
//        this.currentCutscene = cutsceneStart;
//        //this.currentCutscene.CutsceneText();

        File mapFile = new File("MAP_INFO/Tokai.txt");
        this.currentMap = new Grid(mapFile, this);
        this.currentMapName = currentMap.getMapName();
        currentMap.init(null);
        running = true;

        BufferedImage diagonalImage = allUiImages.get("DiagonalBlack");
        this.diagonalBlack = new ImageElement(-diagonalImage.getWidth(), 0, false, this, diagonalImage);
        allVisibleElements.add(diagonalBlack);
        topZOrder.add(diagonalBlack);

//        BufferedImage testImage = allUiImages.get("DialogueBox");
//        ImageElement a = new ImageElement(0, 0, false, this, testImage);
//        allVisibleElements.add(a);
//        topZOrder.add(a);



//        // testing the inventory system
//        CatchItem testItem = new CatchItem("Novice MangatBall");
//        currentMap.addItem(testItem);
//
//        CatchItem testItem2 = new CatchItem("MangatBall");
//        currentMap.addItem(testItem2);
    }

    /**
     * update
     * Updates the current game state.
     * @throws IOException
     * @throws InterruptedException
     */

    public void update() throws IOException, InterruptedException, CloneNotSupportedException {

//        // Update one component of the game and freeze the others
//        if (this.currentCutscene != null) {
//            //this.currentCutscene.update();
//        } else if (this.currentBattle != null) {
//            this.currentBattle.update();
//        } else if ((this.currentMap != null) && !((changingMap) && (mapTransitionFrame >= 0) && (mapTransitionFrame <= MAP_TRANSITION_FADE))) {
//            this.currentMap.updateMap();
//        }

//        if ((this.currentMap != null) && !((changingMap) && (mapTransitionFrame >= 0) && (mapTransitionFrame <= MAP_TRANSITION_FADE))) {
//            this.currentMap.updateMap();
//        }


//        if (this.currentBattle != null){
//            this.currentBattle.update();
//        } else if (this.currentMap != null) {
//            this.currentMap.updateMap();
//        }

        if (this.currentBattle != null){
            this.currentBattle.update();
        }
        if (this.currentMap != null) {
            this.currentMap.updateMap();
        }

//        // map transition fade
//        if (this.changingMap) {
//            updateMapTransition();
//        }

        if (this.currentDialogue != null) {
            currentDialogue.update();
            if (currentDialogue.getState() == Dialogue.FINISHED) {
                this.currentDialogue = null;
            }
        }

        // move all top z order elements to last rendering order
//        for (uiElement e : topZOrder) {
//            allVisibleElements.remove(e);
//            allVisibleElements.add(e);
//        }

        // update all tweens
        HashSet<Tween> temp = new HashSet<>();
        for (Tween t : allTweens) {
            t.update();
            if (t.isFinished()) {
                temp.add(t);
            }
        }
        for (Tween t : temp) {
            allTweens.remove(t);
        }

        // redraw
        canvas.repaint();

        if (isKeyPressed("0")) {
            System.out.println("AAAA");
            deleteGrid();
            startBattle(new File("MapInfo.txt"));
            this.currentBattle.start();
        }

        // clear all pressed keys, as they should show up once per press
        pressedKeys.clear();
    }

    /**
     * getRunning
     * Getter method for if the game is running or not.
     * @return The running attribute as a boolean.
     */
    public boolean getRunning() {
        return running;
    }

    /**
     * getAllMapImages
     * Getter method for the Dictionary of loaded map images.
     * @return The loaded map images as a Dictionary.
     */
    public HashMap getAllMapImages() {
        return allMapImages;
    }

    /**
     * getAllCharGridSprites
     * Getter method for the Dictionary of character grid sprites.
     * @return The loaded character grid sprites as a Dictionary.
     */
    public HashMap getAllCharGridSprites() {
        return allCharGridImages;
    }

    /**
     * getAllCharDialogueSprites
     * Getter method for the Dictionary of character dialogue sprites.
     * @return The loaded character dialogue sprites as a Dictionary.
     */
    public HashMap getAllCharDialogueSprites() {
        return allCharDialogueSprites;
    }

    /**
     * getHeldKeys
     * Getter method for the ArrayList of keys held by the user.
     * @return The ArrayList of keys held.
     */
    public HashSet<String> getHeldKeys() {
        return heldKeys;
    }

    /**
     * getPressedKeys
     * Getter method for the ArrayList of keys pressed by the user (refreshes every update)
     * @return The ArrayList of keys pressed.
     */
    public HashSet<String> getPressedKeys() {
        return pressedKeys;
    }

    /**
     * isKeyHeld
     * Whether a certain key is currently being held by the user.
     * @param key The key to check as a String.
     * @return True if the key is being held, and false if the key is not being held.
     */
    public boolean isKeyHeld(String key) {
        if (heldKeys.contains(key)) {
            return true;
        }
        return false;
    }

    /**
     * isKeyPressed
     * Whether a certain key has been pressed by the user on the current update.
     * @param key The key to check as a String.
     * @return True if the key was pressed within the update, and false if the key was not pressed.
     */

    public boolean isKeyPressed(String key) {
        if (pressedKeys.contains(key)) {
            return true;
        }
        return false;
    }

    /**
     * getAllScreenElements
     * Getter method for the ArrayList of all uiElements to be displayed.
     * @return The allVisibleElements attribute as an ArrayList.
     */

    public ArrayList<uiElement> getAllScreenElements() {
        return allVisibleElements;
    }

    /**
     * getWorldX
     * Getter method for the worldX attribute of the Grid instance, if it exists.
     * @return Returns the worldX attribute as an Integer if a map is saved, and 0 if no map is saved.
     */
    public int getWorldX() {
        if (currentMap != null) {
            return currentMap.getWorldX();
        }
        return 0;
    }

    /**
     * getWorldY
     * Getter method for the worldY attribute of the Grid instance, if it exists.
     * @return Returns the worldY attribute as an Integer if a map is saved, and 0 if no map is saved.
     */
    public int getWorldY() {
        if (currentMap != null) {
            return currentMap.getWorldY();
        }
        return 0;
    }

    /**
     * getCurrentMap
     * Getter method for the saved map of the game.
     * @return The saved map of the game as a Grid instance.
     */

    public Grid getCurrentMap() {
        return currentMap;
    }

    /**
     * getSound
     * Getter method for the preloaded Clip instance with the same name within the allSounds dictionary.
     * @param name The name of the sound as a String.
     * @return The preloaded Clip instance of the sound.
     */
    public Clip getSound(String name) {
        Clip newSound = allSounds.get(name);
        return newSound;
    }

    /**
     * getAllUiImages
     * Getter method for the Dictionary of all preloaded BufferedImages.
     * @return The Dictionary of all preloaded BufferedImages.
     */
    public HashMap<String, BufferedImage> getAllUiImages() {
        return allUiImages;
    }

    /**
     * getInventoryItems
     * Getter method for the Item array of all inventory items.
     * @return The Item array of all inventory items.
     */
    public Item[][] getInventoryItems() {
        return inventoryItems;
    }

    /**
     * getAllItemSprites
     * Getter method for the Dictionary of all Item sprites.
     * @return The Dictionary of all Item sprites.
     */
    public HashMap<String, BufferedImage> getAllItemSprites() {
        return allItemSprites;
    }

    /**
     * playSound
     * Plays a sound from its name in the preloaded dictionary allSounds.
     * @param name The name of the sound to be played as a String.
     */
    public void playSound(String name) {
        // may replace this in the future cuz it lags the game everytime u play a sound
        // fuck
        Clip s = getSound(name);
        s.stop();
        s.setMicrosecondPosition(0);
        s.start();
    }

    /**
     * isInventoryFull
     * Whether the current inventory state can hold more items or not.
     * @return True if full, false if not full
     */
    public boolean isInventoryFull() {
        int inventoryCount = 0;
        for (int i = 0; i < inventoryItems[0].length; i++) {
            for (int k = 0; k < inventoryItems.length; k++) {
                if (inventoryItems[k][i] != null) {
                    inventoryCount++;
                }
            }
        }
        if (inventoryCount < inventoryItems[0].length * inventoryItems.length) {
            return false;
        }
        return true;
    }

    /**
     * getHarpals
     * Getter method for the amount of Harpals.
     * @return The amount of harpals as an Integer.
     */

    public int getHarpals() {
        return harpals;
    }

    /**
     * deleteGrid
     * Clears the current map and all of its elements, and stops its background music.
     */
    public void deleteGrid() {
        if (this.currentMap != null) {
            this.currentMap.delete();
            this.currentMap = null;
        }
    }

    /**
     * changeMap
     * Changes the current map to a new one.
     * @param newName The name of the new map as a String.
     * @param newCoords The coordinates for the player character to start at in the new map.
     */

    public void changeMap(String newName, int[] newCoords) {
        // start the map transition
        changingMap = true;
        currentMapName = newName;
        mapStartingCoords = newCoords;
        this.diagonalBlack.setVisible(true);
        diagonalBlack.setElementX(-diagonalBlack.getImage().getWidth());
        addTween(new Tween(diagonalBlack, -diagonalBlack.getImage().getWidth() + SCREEN_WIDTH + 800, 0, MAP_TRANSITION_FADE, 2));
    }

    /**
     * stopSound
     * Stops the sound with the name from playing.
     * @param name The name of the sound to stop playing.
     */
    public void stopSound(String name) {
        Clip s = getSound(name);
        s.stop();
        s.setMicrosecondPosition(0);
    }

    /**
     * addHarpal
     * Increases the value of Harpals by a certain amount.
     * @param addition The number to increase Harpals by as an integer.
     */
    public void addHarpals(int addition) {
        harpals += addition;
    }

    public Battle getCurrentBattle() {
        return currentBattle;
    }
    public void startBattle(File mapFile) throws IOException {
        this.currentBattle = new Battle(this, mapFile);
    }

    /**
     * getScreen
     * Getter method for the screen.
     * @return The screen as a JFrame.
     */
    public JFrame getScreen() {
        return screen;
    }

    /**
     * addTween
     * Queues a new Tween to be updated in the ArrayList of all tweens.
     * @param newTween The new tween to be updated.
     */
    public void addTween(Tween newTween) {
        Tween temp = null;

        // search for tween using same element
        for (Tween t : allTweens) {
            if (t.getElement() == newTween.getElement()) {
                temp = t;
            }
        }
        // remove existing tween
        if (temp != null) {
            uiElement e = temp.getElement();
            allTweens.remove(temp);
            e.setElementX(temp.getEndValueX());
            e.setElementX(temp.getEndValueY());
        }
        allTweens.add(newTween);
    }

    /**
     * updateMapTransition
     * Updates the visual transition towards changing to a new map.
     * @throws FileNotFoundException
     */
    public void updateMapTransition() throws FileNotFoundException {
        // delete grid behind black screen and create new grid
        if (mapTransitionFrame == 30) {
            deleteGrid();
            File mapFile = new File("MAP_INFO/" + currentMapName + ".txt");
            this.currentMap = new Grid(mapFile, this);
            currentMap.init(mapStartingCoords);
        // move the black screen back to its original position
        } else if (mapTransitionFrame == 45) {
            addTween(new Tween(diagonalBlack, -diagonalBlack.getImage().getWidth(), 0, MAP_TRANSITION_FADE, 2));
        // end map transition
        } else if (mapTransitionFrame == 45 + MAP_TRANSITION_FADE) {
            changingMap = false;
            mapTransitionFrame = 0;
        }
        mapTransitionFrame++;
    }

    /**
     * makeTopZOrder
     * Sets the rendering order of an uiElement to be the last in allVisibleElements.
     * @param newElement
     */
    public void makeTopZOrder(uiElement newElement) {
        topZOrder.add(newElement);
    }

    public void createInteraction(File f) throws FileNotFoundException {
        this.currentDialogue = new Dialogue(this, f);
        System.out.println("CREATED");
    }

    public Dialogue getCurrentDialogue() {
        return currentDialogue;
    }


}
