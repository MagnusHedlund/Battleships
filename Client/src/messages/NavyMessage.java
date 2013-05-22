package battleships.message;

import battleships.Navy;


public class NavyMessage {
	private Navy navy;
	private boolean grantTurn;
	
	public NavyMessage(){
		navy=null;
		grantTurn=false;
	}
	
	public NavyMessage(Navy n){
		navy=n;
		grantTurn=false;
	}
	
	public NavyMessage(Navy n, boolean turn){
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
