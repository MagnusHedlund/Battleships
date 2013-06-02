package battleships.server;
import battleships.game.Coordinate;
import battleships.game.Navy;
import battleships.game.ServerAI;
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
	
	/**
	 * Constant for first players index, used for accessing arrays
	 */
	private final static int PLAYER0 =0;
	
	/**
	 * Constant for second players index, used for accessing arrays
	 */
	private final static int PLAYER1=1; 
	
	/**
	 * Constant for number of Submarines
	 */
	private final static int SUBMARINES=5;
	
	/**
	 * Constant for number of Destroyers
	 */
	private final static int DESTROYERS=3; 
	
	/**
	 * Constant for number of Aircraft_Carriers
	 */
	private final static int AIRCRAFT_CARRIERS=1;
	
	/**
	 * array of Player objects. Contains active players or null
	 * */
	private Player[] player = new Player[2];
	
	/**
	 * boolean for showing if Navy objects are valid
	 */
	private boolean[] navyValid = new boolean[2];
	
	/**
	 * The players Navy objects, the objects to manipulate
	 */
	private Navy[] navy = new Navy[2];
	
	/*
	 * variables to keep track of active player 
	 */
	private int currentPlayer=PLAYER0, otherPlayer=PLAYER1;

	/**
	 * The ServerAI, only used when playing against server
	 */
	private ServerAI serverAI=null;
	
	/**
	 * Constructor for starting session player VS player
	 * 
	 * @param Player ,first player
	 * @param Player ,second player
	 * */
	public Session(Player first, Player second){
		player[PLAYER0]=first;
		player[PLAYER1]=second;
	}
	
	/**
	 * Constructor for starting session against ServerAI
	 * @param Player ,the player
	 * */
	public Session(Player first){
		player[PLAYER0]=first;
		serverAI = new ServerAI(5,3,1);
	}
	
	/**
	 * Runs the game.
	 * */
	@Override
	public void run() {
		System.out.println("Run");
		/* listen and validate Navy objects*/
		while(!navyValid[PLAYER0] || !navyValid[PLAYER1]){
			if(!navyValid[PLAYER0]){
				boolean isValid = readAndValidate(PLAYER0);
				navyValid[PLAYER0]=isValid;
				if(isValid){System.out.println("validated navy 1");}
				player[PLAYER0].sendMessage(new ValidationMessage(isValid));
			}
			
			//only validate if the other player is real
			if(player[PLAYER1]==null){  //no real opponent
				navyValid[PLAYER1]=true; //skip validation of server Navy
				navy[PLAYER1]=serverAI.getNavy();
				System.out.println("server already valid");
			}
			else if(!navyValid[PLAYER1]){
				boolean isValid = readAndValidate(PLAYER1);	
				navyValid[PLAYER1]=isValid;
				if(isValid){System.out.println("validated navy 2");}
				player[PLAYER1].sendMessage(new ValidationMessage(isValid));
			}
		}
		
		System.out.println("left validation loop");
		
		//let first player shoot
		player[currentPlayer].sendMessage(new NavyMessage(navy[currentPlayer], true));
		
		enterGameLoop();
		
	}//run end
	
	/**
	 * Listening for NavyMessage and verifies the Navy object.
	 * @param playerNumber
	 * @return is Navy valid?
	 */
	private boolean readAndValidate(int playerNumber){
		Validator validator = new Validator(SUBMARINES,DESTROYERS,AIRCRAFT_CARRIERS);
		Message msg = readMessage(player[playerNumber]);
		if(msg.getType().equals("NavyMessage")){
			NavyMessage navMsg = (NavyMessage)msg;
			if(validator.validateNavy(navMsg.getNavy())){
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
	
	
	/**
	 * The game loop. Read messages/get shots and evaluate. Messages are sent to clients(not to the ServerAI)
	 * */
	private void enterGameLoop(){
		boolean loop=true;
		Coordinate shotCoordinate=null;
		Ship hitShip=null;
		boolean isHit=false;
		boolean isSunk=false;
		boolean grantTurn=false;
		boolean finished=false;
		
		while(loop){
			
			//reset
			isHit=false;
			isSunk=false;
			grantTurn=false;
			finished=false;
			hitShip=null;
			shotCoordinate=null;
			
			
			
			//Read message
			if(player[currentPlayer]!=null){  //an actual player
				
				Message msg = readMessage(player[currentPlayer]);
				Shot shotMsg=null;
				if(msg.getType().equals("Shot")){
					shotMsg = (Shot)msg;
					shotCoordinate = shotMsg.getCoordinate();
					
				}
				
					
				
			}
			else {  //get shot coordinate from ServerAI
				if(serverAI!=null){
					shotCoordinate = serverAI.shoot();
					System.out.println("server generated shot");
				}	
			}
			
			// do we have a Coordinate?
			if(shotCoordinate!=null){
				
				// shoot the other players navy
				hitShip=navy[otherPlayer].shot(shotCoordinate);
				
				//a hit
				if(hitShip!=null){
					isHit=true;
					if(!hitShip.isSunk()){
						hitShip=null;  //don´t send Ship unless sunk
					}
					else{
						System.out.println("sunk");
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
				
				
				// Tell players if it´s a hit?
				
					//send hitMessage to currentPlayer
					if(player[currentPlayer]!=null){
						player[currentPlayer].sendMessage(new HitMessage(isHit, shotCoordinate, isSunk, hitShip));
					}
					
					//send NavyMessage to otherPlayer
					if(player[otherPlayer]!=null){
						player[otherPlayer].sendMessage(new NavyMessage(navy[otherPlayer], grantTurn));
					}

					if(finished){
						if(player[currentPlayer]!=null){
							player[currentPlayer].sendMessage(new FinishedMessage(true, navy[currentPlayer]));
						}
						
						//send hitMessage to otherPlayer
						if(player[otherPlayer]!=null){
							player[otherPlayer].sendMessage(new FinishedMessage(false, navy[otherPlayer]));
						}
						
						loop=false;
					}
					
				//if otherPlayer was granted next turn, Switch player
				if(grantTurn){
					System.out.println("Switching player");
					switchPlayer();
				}
			}
			else{ //the message received was of wrong type
				if(player[currentPlayer]!=null){
					player[currentPlayer].sendMessage(new ValidationMessage(false));
				}
				
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
	
	
	/**
	 * Reads message. if no message received sleep and check again
	 * @param p, the Player to read from
	 * @return msg, the message
	 */
	private Message readMessage(Player p){
		Message msg=null;
		while(msg==null){
			try{
				msg=p.readMessage();
				if(msg==null){
					Thread.sleep(500);
				}
			}catch(Exception e){
				 e.printStackTrace();
			}
		}
		return msg;	
	}
	
}
