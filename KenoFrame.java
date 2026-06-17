/**
* A simplified game of Keno.
* @author Samuel Carlos
* date: 10/7/25
*/

import javax.swing.*;
import java.awt.*;

/**
* Class that runs the Keno game
*/
public class KenoFrame{
   /**
   * Driver for Keno GUI.
   * @param args not used.
   */
   public static void main(String[] args){
      KenoApp k = new KenoApp();
      k.initializeGUI();
      k.setVisible(true);
   }
 }