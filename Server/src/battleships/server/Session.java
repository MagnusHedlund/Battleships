package battleships.server;

import battleships.game.Navy;
import battleships.game.Ship;
import battleships.game.Validator;
import battleships.message.FinishedMessage;
import battleships.message.HitMessage;
import battleships.message.Message;
import battleships.message.NavyMessage;
import battleships.message.Shot;
import battleships.message.ValidationMessage;

/**
 * A game session object.
 * @author Magnus Hedlund
 * */
public class Session implements Runnable{
	private final static int PLAYER0 =0, PLAYER1=1, SUBMARINES=5, DESTROYERS=3, AIRCRAFT_CARRIERS=1;
	private Player[] player = new Player[2];
	private boolean[] navyValid = new boolean[2];
	private Navy[] navy = new Navy[2];
	private int currentPlayer=PLAYER0, otherPlayer=PLAYER1;
	private boolean isHit=false;
	private boolean isSunk=false;
	private boolean grantTurn=false;
	private boolean finished=false;
	
	public Session(Player first, Player second){
		player[PLAYER0]=first;
		player[PLAYER1]=second;
	}
	
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
		Validator validator = new Validator(SUBMARINES,DESTROYERS,AIRCRAFT_CARRIERS);
		Message msg = player[playerNumber].readMessage();
		if(msg.getType()=="NavyMessage"){
			NavyMessage navMsg = (NavyMessage)msg;
			if(validator.validateNavy(navMsg.getNavy())){  //TODO Correct call 
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
			
			//reset
			isHit=false;
			isSunk=false;
			grantTurn=false;
			finished=false;
			
			//Read message
			Message msg = player[currentPlayer].readMessage();
			Shot navMsg=null;
			if(msg.getType()=="Shot"){
				navMsg = (Shot)msg;
			}
			
			// do we have a valid message?
			if(navMsg!=null){
				
				Ship hitShip=navy[otherPlayer].shot(navMsg.getCoordinate());
				
				//a hit
				if(hitShip!=null){
					isHit=true;
					if(!hitShip.isSunk()){
						hitShip=null;  //don´t send Ship unless sunk
					}
					else{
						isSunk=true;
						// check if won
						if(navy[otherPlayer].allGone()){
							finished=true;
						}
					}
				}
				// no hit
				else{
					//if miss  let the other one fire
					grantTurn=true;
				}
				
				if(finished){
					
					player[currentPlayer].sendMessage(new FinishedMessage(true, navy[currentPlayer]));
					
					//send hitMessage to otherPlayer
					player[otherPlayer].sendMessage(new FinishedMessage(false, navy[otherPlayer]));
					
					loop=false;
				}
				else{
					//send hitMessage to currentPlayer
					player[currentPlayer].sendMessage(new HitMessage(isHit, navMsg.getCoordinate(), isSunk, hitShip));
				
					//send hitMessage to otherPlayer
					player[otherPlayer].sendMessage(new NavyMessage(navy[otherPlayer], grantTurn));
				}

				//if otherPlayer was granted next turn, Switch player
				if(grantTurn){
					switchPlayer();
				}
			}
			else{ //the message received was of wrong type
				player[currentPlayer].sendMessage(new ValidationMessage(false));
			}
		}//while end
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
