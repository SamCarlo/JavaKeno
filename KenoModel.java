/**
* Model for EasyKeno; holds game state.
* @author Samuel Carlos
* Date: 10/9/24
*/

import java.util.Arrays;
import javax.swing.JOptionPane;

public class KenoModel{
   private int betAmount;                  //The amount the user bet on the most recent round
   private int[] userChoice = new int[10]; //The array of numbers representing the user's tile choices (connected to display name, not index)
   private int[] compChoice = new int[10]; //The array of numbers representing the computer's tile choices (by name, not index)
   private int totalEarnings;          //The total amount earned in this cycle of gameplay.
   private int roundEarnings;          //The total amount earned in this present round of Keno.
   private int rounds;                 //The total number of rounds played in this cycle of gameplay.
   private int matches;                //The number of matching tiles between user and computer choices.
   
   /**
   * Constructor for KenoModel.
   */
   public KenoModel(){
   
   }
   
   /**
   * Trigger all events necessary to complete 1 round of play. 
   * @param betAmount     int: The amount the player bet for this round.
   * @param kb            KenoButton[]: The UI's array of buttons and their present states. 
   */
   public void play(int betAmount, KenoApp.KenoButton[] kb){
   
      //Set the bet amount based on param betAmount
      setBetAmount(betAmount);
      
      //Set matches to 0
      setMatches(0);
      
      //Set total and round earnings to 0
      setAllEarnings(0);
     
      // Try to import all selected buttons. If not 10 in number, throw exception and reset the round.
      try {
         //Update userChoice array.
         updateUserChoice(kb);
         
         //Computer chooses 20 random numbers
         setCompChoice(20);
         
         //Calculate total number of matches
         calcMatches();
         
         //Debug msg for log: check number of matches
         System.out.printf("IN GAME.PLAY() --> Matches: %d\n", getMatches());
         
         //Calculate this round's earnings
         calcRoundEarnings();  
         
         //Debug msg for log: check earnings
         System.out.printf("IN GAME.PLAY() --> Round Earnings: %d\n", getRoundEarnings());
   
      } 
      catch (KenoException ke) {
         // Show error window with message. Reset game data for this round.
         JOptionPane.showMessageDialog(null, ke.toString(), "Error", JOptionPane.INFORMATION_MESSAGE);
         roundReset();
      }

      
   }
   
   /**
   * Reset round data (user choices and computer choices)
   * Keeps UI the same.
   */
   public void roundReset(){
      this.userChoice = null;
      this.compChoice = null;
   }
   
   /**
   * Setter for bet amount.
   * @param amount     int: the amount the user wants to bet.
   */
   public void setBetAmount(int amount){
      this.betAmount = amount;
   }
   
   /**
   * Mutator for userChoice[]. 
   * @param kbArray      KenoButton[]: All of the buttons in the UI and their internal states.
   * @throws KenoException if user entered more or less than 10 tile choices. 
   */
   public void updateUserChoice (KenoApp.KenoButton[] kbArray) throws KenoException{
  
      // Create a len = 1 array to start holding values
      int[] tempArray = new int[1];
      Arrays.fill(tempArray, 0);
      
      //Fill temp array with values marked as "clicked"
      for (KenoApp.KenoButton b : kbArray){
         if (b.getUserClicked() == true){
            tempArray = Arrays.copyOf(tempArray, tempArray.length + 1);
            tempArray[tempArray.length - 1] = b.getIndex() + 1;
         }
      }
      
      //Copy tempArray minus the placehold "0" to this.userChoice array
      this.userChoice = Arrays.copyOfRange(tempArray, 1, tempArray.length);
      
      //Validate that the user chose fewer than 11 and greater than 0 values
      if (userChoice.length != 10){
         System.out.printf("Number of numbers selected: %d", userChoice.length);
         KenoException ke = new KenoException();
         String keMsg = String.format("Please select 10 numbers. You selected %d.\n", this.userChoice.length);
         ke.setMessage(keMsg);
         throw ke;
      }
      
      
   }
   
   /**
   * Setter (randomized) for compChoice[].
   * @param count   int: The amount of numbers to generate. 
   */
   public void setCompChoice(int count){
      
      int[] tempArray = new int[1];
      int ranNum = 0;
      
      
      // Generate and append count # of random numbers to tempArray
      for (int i = 0; i < count; i++){
         boolean novelNumber = false;                 //set false to enter loop
         while(!novelNumber){
            novelNumber = true;                       //set true to prepate to capture a single repeat in the sequence
            ranNum = (int)(Math.random() * 40) + 1;
            
            for (int n : tempArray){
               if (ranNum == n){
                  novelNumber = false;                //set false to show that a match has been found and the loop needs repeating.
               }
            }
         } //If out of loop, then ranNum is novel. 
         
         tempArray = Arrays.copyOf(tempArray, tempArray.length + 1);
         tempArray[tempArray.length - 1] = ranNum;
      }
      
      this.compChoice = Arrays.copyOfRange(tempArray, 1, tempArray.length);
      System.out.printf("compChoice leng = %d\n", this.compChoice.length);
   }
   
   /**
   * Plain setter for matches. For initializing to 0.
   * @param n     int: the number of matching tiles.
   */
   public void setMatches(int n){
      this.matches = n;
   }
   
   /**
   * Calculator for counting matches. Sets by counting number of tile matches. 
   */
   public void calcMatches(){
      //Compare each value in userChoice[] to each value in compChoice[]
      //Increment matches each time a match is found.
      for (int i = 0; i < userChoice.length; i++){
         for (int j = 0; j < compChoice.length; j++){
            if (userChoice[i] == compChoice[j]){
               this.matches++; 
            }
         }
      }
   }
   
   /**
   * Getter for matches.
   * @return matches    int: the number of matching tiles between human and computer.
   */
   public int getMatches(){
      return this.matches;
   }
   
   /**
   * Plain setter for totalEarnings =0 and roundEarnings = 0.
   * Used only in constructor; combined is more efficient.
   * @param n     int: the number to set round and total earnings to. Default 0 in constructor.
   */
   public void setAllEarnings(int n){
      this.roundEarnings = n;
      this.totalEarnings = n;
   }
   
   /**
   * Calculate roundEarnings after a round is played.
   */
   public void calcRoundEarnings(){
      
      // Pay Table
      // Because assigned pay table doesn't follow an obvious
      // function, I will define each case defined in the assignment.
      int prizeInt = 0;    //The amount won
      
      switch(matches){
         case 0:
            prizeInt = this.betAmount * 2;
            break;
         case 1: 
            prizeInt = this.betAmount * 1;
            break;
         case 2: 
            prizeInt = this.betAmount * 1;
            break;
         case 3: 
            prizeInt = this.betAmount * 1;
            break;
         case 4: 
            prizeInt = this.betAmount * 2;
            break;
         case 5: 
            prizeInt = this.betAmount * 3;
            break;
         case 6: 
            prizeInt = this.betAmount * 7;
            break;
         case 7: 
            prizeInt = this.betAmount * 30;
            break;
         case 8: 
            prizeInt = this.betAmount * 200;
            break;
         case 9: 
            prizeInt = this.betAmount * 1000;
            break;
         case 10: 
            prizeInt = this.betAmount * 10000;
            break;
      }
      
      //Update this.roundEarnings
      this.roundEarnings = prizeInt;
   }
   
   /**
   * Calculate totalEarnings after gameplay.
   */
   public void calcTotalEarnings(){
      this.totalEarnings = this.roundEarnings;
   }
   
   
   /**
   * Getter for userChoice[].
   * @return userChoice      int[]: the array of tiles that the user clicked (by title, not index).
   */
   public int[] getUserChoice(){
      return this.userChoice;
   }
   
   /**
   * Getter for compChoice[].
   * @return int[] compChoice.
   */ 
   public int[] getCompChoice(){
      return this.compChoice;
   }

   /**
   * Getter for roundEarnings.
   * @return int roundEarnings.
   */
   public int getRoundEarnings(){
      return this.roundEarnings;
   }

   
   /**
   * toString method for total earnings.
   * @return   a String describing the total earnings.
   */
   public String totalEarningsToString(){
      return String.format("Total earnings = $%d", this.totalEarnings);
   }
   
   /**
   * Reset the userChoice and compChoice arrays.
   */
   public void resetBoard(){
      this.userChoice = null;
      this.compChoice = null;
   }
}
