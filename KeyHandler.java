/**
 KeyHandler
 Name: Vincent Lam
 Description: A subclass of Being on the grid representing a spawn area for ItemBeings.
 Date: June 11, 2023
 **/

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;

public class KeyHandler implements KeyListener {
    private Game sys;

    /**
     * A constructor for the KeyHandler class. Takes in the Game instance to record the inputs in.
     * @param sys The Game instance.
     */
    KeyHandler(Game sys) {
        this.sys = sys;
    }

    /**
     * keyTyped
     * A method that runs whenever the keyboard registers an input from the user.
     * @param e The event to be processed
     */

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * keyPressed
     * A method that runs whenever the keyboard registers a single down press from the user of a key.
     * @param e the event to be processed
     */

    @Override
    public void keyPressed(KeyEvent e) {
        String code = String.valueOf(e.getKeyChar());
        // prevents duplicates from being added
        HashSet<String> currentHeldKeys = sys.getHeldKeys();
        HashSet<String> currentPressedKeys = sys.getPressedKeys();
        System.out.println(code);
        if (currentHeldKeys.contains(code)) {
            return;
        }
        currentHeldKeys.add(code);
        currentPressedKeys.add(code);
    }

    /**
     * keyReleased
     * A method that runs whenever the keyboard registers the release of a key.
     * @param e the event to be processed
     */

    @Override
    public void keyReleased(KeyEvent e) {
        String code = String.valueOf(e.getKeyChar());
        sys.getHeldKeys().remove(code);
    }
}
