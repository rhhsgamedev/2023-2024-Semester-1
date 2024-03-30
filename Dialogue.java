import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dialogue {
    private Game sys;
    private File info;
    private ImageElement dialogueBoxElement;
    private ImageElement dialogueFade;
    private ImageElement leftChar;
    private ImageElement rightChar;
    private Scanner input;
    private boolean startedDialogue;


    // dialogue states
    public static final int LOADING = 0;
    public static final int RUNNING = 1;
    public static final int ENDING = 2;
    public static final int FINISHED = 3;

    // other variables
    private static final int TYPE_DELAY = 2;
    private static final int FOCUS_X = 20;
    private static final int FOCUS_Y = 10;
    private int frameCounter;
    private String currentDialogue = "";
    private String currentDialogueDisplay = "";
    private String speakerName;
    private int state;
    private int leftCharFinalX;
    private int leftCharFinalY;
    private int rightCharFinalX;
    private int rightCharFinalY;

    Dialogue(Game sys, File info) throws FileNotFoundException {
        this.sys = sys;
        this.info = info;
        this.state = LOADING;
        this.frameCounter = 0;
        this.input = new Scanner(info);
        this.startedDialogue = false;
        this.speakerName = "";

        // importing images

        BufferedImage fade = sys.getAllUiImages().get("dialogueFade");
        this.dialogueFade = new ImageElement(0, 500, false, this, fade, fade.getWidth(), fade.getHeight());
        sys.getAllScreenElements().add(dialogueFade);
        dialogueFade.setVisible(true);

        BufferedImage box = sys.getAllUiImages().get("DialogueBox");
        this.dialogueBoxElement = new ImageElement(0, 300, false, this, box, box.getWidth(), box.getHeight());
        sys.getAllScreenElements().add(dialogueBoxElement);
        dialogueBoxElement.setVisible(true);
        sys.makeTopZOrder(dialogueBoxElement);

        sys.addTween(new Tween(dialogueFade, 0, 0, 60, Tween.IN_OUT));
        sys.addTween(new Tween(dialogueBoxElement, 0, 0, 40, Tween.OUT));
        sys.playSound("DialoguePrompt");
    }

    public void update() {

        if (state == RUNNING) {

            if (sys.isKeyPressed("e") || (!startedDialogue)) {
                startedDialogue = true;
                String line;
                if (input.hasNextLine()) {
                    while ((input.hasNextLine())) {
                        line = input.nextLine();

                        System.out.println(line);

                        if (line.contains("text: ")) {
                            currentDialogueDisplay = "";
                            currentDialogue = line.substring(6, line.length());
                            frameCounter = 0;
                        } else if (line.contains("sound: ")) {
                            sys.playSound(line.substring(7, line.length()));
                        } else if (line.contains("left char: ")) {

                            String[] info = line.substring(11, line.length()).split(" ");
                            String charName = info[0];
                            String expressionName = info[1];
                            int x = Integer.parseInt(info[2]);
                            int y = Integer.parseInt(info[3]);
                            int width = Integer.parseInt(info[4]);
                            int height = Integer.parseInt(info[5]);

                            BufferedImage left = (BufferedImage) sys.getAllCharDialogueSprites().get(charName + " " + expressionName);
                            this.leftCharFinalX = x;
                            this.leftCharFinalY = y;
                            this.leftChar = new ImageElement(x, 1000, false, this, left, width, height);
                            sys.addTween(new Tween(leftChar, x, y, 40, Tween.OUT));
                            sys.getAllScreenElements().add(leftChar);
                            leftChar.setVisible(true);

                        } else if (line.contains("right char: ")) {

                            String[] info = line.substring(12, line.length()).split(" ");
                            String charName = info[0];
                            String expressionName = info[1];
                            int x = Integer.parseInt(info[2]);
                            int y = Integer.parseInt(info[3]);
                            int width = Integer.parseInt(info[4]);
                            int height = Integer.parseInt(info[5]);

                            BufferedImage right = (BufferedImage) sys.getAllCharDialogueSprites().get(charName + " " + expressionName);
                            this.rightCharFinalX = x;
                            this.rightCharFinalY = y;
                            this.rightChar = new ImageElement(x, 1000, false, this, right, width, height);
                            sys.addTween(new Tween(rightChar, x, y, 40, Tween.OUT));
                            sys.getAllScreenElements().add(rightChar);
                            rightChar.setVisible(true);

                        } else if (line.contains("change left: ")) {

                            String[] info = line.substring(13, line.length()).split(" ");
                            String charName = info[0];
                            String expressionName = info[1];
                            BufferedImage left = (BufferedImage) sys.getAllCharDialogueSprites().get(charName + " " + expressionName);
                            this.leftChar.changeImage(left);

                        } else if (line.contains("change right: ")) {

                            String[] info = line.substring(14, line.length()).split(" ");
                            String charName = info[0];
                            String expressionName = info[1];
                            BufferedImage right = (BufferedImage) sys.getAllCharDialogueSprites().get(charName + " " + expressionName);
                            this.rightChar.changeImage(right);

                        } else if (line.equals("defocus left")) {
                            sys.addTween(new Tween(leftChar, leftCharFinalX, leftCharFinalY, leftCharFinalX - FOCUS_X, leftCharFinalY + FOCUS_Y, 60, Tween.OUT));
                            this.leftCharFinalX -= FOCUS_X;
                            this.leftCharFinalY += FOCUS_Y;
                            leftChar.toggleGrayScale(true);
                        } else if (line.equals("defocus right")) {
                            sys.addTween(new Tween(rightChar, rightCharFinalX, rightCharFinalY, rightCharFinalX + FOCUS_X, rightCharFinalY + FOCUS_Y, 60, Tween.OUT));
                            this.rightCharFinalX += FOCUS_X;
                            this.rightCharFinalY += FOCUS_Y;
                            rightChar.toggleGrayScale(true);
                        } else if (line.contains("speaker: ")) {
                            speakerName = line.substring(9, line.length());
                        } else if (line.equals("focus left")) {
                            sys.addTween(new Tween(leftChar, leftCharFinalX, leftCharFinalY, leftCharFinalX + FOCUS_X, leftCharFinalY - FOCUS_Y, 60, Tween.OUT));
                            this.leftCharFinalX += FOCUS_X;
                            this.leftCharFinalY -= FOCUS_Y;
                        } else if (line.equals("focus right")) {
                            sys.addTween(new Tween(rightChar, rightCharFinalX, rightCharFinalY, rightCharFinalX - FOCUS_X, rightCharFinalY - FOCUS_Y, 60, Tween.OUT));
                            this.rightCharFinalX -= FOCUS_X;
                            this.rightCharFinalY -= FOCUS_Y;
                        } else if (line.equals("- - -")) {
                            System.out.println("BREAK");
                            break;
                        }
                    }
                } else {
                    // end
                    state = ENDING;
                    frameCounter = 0;
                    sys.addTween(new Tween(dialogueFade, 0, 500, 60, Tween.IN_OUT));
                    sys.addTween(new Tween(dialogueBoxElement, 0, 300, 40, Tween.OUT));
                    if (leftChar != null) {
                        sys.addTween(new Tween(leftChar, leftCharFinalX, leftCharFinalY, leftChar.getElementX(), 1000, 40, Tween.OUT));
                    }
                    if (rightChar != null) {
                        sys.addTween(new Tween(rightChar, rightCharFinalX, rightCharFinalY,rightChar.getElementX(), 1000, 40, Tween.OUT));
                    }
                    sys.playSound("DialoguePrompt");
                }
            }

            if (!currentDialogue.equals("")) {
                // typewriter effect
                if (!currentDialogue.equals(currentDialogueDisplay)) {
                    if (frameCounter == TYPE_DELAY) {
                        frameCounter = 0;
                        // next character
                        currentDialogueDisplay += currentDialogue.substring(currentDialogueDisplay.length(), currentDialogueDisplay.length() + 1);
                    } else {
                        frameCounter += 1;
                    }
                }
            }

        } else if (state == LOADING) {
            frameCounter++;
            if (frameCounter == 40) {
                frameCounter = 0;
                state = RUNNING;
            }
        } else if (state == ENDING) {
            frameCounter++;
            if (frameCounter == 60) {
                // remove screen elements created in dialogue
                state = FINISHED;
                sys.getAllScreenElements().remove(dialogueBoxElement);
                sys.getAllScreenElements().remove(dialogueFade);
                if (this.leftChar != null) {
                    sys.getAllScreenElements().remove(this.leftChar);
                }
                if (this.rightChar != null) {
                    sys.getAllScreenElements().remove(this.rightChar);
                }
            }
        }
    }

    public String getText() {
        return currentDialogueDisplay;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public int getState() {
        return state;
    }
}
