/**
* Exception class for the Keno game
* @author Samuel Carlos
*/
public class KenoException extends Exception{
   private String message;       // Error message to send the user
   
   /**
   * Constructor.
   */
   public KenoException(){
   
   }
   
   /**
   * Message setter.
   8 @param msg      String describing the issue.
   */
   public void setMessage(String msg){
      this.message = msg;
   }
   
   /**
   * Message toString(). 
   */
   public String toString(){
      return this.message;
   }
   
   
}
