package battleships.message;

import battleships.message.Message;

public class refreshMessage extends Message {

	private static final String myType="ActivePlayersMessage";
	
	public refreshMessage() {
		super(myType);
	}

}
