package battleships.message;

public class ChallengeMessage extends Message {
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="ChallengeMessage";
	
	/**
	 * Tells if this is an accept or challenge
	 */
	private boolean isAcceptMsg;
	
	/**
	 * Tells if challenge accepted
	 */
	private boolean accepted;
	
	/**
	 * Name of the challenged opponent
	 */
	private String opponentName;
	
	/**
	 * ID of the challenged opponent
	 */
	private int opponentID; 
	
	/**
	 * Constructor
	 */
	public ChallengeMessage(){
		super(myType);
		this.isAcceptMsg=false;
		this.accepted=false;
		this.opponentName="";
		this.opponentID=-1;
	}
	
	/**
	 * Initializing constructor
	 * @param opponentName
	 * @param opponentID
	 */
	public ChallengeMessage(String opponentName, int opponentID){
		super(myType);
		this.isAcceptMsg=false;
		this.accepted=false;
		this.opponentName=opponentName;
		this.opponentID=opponentID;
	}
	
	/**
	 * Initializing constructor
	 * @param opponentName
	 * @param opponentID
	 * @param isAcceptMsg
	 * @param accept
	 */
	public ChallengeMessage(String opponentName, int opponentID, boolean isAcceptMsg, boolean accept){
		super(myType);
		this.isAcceptMsg=isAcceptMsg;
		this.accepted=accept;
		this.opponentName=opponentName;
		this.opponentID=opponentID;
	}
	
	/**
	 * Returns accepted
	 * @return accepted
	 */
	public boolean getAccept(){return accepted;}
	
	/**
	 * Returns isAcceptMsg
	 * @return isAcceptMsg
	 */
	public boolean isAcceptMessage(){return isAcceptMsg;}
	
	/**
	 * Returns opponentName
	 * @return opponentName
	 */
	public String getOpponentName(){return opponentName;}
	
	/**
	 * Returns opponentId
	 * @return opponentId
	 */
	public int getOpponentID(){return opponentID;}
	
	/**
	 * Sets accepted
	 * @param accept
	 */
	public void setAccept(boolean accept){this.accepted=accept;}
	
	/**
	 * Sets isAcceptMsg
	 * @param isAcceptMsg
	 */
	public void setIsAcceptMessage(boolean isAcceptMsg){this.isAcceptMsg=isAcceptMsg;}
	
	/**
	 * Sets opponentName
	 * @param opponentName
	 */
	public void setOpponentName(String opponentName){this.opponentName=opponentName;}
	
	/**
	 * Sets opponentID
	 * @param opponentID
	 */
	public void setOpponentID(int opponentID){this.opponentID=opponentID;}
	
	/**
	 * Sets the message to become a accept challenge message
	 * */
	public void accept(){
		this.isAcceptMsg=true;
		this.accepted=true;
	}
	
	/**
	 * Sets the message to become a decline challenge message
	 * */
	public void decline(){
		this.isAcceptMsg=true;
		this.accepted=false;
	}
}
