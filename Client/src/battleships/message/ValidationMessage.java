package battleships.message;

/**
 * Message to report that a navy was validated on server
 * @author Mono
 *
 */
public class ValidationMessage extends Message {
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="ValidationMessage";
	
	/**
	 * Tells if Navy is valid
	 */
	private boolean valid;
	
	/**
	 * Constructor
	 */
	public ValidationMessage(){
		super(myType);
		valid=false;
	}
	
	/**
	 * Initializing constructor
	 * @param val
	 */
	public ValidationMessage(boolean val){
		super(myType);
		valid=val;
	}
	
	/**
	 * Returns valid
	 * @return valid
	 */
	public boolean getMessage(){return valid;}
	
	/**
	 * Sets valid
	 * @param val
	 */
	public void setMessage(boolean val){valid=val;}
}
