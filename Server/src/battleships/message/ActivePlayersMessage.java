package battleships.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Message for sending list of names and id´s of players.
 * @author Magnus Hedlund
 *
 */
public class ActivePlayersMessage extends Message {
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="ActivePlayersMessage";
	
	/**
	 * Map of player name as key and Integer as value
	 */
	private Map<String, Integer> contenders = new HashMap<String,Integer>();
	
	/**
	 * Constructor
	 */
	public ActivePlayersMessage() {
		super(myType);
	}
	
	/**
	 * Constructor for initializing message
	 * @param contenders
	 */
	public ActivePlayersMessage(HashMap<String,Integer> contenders) {
		super(myType);
		this.contenders=contenders;
	}
	
	/**
	 * Returns contenders
	 * @return contenders
	 */
	public Map<String, Integer> getContenders(){
		return contenders;
	}

	/**
	 * Sets contenders
	 * @param contenders, Map of contenders
	 */
	public void setContenders(HashMap<String,Integer> contenders){
		this.contenders=contenders;
	}
}
