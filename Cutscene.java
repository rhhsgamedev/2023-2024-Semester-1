import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.PrintWriter;

public class Cutscene implements ActionListener{

    JTextArea textArea;
    String text;
    int i = 0;

    //JFrame screen;

    boolean typingFinished = false;
    boolean nextLine = false;

    JButton button;

    int imageCounter;

    int lineCounter;

    String imageNum;

    JFrame screen;

    Container con;

    JPanel backgroundPanel;

    JPanel panel;

    Game sys;
    Cutscene() {
        this.sys = sys;
    }


    private JLabel titlesetLayer1 = new JLabel();
    private JLabel titlesetLayer2[] = new JLabel[1];
    private JLabel titlesetLayer3[] = new JLabel[1];
    private JLabel titlesetLayer4[] = new JLabel[0];
    private JLabel titlesetLayer5[] = new JLabel[0];
    private JLabel characters[] = new JLabel[2];

/*
    public static void main(String[] args) throws IOException, InterruptedException{

        new displayCutscene(screen);

    }
 */

    //modifies the text file so that it adds Username
    public void CutsceneText() throws IOException, InterruptedException {

         imageCounter = 0;
        lineCounter = 0;
         imageNum = "";
        String protagonistName = "Bob";
        boolean protagonistGender = true;
        JTextField textField = new JTextField();
        Scanner sc = new Scanner(System.in);


        screen = new JFrame("Cutscene");
        screen.setSize(1450, 875);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.getContentPane().setBackground(Color.WHITE);
        screen.setLayout(null);
        con = screen.getContentPane();


//Creates a copy of the dialogue with the appropriate pronouns and the name that the user has chosen
        File oldFile = new File("Dialogue.txt");

        try {
            File newFile = new File("modifiedDialogueFile.txt");
            PrintWriter output = new PrintWriter(newFile);
            Scanner input = new Scanner(oldFile);

            while (input.hasNext()) {
                String line = input.nextLine();
                output.println(line.replace("PROTAGONISTNAME", protagonistName));
            }

            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        };

        //sends it to updateDialogue which takes in updated file and outputs dialogue
        updateDialogue();

    }

    //Gets dialogue and sends it to displayDialogue to dislpay it
    public void updateDialogue() throws IOException, InterruptedException {

        System.out.println("Hello there");
        clearFrame();

        String line = Files.readAllLines(Paths.get("modifiedDialogueFile.txt")).get(lineCounter);
        lineCounter++;

        if(line.equals("NextImage")){
            imageCounter++;
            updateDialogue();
        }else if(line.equals("END")){
            screen.dispose();

        }else{
            displayCutscene(line);
        }



/*
        File dialogueFile = new File("modifiedDialogueFile.txt");

        try {
            Scanner input = new Scanner(dialogueFile);

            while (input.hasNext()) {
                System.out.println(lineCounter);
                String line = input.nextLine();
                System.out.println("Current Dialogue: " + line);
                lineCounter++;

                if (line.equals("NextImage")) {
                    imageCounter++;
                }else{
                    //actually display the images
                    displayCutscene(line, imageNum, imageCounter, lineCounter, sys, screen);
                }

            }

            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        };

*/
    }

    //Displays the actual cutscene by taking in text from the method updateDialogue
    public void displayCutscene(String line) throws IOException, InterruptedException {

        Font textFont = new Font("Times New Roman", Font.PLAIN, 28);

        imageNum = Integer.toString(imageCounter);

        con.removeAll();

        //Creates a text box with inputs and outputs
        JPanel textPanel = new JPanel();
        textPanel.setBounds(300, 625, 900, 200);
        textPanel.setBackground(Color.PINK);
        screen.add(textPanel);

        textArea = new JTextArea();
        textArea.setBounds(300, 625, 900, 200);
        textArea.setBackground(Color.PINK);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(textFont);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textPanel.add(textArea);


        if (lineCounter >= 3 && imageCounter == 0) {

            //Creates the characters
            Image character1 = ImageIO.read(new File(".//CUTSCENE_ASSETS//Characters//Sae//Neutral.png"));
            ImageIcon characterIcon1 = new ImageIcon(character1);

            characters[1] = new JLabel(characterIcon1);
            characters[1].setBounds(1000, 100, 244, 600);
            screen.add(characters[1]);


            Image character2 = ImageIO.read(new File(".//CUTSCENE_ASSETS//Characters//Hato//Neutral.png"));
            ImageIcon characterIcon2 = new ImageIcon(character2);

            characters[0] = new JLabel(characterIcon2);
            characters[0].setBounds(300, 150, 265, 600);
            screen.add(characters[0]);

        }else if (lineCounter >= 21 && imageCounter == 1) {

            //Creates the characters
            Image character1 = ImageIO.read(new File(".//CUTSCENE_ASSETS//Characters//Sae//Neutral.png"));
            ImageIcon characterIcon1 = new ImageIcon(character1);

            characters[1] = new JLabel(characterIcon1);
            characters[1].setBounds(1000, 100, 244, 800);
            screen.add(characters[1]);


            Image character2 = ImageIO.read(new File(".//CUTSCENE_ASSETS//Characters//Hato//Angry.png"));
            ImageIcon characterIcon2 = new ImageIcon(character2);

            characters[0] = new JLabel(characterIcon2);
            characters[0].setBounds(300, 150, 265, 600);
            screen.add(characters[0]);


        }else if (lineCounter >= 30 && imageCounter == 2) {

            //Creates the characters
            Image character2 = ImageIO.read(new File(".//CUTSCENE_ASSETS//Characters//Misaki//Happy.png"));
            ImageIcon characterIcon2 = new ImageIcon(character2);

            characters[0] = new JLabel(characterIcon2);
            characters[0].setBounds(300, 250, 362, 554);
            screen.add(characters[0]);


        } else if (lineCounter >= 32 && imageCounter == 2) {

            //Creates the characters
            Image character2 = ImageIO.read(new File(".//CUTSCENE_ASSETS//Characters//Misaki//Neutral.png"));
            ImageIcon characterIcon2 = new ImageIcon(character2);

            characters[0] = new JLabel(characterIcon2);
            characters[0].setBounds(300, 250, 362, 554);
            screen.add(characters[0]);

        } else if (imageCounter == 3) {

            //Creates the characters
            Image character1 = ImageIO.read(new File(".//CUTSCENE_ASSETS//Characters//Professor//TheMAPLE.png"));
            ImageIcon characterIcon1 = new ImageIcon(character1);

            characters[1] = new JLabel(characterIcon1);
            characters[1].setBounds(700, 100, 333, 799);
            screen.add(characters[1]);


            Image character2 = ImageIO.read(new File(".//CUTSCENE_ASSETS//Characters//Misaki//Happy.png"));
            ImageIcon characterIcon2 = new ImageIcon(character2);

            characters[0] = new JLabel(characterIcon2);
            characters[0].setBounds(300, 250, 324, 556);
            screen.add(characters[0]);

        }



        //Creates the background
      /*  BufferedImage background = ImageIO.read(new File(".//CUTSCENE_ASSETS//Backgrounds//" + imageNum + ".png"));
        Image backgroundImage = background.getScaledInstance(1600, 850, Image.SCALE_SMOOTH);
        ImageIcon backgroundIcon = new ImageIcon(backgroundImage);

        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(backgroundIcon);
        screen.add(backgroundLabel);

       */


        backgroundPanel = new JPanel();
        backgroundPanel.setBounds(0, 0, 1450, 875);
        backgroundPanel.setBackground(Color.WHITE);

        JLabel backgroundLabel = new JLabel();

        BufferedImage background = ImageIO.read(new File(".//CUTSCENE_ASSETS//Backgrounds//" + imageNum + ".png"));
        Image backgroundImage = background.getScaledInstance(1600, 850, Image.SCALE_SMOOTH);
        ImageIcon backgroundIcon = new ImageIcon(backgroundImage);

        //ImageIcon backgroundIcon = new ImageIcon(".//CUTSCENE_ASSETS//Backgrounds//" + imageNum + ".png");

        backgroundLabel.setIcon(backgroundIcon);
        backgroundPanel.add(backgroundLabel);
        screen.add(backgroundPanel);


        button = new JButton("Next");
        button.setPreferredSize(new Dimension(200, 100));
        button.addActionListener(this);

        panel = new JPanel();
        panel.setBounds(0, 650, 200, 100);
        //panel.setBorder(BorderFactory.createEmptyBorder(700, 800, 0, 0));
        //panel.setLayout(new GridLayout(0,1));
        panel.add(button);

        screen.add(panel);

        screen.setVisible(true);


        text = line;

        textArea.append(text);



        //timer.start();

/*
        ////////////////////////////////////////////////
        //program will do nothing until e is pressed
            while(nextLine != true){
                if (sys.isKeyPressed("e")) {
                    nextLine = true;
                }

            }

*/
        typingFinished = false;
        nextLine = false;


    }

    Timer timer = new Timer(50, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            char character[] = text.toCharArray();
            int arrayNumber = character.length;

            String addedCharacter = "";
            String blank = "";

            addedCharacter = blank + character[i];
            textArea.append(addedCharacter);

            i++;
            if (i == arrayNumber) {
                i = 0;
                typingFinished = true;
                timer.stop();
            }
        }
    });

/*
    private boolean pressEnterToContinue() {
        System.out.println("Press Enter key to continue...");
        try {
            System.in.read();
            return true;
        } catch (Exception e) {
        }
        return false;
    }
    */

@Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == button) {
            con.removeAll();
            backgroundPanel.removeAll();

            if(characters[0] != null){
                characters[0].removeAll();
            }
            if(characters[1] != null){
                characters[1].removeAll();
            }
            /*backgroundPanel.removeAll();
            characters[0].removeAll();
            characters[1].removeAll();
            panel.removeAll();
             */
            System.out.println("Hello world");
            try {
                updateDialogue();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }


    }

    public void clearFrame () {

        screen.getContentPane().removeAll();
        screen.getContentPane().revalidate();
        screen.getContentPane().repaint();



    }





}

