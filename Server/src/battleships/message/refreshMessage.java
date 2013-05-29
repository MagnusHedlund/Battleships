package battleships.message;

import battleships.message.Message;

public class RefreshMessage extends Message {

	private static final String myType="ActivePlayersMessage";
	
	public RefreshMessage() {
		super(myType);
	}

}
