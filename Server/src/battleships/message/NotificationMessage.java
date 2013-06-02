package battleships.message;

/**
 * Message for sending notification messages
 * @author Christopher Nilsson
 *
 */
public class NotificationMessage extends Message {
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="NotificationMessage";
	
	/**
	 * The notification string
	 */
	private String notification;
	
	/**
	 * Constructor for initializing message
	 * @param notification
	 */
	public NotificationMessage(String notification) {
		super(myType);
		this.notification = notification;
	}
	
	/**
	 * Returns notification
	 * @return notification
	 */
	public String getNotification(){return notification;}
}
