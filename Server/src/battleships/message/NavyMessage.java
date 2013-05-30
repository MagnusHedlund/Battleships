package battleships.message;

import battleships.game.Navy;

/**
 * Message containing a full Navy object, used to update Client and grant turn
 * 
 * @author Magnus Hedlund
 * 
 * */

public class NavyMessage extends Message{
	private static final String myType="NavyMessage";
	private Navy navy;
	private boolean grantTurn;
	
	public NavyMessage(){
		super(myType);
		navy=null;
		grantTurn=false;
	}
	
	public NavyMessage(Navy n){
		super(myType);
		navy=n;
		grantTurn=false;
	}
	
	public NavyMessage(Navy n, boolean turn){
		super(myType);
		navy=n;
		grantTurn=turn;		
	}
	
	public void setNavy(Navy n){
		navy=n;
	}
	
	public Navy getNavy(){
		return navy;
	}
	
	public void setGrantTurn(boolean grant){
		grantTurn=grant;
	}
	
	public boolean getGrantTurn(){
		return grantTurn;
	}
}
