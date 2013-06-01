package battleships.message;

public class NotificationMessage extends Message {

	private static final String myType="NotificationMessage";
	private String notification;
	
	public NotificationMessage(String notification) {
		super(myType);
		this.notification = notification;
	}
	
	public String getNotification(){return notification;}
}
