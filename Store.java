/**
 * Store
 * @author Robbin Shen
 * Description: Creates the actions that class Store can do
 * June 12, 2023
 */

// HARDCODED

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Store implements ActionListener {
  
    private Game sys;

    public int imageNum = 1;
    public String imagePic = " ";
    public String itemID = "";
    public boolean add;
    public int cost = 50;
    public JFrame screen;
    public Container con;
  
    public JButton button1;
    public JButton button2;
    public JButton button3;
    public JButton button4;
    public JButton button5;
    public JButton button6;

    public JTextArea textArea;
    public JPanel panel;

    public JPanel backgroundPanel;

    public String button1Text;
    public String button2Text;
    public String button3Text;
    public String button4Text;
    public String button5Text;
    public String button6Text;
    public String text;
    public String line;

    public boolean descriptionBox = false;
    public boolean running = true;


    /**
     * Store
     * A constructor for the Store class
    */
    Store() {
      this.sys = sys;
    }

    /**
     * storeStart
     * Initializes the store system.
     * @return returns a boolean to end the screen for the user in the store
     * @throws IOException
     * @throws InterruptedException
     */
  
    public boolean storeStart() throws IOException, InterruptedException {

        System.out.println("Arrived in Store");

        screen = new JFrame("Store");
        screen.setSize(1450, 875);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.getContentPane().setBackground(Color.WHITE);
        screen.setLayout(null);
        con = screen.getContentPane();
        updateDisplay();

        while(running =  true) {
            if (running = false) {
                screen.dispose();
                return true;
            }

        }
        return false;
    }

    /**
    * updateDisplay
    * updates the description of the items and the store
    **/
    public void updateDisplay() throws IOException, InterruptedException {

        clearFrame();
        add = false;
        cost = 0;

        if(imageNum == 1){

            button1Text = "back";
            button2Text = "Attacking";
            button3Text = "Healing";
            button4Text = "Mangat Balls";
            button5Text = "null";
            button6Text = "null";

            displayShop(line);

        }else if(imageNum == 2 || imageNum == 3 || imageNum == 4) {
            button1Text = "back";
            button2Text = "1";
            button3Text = "2";
            button4Text = "3";
            button5Text = "4";
            button6Text = "buy";

            displayShop(line);

        }


    }

    /**
    displayShop
    Brings up the shop UI for the user.
    @param line The current dialogue line to display as a String.
    **/
  
    public void displayShop(String line) throws IOException, InterruptedException {


        imagePic = Integer.toString(imageNum);

        Font textFont = new Font("Times New Roman", Font.PLAIN, 22);

        con.removeAll();

        if(descriptionBox == true){
            //Creates a text box with inputs and outputs
            JPanel textPanel = new JPanel();
            textPanel.setBounds(275, 550, 900, 150);
            textPanel.setBackground(Color.GRAY);
            screen.add(textPanel);

            textArea = new JTextArea();
            textArea.setBounds(275, 550, 900, 150);
            textArea.setBackground(Color.GRAY);
            textArea.setForeground(Color.WHITE);
            textArea.setFont(textFont);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textPanel.add(textArea);

        }


//Creates the background
        backgroundPanel = new JPanel();
        backgroundPanel.setBounds(0, 0, 1450, 700);
        backgroundPanel.setBackground(Color.WHITE);
        JLabel backgroundLabel = new JLabel();
        BufferedImage background = ImageIO.read(new File(".//Shop//" + imagePic + ".png"));
        Image backgroundImage = background.getScaledInstance(1450, 700, Image.SCALE_SMOOTH);
        ImageIcon backgroundIcon = new ImageIcon(backgroundImage);
        backgroundLabel.setIcon(backgroundIcon);
        backgroundPanel.add(backgroundLabel);
        screen.add(backgroundPanel);

        //Creates the buttons
        button1 = new JButton(button1Text);
        button1.setPreferredSize(new Dimension(100, 50));
        button1.addActionListener(this);

        button2 = new JButton(button2Text);
        button2.setPreferredSize(new Dimension(100, 50));
        button2.addActionListener(this);

        button3 = new JButton(button3Text);
        button3.setPreferredSize(new Dimension(100, 50));
        button3.addActionListener(this);

        button4 = new JButton(button4Text);
        button4.setPreferredSize(new Dimension(100, 50));
        button4.addActionListener(this);

        button5 = new JButton(button5Text);
        button5.setPreferredSize(new Dimension(100, 50));
        button5.addActionListener(this);

        button6 = new JButton(button6Text);
        button6.setPreferredSize(new Dimension(100, 50));
        button6.addActionListener(this);

        panel = new JPanel();
        panel.setBounds(0, 700, 1450, 100);
        //panel.setBorder(BorderFactory.createEmptyBorder(700, 800, 0, 0));
        //panel.setLayout(new GridLayout(0,1));
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(button5);
        panel.add(button6);

        screen.add(panel);


        screen.setVisible(true);
if(descriptionBox == true){
    textArea.append(text);
}

    }

    /**
    buyItem
    Controls the buying and adding the item to the player inventory
    **/
  
    public void buyItem() throws IOException, InterruptedException{

        //Gets the item cost
        if(cost > sys.getHarpals()){
            System.out.println("You can't buy this ");
        } else if (cost < sys.getHarpals()){
        }
    }


    /**  
    clearFrame
    Removes all components from the screen.
    */

    public void clearFrame() {
        screen.getContentPane().removeAll();
        screen.getContentPane().revalidate();
        screen.getContentPane().repaint();

    }

  
    /**
     * actionPerformed
     * @param e Creates all of the conditions for the buttons and what happens when you click on them
     * @param e the event to be processed
     */

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();

        if (obj == button1) {
            con.removeAll();
            backgroundPanel.removeAll();

            if(imageNum == 1){
                text = "Store has been closed";
                screen.dispose();

            }else if(imageNum == 2){
                imageNum = 1;
                descriptionBox = false;

            }else if(imageNum == 3){
                imageNum = 1;
                descriptionBox = false;

            }else if(imageNum == 4){
                imageNum = 1;
                descriptionBox = false;

            }


            try {
                updateDisplay();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
        if (obj == button2) {
            con.removeAll();
            backgroundPanel.removeAll();

            if(imageNum == 1){
                imageNum = 3;
                descriptionBox = false;

            }else if(imageNum == 2){
                text = "A spray-painted cherry that somehow increases vitality. +10% health, costs 30";
                cost = 30;
                itemID = "GoldenCherry";
                descriptionBox = true;

            }else if(imageNum == 3){
                text = "To some a drug, but to others a miracle medicine. + 5% attack, cost 10";
                cost = 10;
                itemID = "Mushroom";
                descriptionBox = true;

            }else if(imageNum == 4){
                text = "A device used for capturing Mangats. + %, cost 20";
                cost = 20;
                itemID = "MangatBall";
                descriptionBox = true;

            }


            try {
                updateDisplay();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
        if (obj == button3) {
            con.removeAll();
            backgroundPanel.removeAll();

            if(imageNum == 1){
                imageNum = 2;
                descriptionBox = false;

            }else if(imageNum == 2){
                text = "Harvested from the mountains of Kanto. +5% health, costs 5";
                itemID = "ShinglyRoot";
                cost = 5;
                descriptionBox = true;

            }else if(imageNum == 3){
                text = "The #1 Ventureer-approved item boost! +10% attack, costs 50";
                itemID = "HyperPotion";
                cost = 50;
                descriptionBox = true;


            }else if(imageNum == 4){
                text = "A staple among rising Ventureers. + %, costs 50";
                itemID = "NoviceBall";
                cost = 50;
                descriptionBox = true;

            }


            try {
                updateDisplay();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
        if (obj == button4) {
            con.removeAll();
            backgroundPanel.removeAll();

            if(imageNum == 1){
                imageNum = 4;
                descriptionBox = false;

            }else if(imageNum == 2){
                text = "Grants the power of a hundred cows. +25% health, costs 10";
                itemID = "MooMooMilk";
                cost = 10;
                descriptionBox = true;

            }else if(imageNum == 3){
                text = "White flower + fine powder + drop of elixir. Endorsed by Waruto Black. +25% attack, costs 100";
                itemID = "WhiteCrystalPowder";
                cost = 100;
                descriptionBox = true;

            }else if(imageNum == 4){
                text = " For the big-league Mangats. + %, costs 100";
                itemID = "MasterBall";
                cost = 100;
                descriptionBox = true;

            }

            try {
                updateDisplay();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
        if (obj == button5) {
            con.removeAll();
            backgroundPanel.removeAll();

            if(imageNum == 2){
                text = "A needle and a mysterious serum. What could possibly go wrong? + 35%, costs 20";
                itemID = "Stimpack";
                cost = 20;
                descriptionBox = true;

            }else if(imageNum == 3){
                text = "My lawyer has advised me not to continue this sentence. +100% attack, costs 200 ";
                itemID = "JungleJuice";
                cost = 200;
                descriptionBox = true;

            }else if(imageNum == 4){
                text = " Guaranteed to capture a Mangat when used. + %, costs 200";
                itemID = "UltrBall";
                cost = 200;
                descriptionBox = true;

            }


            try {
                updateDisplay();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
        if (obj == button6) {
            con.removeAll();
            backgroundPanel.removeAll();

            if(imageNum == 2){

                try {
                    buyItem();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                if(add == true){
                    text = "Succesfully Bought!";
                }else if(add == false){
                    text = "Not enough harpals";
                }

            }else if(imageNum == 3){
                text = "buy ";

                try {
                    buyItem();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                if(add == true){
                    text = "Succesfully Bought!";
                }else if(add == false){
                    text = "Not enough harpals";
                }

            } else if(imageNum == 4){

                try {
                    buyItem();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                if(add == true){
                    text = "Succesfully Bought!";
                }else if(add == false){
                    text = "Not enough harpals";
                }

            }
            try {
                updateDisplay();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}