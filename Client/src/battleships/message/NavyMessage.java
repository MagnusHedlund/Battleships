package battleships.message;

import battleships.game.Navy;

/**
 * Message containing a full Navy object, used to update Client and grant turn
 * 
 * @author Magnus Hedlund
 * 
 * */

public class NavyMessage extends Message{
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="NavyMessage";
	
	/**
	 * The navy object
	 */
	private Navy navy;
	
	/**
	 * Shows whether a turn is granted
	 */
	private boolean grantTurn;
	
	/**
	 * Constructor
	 */
	public NavyMessage(){
		super(myType);
		navy=null;
		grantTurn=false;
	}
	
	/**
	 * Initializing constructor
	 * @param n, the navy
	 */
	public NavyMessage(Navy n){
		super(myType);
		navy=n;
		grantTurn=false;
	}
	
	/**
	 * Initializing constructor
	 * @param n
	 * @param turn
	 */
	public NavyMessage(Navy n, boolean turn){
		super(myType);
		navy=n;
		grantTurn=turn;		
	}
	
	/**
	 * Sets navy
	 * @param n
	 */
	public void setNavy(Navy n){
		navy=n;
	}
	
	/**
	 * Returns navy
	 * @return navy
	 */
	public Navy getNavy(){
		return navy;
	}
	
	/**
	 * Sets grantTurn
	 * @param grant
	 */
	public void setGrantTurn(boolean grant){
		grantTurn=grant;
	}
	
	/**
	 * Returns grantTurn
	 * @return grantTurn
	 */
	public boolean getGrantTurn(){
		return grantTurn;
	}
}
