package battleships.message;

/**
 * Message for requesting update from server
 * @author Magnus Hedlund
 *
 */
public class RefreshMessage extends Message {
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="RefreshMessage";
	
	/**
	 * Constructor
	 */
	public RefreshMessage() {
		super(myType);
	}

}
