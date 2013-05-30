package message;

import game.Navy;

/**
 * Message for telling the game is over, and whether the receiver is the winner.
 * 
 * @author Magnus Hedlund
 * */
public class FinishedMessage extends Message {

	private static final String myType="FinishedMessage";
	Navy navy=null;
	boolean winner=false;
	
	public FinishedMessage() {
		super(myType);
	}
	
	public FinishedMessage(boolean winner, Navy navy) {
		super(myType);
		this.winner=winner;
		this.navy=navy;
	}
	
	public boolean getWinner(){
		return winner;
	}
	
	public void setWinner(boolean winner){
		this.winner=winner;
	}

}
