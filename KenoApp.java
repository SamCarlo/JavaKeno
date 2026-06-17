/**
* GUI components and listeners for Keno game.
* @author: Samuel Carlos.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.io.*;
         

/**
* The app class for the Keno game.
* Defines JFrame, Jpanels, and Listener buttons. 
*/
public class KenoApp extends JFrame{
   private KenoModel game;                         //Instance of game data and functions. 
   
   private GridPanel gridPanel;                    //JPanel that will hold all 40 Keno board options.
   
   private OptionsPanel optionsPanel;              //JPanel that will hold Bet, Play, End, and Instructions options.
   
   private final int BUTTONCOUNT = 40;             //Number of buttons in the array; for searching button array in listener.
   
   private KenoButton[] buttons;                   //Array containing all gameboard buttons.
   
   private Choice bet;                             //Betting selection menu.
   
   private JLabel betLabel;                        //Message above betting Choice menu.
   
   private JButton play;                           //Play button.
   
   private JButton end;                            //End button.
   
   private JButton instructions;                   //Instructions button.
   
  // private JLabel totalEarningsLabel;              //JLabel displaying total earnings in one gameplay cycle.
   
   private boolean clickMax;                       //Flag for determining button behavior if user clicks 10 buttons.
   
   private File file;                              //Output .txt file to generate upon exit.
   
   
   public KenoApp(){
      // Initialize Frame
      this.setSize(1000, 700);                 // Initialize size to a landscape
      this.setTitle("Easy Keno");              // Title
      this.setLayout(new BorderLayout());      // Set frame to a N, S, E, W layout
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);
      this.clickMax = false; 
      
   } // End frame constructor
   
   /**
   * Init GUI: Add panels, listeners, and objects into the frame.
   *
   */
   public void initializeGUI(){
      // Initialize Center Panel (Grid)
      gridPanel = new GridPanel();       // Panel containing 40 Keno buttons
      
      // Initialize Game Options Panel
      optionsPanel = new OptionsPanel(); // Panel with  Bet, Play, End, and Instructions buttons
      
      
      // Add panels to this KenoApp frame.
      this.add("North", new JLabel("Easy Keno! :)"));      //Title
      this.add("Center", gridPanel);                       //Keno grid buttons panel
      this.add("West", optionsPanel);                      //Options panel on left
      
      //A Listener
      ActionListener listener = new KenoListener();
      
      //Add listener to all 40 Keno buttons
      JButton [] gridButtons = gridPanel.getAllGridButtons();     
      for (JButton button : gridButtons){
         button.addActionListener(listener);
      }
      
      //Add listener to play, end, and instructions buttons
      play.addActionListener(listener);
      end.addActionListener(listener);
      instructions.addActionListener(listener);
     
   }
   
   /**
   * JButton class for Keno game board buttons.
   * Holds and modifies: 
   * 1) toggle-based booleans to indicate clicked; 
   * 2) corresponding color state depending on click status.
   * 3) Numerical Label corresponding to location on Keno board.
   * 4) The index of the button as it lays in the grid panel.
   */
   public class KenoButton extends JButton{
      //Instance variables
      private boolean userClicked;           // flag describing whether user clicked or not
      private boolean computerClicked;       // flag describing whether computer selected or not
      private boolean reveal;                // flag describing whether the computer-selected numbers have been revealed or not.
      private int index;                     // The index of the button as it lays in the grid panel.  
         
      /**
      * Constructor
      * @param index    The int that will appear on the button's label. 
      */
      public KenoButton(int index){
         this.index = index;
         setText(String.format("%d", index + 1));
         userClicked = false;
         computerClicked = false;
         reveal = false;
         setBackground(Color.WHITE);
         setOpaque(true);
      }
      
      /**
      * Constructor without label. 
      */
      public KenoButton(){
         userClicked = false;
         computerClicked = false;
         reveal = false;
         setBackground(Color.WHITE);
         setOpaque(true);
      }
      
      /**
      * Get the index of this button as it lays in the grid panel.
      * @return           The int index of the button, index 0. 
      *
      */
      public int getIndex(){
         return index;
      }
      
      /**
      * Get user clicked status.
      * @return boolean flag indiciating click status. True if the user has clicked it.
      */
      public boolean getUserClicked(){
         return userClicked;
      }
      
      /**
      * Get the computer clicked status.
      * @return boolean flag indiciating click status. True if it is one of the computer's selections.
      */
      public boolean getComputerClicked(){
         return computerClicked;
      }
      
      /**
      * Set the gameplay status.
      * @param status      Boolean flag indicating whether the winning numbers have been revealed. True if so.
      */
      public void setReveal(boolean status){
         reveal = status;
      }
      
      /**
      * Get the gameplay status (false = pre-game, true = post-game)
      * @return boolean flag describing whether the answers have been revealed. True if they have been revealed.
      */
      public boolean getReveal(){
         return reveal;
      }
      
      /**
      * Set the userClicked status like a radio button.
      * Simply call the method and it will toggle.
      */
      public void toggleUserClicked(){
         if (userClicked == false){
            userClicked = true;
         }
         else{
            userClicked = false;
         }
      }
      
      /**
      * Set the userClicked status like a radio button.
      * Simply call the method and it will toggle.
      */
      public void toggleComputerClicked(){
         if (computerClicked == false){
            computerClicked = true;
         }
         else{
            computerClicked = false;
         }
      }
      
      /**
      * Reset all clicked status to false and color to white.
      */
      public void reset(){
         computerClicked = false;
         userClicked = false;
         setBackground(Color.WHITE);
         setEnabled(true);
         setReveal(false);
      }
      
      /**
      * Universal automation for setting background color based on click status.
      */
      public void update(){
      
         if (reveal){
            if (userClicked == false && computerClicked == false){
               setBackground(Color.WHITE);
            }
            else if (userClicked == true && computerClicked == false){
               setBackground(Color.BLUE);
            }
            else if (userClicked == false && computerClicked == true){
               setBackground(Color.RED);
            }
            else if (userClicked == true && computerClicked == true){
               setBackground(Color.GREEN);
            }
         }
         else {
            if (userClicked == false){
               setBackground(Color.WHITE);
            }
            else if (userClicked == true){
               setBackground(Color.GRAY);
            }
         }
      }    
   }
   
   
   /**
   * JPanel class to hold the Keno game board.
   * To be placed in the Center of the frame. 
   */
   private class GridPanel extends JPanel{
      
      /**
      * Constructor.  
      * Makes all 40 buttons and places them into the panel.
      */
      public GridPanel() {
         setLayout(new GridLayout(5,8));                     //Set this JPanel layout to 5x8 grid
         buttons = new KenoButton[BUTTONCOUNT];              //Initialize an array of Keno Buttons
         for (int i = 0; i < BUTTONCOUNT; i++){ 
            buttons[i] = new KenoButton(i);                  // Create the new button
            this.add(buttons[i]);                            // Add the new button to this JPanel object
         }
      }
      
      /**
      * Button getter, for action listener
      */
      public KenoButton getGridButton(int index){
         return buttons[index];
      }
      
      /**
      * Getter for all buttons
      */
      public KenoButton[] getAllGridButtons(){
         return buttons;
      }
      
      /**
      * Selection counter. Utility to help stop the user from selecting more than 10 options.
      * @return true if user has reached 10 selections.
      */
      public boolean limitTen(){
         int count = 0;  //Will count the number of selected items
         for (KenoButton b : buttons){
            if (b.getUserClicked()){
               count ++;
               if (count == 10){
                  return true;
               }
            }
         }
         return false;
      }
      
      /**
      * Disables the unselected Keno Board options if limit reached.
      * @param boolean limitTen     true if limit has been reached.
      */
      public void disableNewSelections(boolean limitTen){
         if (limitTen){
            for (KenoButton b : buttons){          // If limit has been reached, disable all unselected buttons
               if (!b.getUserClicked()){
                  b.setEnabled(false);
               }
            }
         }
         else {
            for (KenoButton b : buttons){
               b.setEnabled(true);
            }
         }
      }
   }
   
   
   /**
   * JPanel class: Options Panel.
   * To be placed in the West border of the frame. 
   */
   private class OptionsPanel extends JPanel{
      /**
      * Constructor.
      */
      public OptionsPanel(){
         setLayout(new GridLayout(5, 2));
      
         //// West Panel Game Option 1. Bet (Choice() object)
         betLabel = new JLabel("Your bet ($):");
         bet = new Choice();                            // Betting drop-down menu
         bet.add("10");
         bet.add("20");
         bet.add("50");
         bet.add("100");
      
         //// West Panel Game Option 2. Play (JButton object)
         play = new JButton("Play");                           // Play button
         play.setEnabled(false);                               // Disable by default (until 10 bets chosen)
      
         //// West Panel Game Option 3. End (Jbutton object)
         end = new JButton("End");                             // End button
      
         //// West Panel Game Option 4. Instructions (JButton object)
         instructions = new JButton("Instructions");           //Instructions button
      
         //// West Panel Button Placement
         add(betLabel);
         add(bet);
         add(new JLabel(""));
         add(new JLabel(""));
         add(play);
         add(new JLabel(""));
         add(end);
         add(new JLabel(""));
         add(instructions);
         add(new JLabel(""));
      }
      
      /**
      * Enable or Disable play button based on clickMax
      */
      public void togglePlayEnabled(){
         if (clickMax){
            play.setEnabled(true);
         }
         else {
            play.setEnabled(false);
         }
      }
      
      /**
      * Define Instructions button behavior
      */
      public void showInstructions(){
         String str = "Welcome to Easy Keno :)\n\n" + 
         
            "Win by placing bets on one of 20 winning numbers.\n" +
            "Start by selecting a bet amount. " + 
            "Then choose exactly 10 numbers from the " +
            "Keno board to bet on.\n" +
            "Fianlly, click Play to reveal the answers and see your earnings!\n\n" + 
            "Colors will appear on the tiles as follows: \n\n" + 
            "Grey: You selected the number before submitting.\n" +
            "Red: Revealed computer choice.\n" +
            "Blue: You selected this number, but it didn't win.\n" +
            "Green: You selected a number that the computer did. This is a winning tile!\n\n" +
            "For each tile you guess correctly, you will earn increasing payouts:\n\n" + 
         
            "0 tiles correct = earn back 2x your bet\n" +
            "1-3 tiles correct = earn 1x your bet\n" +
            "4 tile correct = earn back 2x of your bet\n" + 
            "5 tiles correct = earn back 3x your bet\n" +
            "6 tiles correct = earn back 7x your bet\n" +
            "7 tiles correct = earn back 30x your bet\n" +
            "8 tiles correct = earn back 200x your bet\n" +
            "9 tiles correct = earn back 1000x your bet\n" +
            "10 tiles correct = earn back 10000x your bet\n" +
                  
            "Press Exit at any time to end the game and save your results.";
         
         JOptionPane.showMessageDialog(null, str, "Instructions", JOptionPane.INFORMATION_MESSAGE);
      }
      
      /**
      * Define Exit button behavior.
      * Saves a file to the disk in local folder. 
      * @throws IOException   via FileWriter.
      */
      public void endGame() throws IOException{
      
         // Turn off Play button so that it can't be clicked again with 0 selections
         play.setEnabled(false);
         
         // Open pop-up window to explain 
         JOptionPane.showMessageDialog(
            null, 
            "KenoFile.txt saved to the working directory.", 
            "Save Successful", 
            JOptionPane.INFORMATION_MESSAGE);
            
         file = new File("KenoFile.txt");       //Global file gets set
         FileWriter fw = new FileWriter(file);  //Connect to the file with this
         PrintWriter pw = new PrintWriter(fw);  //Edit the file with this
         
         //List all the user's choices
         pw.println("The user selected the following numbers:\n");
         int[] userTiles = new int[10];
         userTiles = game.getUserChoice();
         for (int tile : userTiles){
            String s = String.format("%d", tile);
            pw.println(s);
         }
         
         pw.println("");
         
         //List all the matches and match count
         pw.print("The user matched on the following numbers:\n");
         int count = 0;
         int[] compTiles = new int[20];
         compTiles = game.getCompChoice();
         for (int tile : userTiles){
            for (int cTile : compTiles){
               if (tile == cTile){
                  String st = String.format("%d ", tile);
                  pw.println(st);
                  count++;
               }
            }
         }
         pw.printf("For a total of %d matches.\n\n", count);
         
         //List the total earnings
         pw.printf("The user's total earnings were $%d. WOWZA!\n", game.getRoundEarnings());
         
         
         //Close the print writer
         pw.close();
        
      }
      
   }
   
   /**
   * Action Listener for all buttons
   *
   */
   public class KenoListener implements ActionListener{
      
      public void actionPerformed(ActionEvent e){
         Object source = e.getSource();                                     //Name the source of the interaction
         System.out.printf("Action command: %s\n", e.getActionCommand());   //Show the title of the object clicked                  
         
         //Play button
         if (source == play){
            System.out.println("Play button clicked.");
            
            //Get user bet
            int betInt = Integer.parseInt(bet.getSelectedItem()); //Take bet from Choice -> convert to Integer -> save as int
            
            //Init game model
            game = new KenoModel();                              //Send user's bet and button states to game model
            
            //Run all calcs on model based on UI state at moment of clicking "play"
            game.play(betInt, buttons);
            
            // Update UI: set button colors to reflect model's computer choices
            int[] compChoices = game.getCompChoice();
            if (compChoices != null){
               for (int c : compChoices){
                  for (KenoButton b : buttons){
                     if (b.getIndex() + 1 == c){
                        b.toggleComputerClicked();
                     }  
                  }
               }
            }
            
            // Update UI: Display user and computer post-round colors; disable future clicks
            for (KenoButton b : buttons){
               b.setReveal(true);
               b.update();
               b.setEnabled(false);
            }
            
            //Debug messages in log
            System.out.println("User choices: ");
            int[] uc = game.getUserChoice();
            if (uc != null){
               for (int i = 0; i < uc.length; i++){
                  System.out.printf("%d ", uc[i]);
               }
            }
            System.out.println("");
            
            System.out.println("Computer choices: ");
            int[] cc = game.getCompChoice();
            if (cc != null){
               for (int i = 0; i < 20; i++){
                  System.out.printf("%d ", cc[i]);
               }
            }
            System.out.println("");
            System.out.print("Matches:");
            System.out.println(String.format(" %d ", game.getMatches()));
            
         }
         
         //End button
         else if (source == end){
            System.out.println("End button clicked.");
            
            //Save game data to txt file.
            try{
               optionsPanel.endGame();
            }
            catch (IOException ioe){
               System.out.println("There was an error saving the file."); 
               System.out.println(ioe);
            }
            
            //Reset color and click status.
            for (KenoButton b : buttons){
               b.reset();
            }
            
            //Reset game state selection arrays
            game.resetBoard();
         }
         
         //Instructions button
         else if (source == instructions){
            System.out.println("Instructions button clicked.");
            optionsPanel.showInstructions();
         }
         
         //Any keno board button
         else { 
            //Select the source object as a KenoButton
            KenoButton kbSource = (KenoButton)e.getSource();
            
            //Debug msg
            System.out.printf("KenoButton %s clicked.\n", e.getActionCommand());
            
            //Change selected state of the clicked button
            kbSource.toggleUserClicked();
            
            //Set the new color of the clicked button
            kbSource.update();
            
            //Click limiting behavior
            clickMax = gridPanel.limitTen();          // Check if limit reached
            gridPanel.disableNewSelections(clickMax); // Disable if reached
            optionsPanel.togglePlayEnabled();         // Disable/Enable Play button. If User clicks 10 buttons, play = enabled.
         }
      }
   }
}  
   
      

