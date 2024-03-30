/**
 GraphicsPanel
 Name: Vincent Lam
 Description: The graphics module which is a subclass of JPanel for drawing all the game's components.
 Date: June 11, 2023
 **/

import org.w3c.dom.css.Rect;

import javax.swing.JPanel;
import java.awt.*;
import java.io.DataInput;
import java.util.ArrayList;
import java.util.HashSet;

public class GraphicsPanel extends JPanel {
    Game sys;

    /**
     * A constructor for the GraphicsPanel class. Takes in the Game instance.
     * @param sys The Game instance.
     */
    public GraphicsPanel(Game sys) {
        this.sys = sys;

        // Creating the screen dimensions
        this.setPreferredSize(new Dimension(sys.SCREEN_WIDTH, sys.SCREEN_HEIGHT));
        this.setBackground(Color.black);
//        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.requestFocus();
    }

    /**
     * paintComponent
     * Redraws all components in allScreenElements.
     * @param g the <code>Graphics</code> object to protect
     */

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Drawing all uiElements
        ArrayList<uiElement> allElements = sys.getAllScreenElements();

        for (uiElement currentElement : allElements) {
            if (currentElement.getVisible()) {

                if (currentElement instanceof ImageElement) {
                    ImageElement currentImage = (ImageElement) currentElement;

                    // transparency
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentElement.getTransparency()));

                    // if grid adjustable, translate by worldX and worldY
                    if (currentElement.isGridAdjustable()) {
                        if (currentElement.getParent() instanceof Grid) {
                            int scaledX = (int) ((currentElement.getElementX() + sys.getWorldX()) * sys.getCurrentMap().getScaling()) + sys.getCurrentMap().getXZoomOffset();
                            int scaledY = (int) ((currentElement.getElementY() + sys.getWorldY()) * sys.getCurrentMap().getScaling()) + sys.getCurrentMap().getYZoomOffset();
                            g2.drawImage(currentImage.getImage(), scaledX, scaledY, (int) (currentImage.getElementWidth() * sys.getCurrentMap().getScaling()), (int) (currentImage.getElementHeight() * sys.getCurrentMap().getScaling()), this);
                        } else if (currentElement.getParent() instanceof Battle) {
                            int scaledX = (int) ((currentElement.getElementX() + sys.getCurrentBattle().getWorldX()) * sys.getCurrentBattle().getScaling()) + sys.getCurrentBattle().getXZoomOffset();
                            int scaledY = (int) ((currentElement.getElementY() + sys.getCurrentBattle().getWorldY()) * sys.getCurrentBattle().getScaling()) + sys.getCurrentBattle().getYZoomOffset();

                            if (currentImage.isFlipped()) {
                                g2.drawImage(currentImage.getImage(), scaledX + currentImage.getElementWidth(), scaledY, (int) -(currentImage.getElementWidth() * sys.getCurrentBattle().getScaling()), (int) (currentImage.getElementHeight() * sys.getCurrentBattle().getScaling()), this);
                            } else {
                                g2.drawImage(currentImage.getImage(), scaledX, scaledY, (int) (currentImage.getElementWidth() * sys.getCurrentBattle().getScaling()), (int) (currentImage.getElementHeight() * sys.getCurrentBattle().getScaling()), this);
                            }
                        }
                    } else {
                        g2.drawImage(currentImage.getImage(), currentElement.getElementX(),currentElement.getElementY(), currentImage.getElementWidth(), currentImage.getElementHeight(), this);
                    }

                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

                } else if (currentElement instanceof TextElement) {
                    TextElement currentTextElement = (TextElement) currentElement;

                    // color and font
                    g2.setFont(currentTextElement.getFont());
                    g2.setColor(currentTextElement.getColor());

                    // if grid adjustable, translate by worldX and worldY
                    if (currentTextElement.isGridAdjustable()) {
                        g2.drawString(currentTextElement.getText(), currentElement.getElementX() + sys.getWorldX(), currentElement.getElementY() + sys.getWorldY());
                    } else {
                        g2.drawString(currentTextElement.getText(), currentElement.getElementX(), currentElement.getElementY());
                    }
                }
            }
        }

        // hitboxes!!!
        if (sys.getCurrentBattle() != null) {
//            g2.setStroke(new BasicStroke(3));
//            for (Hitbox h : sys.getCurrentBattle().getHitboxes()) {
//                Rectangle r = h.getRect();
//                if (h.state == Hitbox.STARTUP) {
//                    g2.setColor(Color.red);
//                    g2.drawRect((int) (r.getX() + sys.getCurrentBattle().getWorldX()), (int) (r.getY() + sys.getCurrentBattle().getWorldY()), (int) r.getWidth(), (int) r.getHeight());
//                } else if (h.state == Hitbox.ACTIVE) {
//                    g2.setColor(Color.green);
//                    g2.drawRect((int) (r.getX() + sys.getCurrentBattle().getWorldX()), (int) (r.getY() + sys.getCurrentBattle().getWorldY()), (int) r.getWidth(), (int) r.getHeight());
//                }
//            }
//
//            for (BattleChar c : sys.getCurrentBattle().getChars()) {
//                g.setColor(Color.blue);
//                Rectangle box = c.getHitbox();
//                g.drawRect((int) (box.getX() + sys.getCurrentBattle().getWorldX()), (int) (box.getY() + sys.getCurrentBattle().getWorldY()), (int) box.getWidth(), (int) box.getHeight());
//            }

            g.setFont(sys.battleFont);

            g.setColor(Color.BLACK);
            g.drawString(sys.getCurrentBattle().lastHitDamage + " DAMAGE (100%)", 30, 450);
            g.setColor(Color.WHITE);
            g.drawString(sys.getCurrentBattle().lastHitDamage + " DAMAGE (100%)", 35, 450);

            g.setColor(Color.BLACK);
            g.drawString(sys.getCurrentBattle().comboCount + " HIT COMBO", 30, 500);
            g.setColor(Color.ORANGE);
            g.drawString(sys.getCurrentBattle().comboCount + " HIT COMBO", 35, 500);

            g.setColor(Color.BLACK);
            g.drawString(sys.getCurrentBattle().comboDamage + " DAMAGE", 30, 550);
            g.setColor(Color.YELLOW);
            g.drawString(sys.getCurrentBattle().comboDamage + " DAMAGE", 35, 550);
        }

        if (sys.getCurrentMap() != null) {
            for (Being b : sys.getCurrentMap().getAllBeings()) {

                g.setColor(Color.red);
                
                if (b instanceof Boundary)  {
                    int scaledX = (int) ((b.getX() + sys.getWorldX()) * sys.getCurrentMap().getScaling()) + sys.getCurrentMap().getXZoomOffset();
                    int scaledY = (int) ((b.getY() + sys.getWorldY()) * sys.getCurrentMap().getScaling()) + sys.getCurrentMap().getYZoomOffset();
                    g.drawRect(scaledX, scaledY, (int) (b.getHitbox().getWidth() * sys.getCurrentMap().getScaling()), (int) (b.getHitbox().getHeight() * sys.getCurrentMap().getScaling()));
                }
                PlayerChar plr = sys.getCurrentMap().getPlayer();
                int scaledX = (int) ((plr.getX() + sys.getWorldX()) * sys.getCurrentMap().getScaling()) + sys.getCurrentMap().getXZoomOffset();
                int scaledY = (int) ((plr.getY() + sys.getWorldY()) * sys.getCurrentMap().getScaling()) + sys.getCurrentMap().getYZoomOffset();
                g.drawRect(scaledX, scaledY, (int) (plr.getHitbox().getWidth() * sys.getCurrentMap().getScaling()), (int) (plr.getHitbox().getHeight() * sys.getCurrentMap().getScaling()));

                g.setColor(Color.blue);
                int scaledXinteract = (int) ((plr.getInteractHitbox().getX() + sys.getWorldX()) * sys.getCurrentMap().getScaling()) + sys.getCurrentMap().getXZoomOffset();
                int scaledYinteract = (int) ((plr.getInteractHitbox().getY() + sys.getWorldY()) * sys.getCurrentMap().getScaling()) + sys.getCurrentMap().getYZoomOffset();
                g.drawRect(scaledXinteract, scaledYinteract, (int) (plr.getInteractHitbox().getWidth() * sys.getCurrentMap().getScaling()), (int) (plr.getInteractHitbox().getHeight() * sys.getCurrentMap().getScaling()));
            }
        }

        // dialogue
        Dialogue d = sys.getCurrentDialogue();
        if (d != null) {
            int state = d.getState();
            if ((state != Dialogue.ENDING) && (state != Dialogue.FINISHED)) {
                g.setColor(Color.white);
                g.setFont(sys.dialogueFont);
                g.drawString(d.getSpeakerName(), 365, 580);
                g.drawString(d.getText(), 380, 610);
            }
        }

        g2.dispose(); // this line saves memory apparently idk
    }
}
