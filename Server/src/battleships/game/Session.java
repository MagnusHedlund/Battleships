package battleships.game;

import battleships.Navy;
import battleships.message.Message;
import battleships.message.NavyMessage;
import battleships.message.ValidationMessage;

/**
 * A game session object.
 * @author Magnus Hedlund
 * */
public class Session implements Runnable{
	private final static int PLAYER0 =0, PLAYER1=1;
	private Player[] player = new Player[2];
	private boolean[] navyValid = new boolean[2];
	private Navy[] navy = new Navy[2];
	int currentPlayer=PLAYER0, otherPlayer=PLAYER1;
	
	/**
	 * Runs the game.
	 * */
	@Override
	public void run() {
		
		/* listen and validate Navy objects*/
		while(!navyValid[PLAYER0] && !navyValid[PLAYER1]){
			if(!navyValid[PLAYER0]){
				boolean valid = readAndValidate(PLAYER0);
				navyValid[PLAYER0]=valid;
				player[PLAYER0].sendMessage(new ValidationMessage(valid));
			}
			if(!navyValid[PLAYER1]){
				boolean valid = readAndValidate(PLAYER1);	
				navyValid[PLAYER1]=valid;
				player[PLAYER0].sendMessage(new ValidationMessage(valid));
			}
		}
		
		enterGameLoop();
		
	}//run end
	
	/**
	 * Listening for NavyMessage and verifies the Navy object.
	 * 
	 * */
	
	private boolean readAndValidate(int playerNumber){
		Message msg = player[playerNumber].readMessage();
		if(msg.getType()=="NavyMessage"){
			NavyMessage navMsg = (NavyMessage)msg;
			if(navMsg.valid()){  //TODO Correct call 
				navy[playerNumber]=navMsg.getNavy();
				return true;
			}
			else{
				return false;
			}
			
		}
		else{
			return false;
		}	
	}
	
	private void enterGameLoop(){
		boolean loop=true;
		while(loop){
			//TODO game logic
			//Read message
			Message msg = player[currentPlayer].readMessage();
			if(msg.getType()=="Shot"){
				Shot navMsg = (Shot)msg;
			}
			
			//is hit? Win? Sunk?
			boolean isHit=false;
			boolean isSunk=false;
			boolean grantTurn=false;
			Ship hitShip=navy[otherPlayer].shot(msg.getCoordinate());
			if(hitShip!=null){
				isHit=true;
				if(!hitShip.isSunk()){
					hitShip=null;  //don´t send Ship unless sunk
				}
				else{
					isSunk=true;
				}
			}
			else{
				//if miss  let the other one fire
				grantTurn=true;
			}
				
			//send hitMessage to currentPlayer
			player[currentPlayer].sendMessage(new HitMessage(isHit, msg.getCoordinate(), isSunk, hitShip));
			
			//send hitMessage to otherPlayer
			player[otherPlayer].sendMessage(new NavyMessage(navy[otherPlayer], grantTurn));
			
			//if otherPlayer was granted next turn, Switch player
			if(grantTurn){
				switchPlayer();
			}
		}
	}
	
	/**
	 * 
	 * Switch positions of the currentPlayer and otherPlayer
	 * 
	 * */
	private void switchPlayer(){
		
		otherPlayer=currentPlayer;
		
		if(currentPlayer==PLAYER0){
			currentPlayer=PLAYER1;
		}
		else{
			currentPlayer=PLAYER0;
		}
	}
	
}
