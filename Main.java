/**
 Main
 Name: Vincent Lam
 Description: The executable of this program that creates the Game object and starts its loop.
 Date: June 11, 2023
 **/

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main {
    private static Game sys;
    private static int TICKRATE = 10;

    /**
     * main
     * The executable of this program.
     * @param args idk???
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException, CloneNotSupportedException {
        sys = new Game();
        runGameLoop();
    }

    /**
     * runGameLoop
     * Initializes the Game object, and starts the game loop running at TICKRATE frequency.
     * @throws IOException
     * @throws InterruptedException
     */

    private static void runGameLoop() throws IOException, InterruptedException, CloneNotSupportedException {
        // initialize game
        sys.start();

        // start loop
        while (sys.getRunning()) {
            sys.update();
            try  {Thread.sleep(TICKRATE);} catch(Exception e){}
        }
    }
}

