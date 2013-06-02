package battleships.message;

import battleships.game.Navy;

/**
 * Message for telling the game is over, and whether the receiver is the winner.
 * 
 * @author Magnus Hedlund
 * */
public class FinishedMessage extends Message {
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="FinishedMessage";
	
	/**
	 * The winning Navy
	 */
	Navy navy=null;
	
	/**
	 * Shows if the receiver is the winner
	 */
	boolean winner=false;
	
	/**
	 * Constructor
	 */
	public FinishedMessage() {
		super(myType);
	}
	
	/**
	 * Initializing constructor
	 * @param winner
	 * @param navy
	 */
	public FinishedMessage(boolean winner, Navy navy) {
		super(myType);
		this.winner=winner;
		this.navy=navy;
	}
	
	/**
	 * Returns winner
	 * @return winner
	 */
	public boolean getWinner(){
		return winner;
	}
	
	/**
	 * Sets winner
	 * @param winner
	 */
	public void setWinner(boolean winner){
		this.winner=winner;
	}

}
