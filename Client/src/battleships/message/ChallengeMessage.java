package battleships.message;

public class ChallengeMessage extends Message {
	private static final String myType="ChallengeMessage";
	private boolean isAcceptMsg;  //is this an acceptMessage?
	private boolean accepted;  //was challenge accepted?
	private String opponentName;  
	private int opponentID; 
	
	public ChallengeMessage(){
		super(myType);
		isAcceptMsg=false;
		accepted=false;
		opponentName="";
		opponentID=-1;
	}
	
	public ChallengeMessage(String opponentName, int opponentID){
		super(myType);
		isAcceptMsg=false;
		accepted=false;
		this.opponentName=opponentName;
		this.opponentID=opponentID;
	}
	
	public ChallengeMessage(String opponentName, int opponentID, boolean isAcceptMsg, boolean accept){
		super(myType);
		this.isAcceptMsg=isAcceptMsg;
		this.accepted=accept;
		this.opponentName=opponentName;
		this.opponentID=opponentID;
	}
	
	/* Getters */
	public boolean getAccept(){return accepted;}
	public boolean isAcceptMessage(){return isAcceptMsg;}
	public String getOpponentName(){return opponentName;}
	public int getOpponentID(){return opponentID;}
	
	/* Setters */
	public void setAccept(boolean accept){this.accepted=accept;}
	public void setIsAcceptMessage(boolean isAcceptMsg){this.isAcceptMsg=isAcceptMsg;}
	public void setOpponentName(String opponentName){this.opponentName=opponentName;}
	public void setOpponentID(int opponentID){this.opponentID=opponentID;}
	
	/**
	 * Sets the message to become a accept challenge message
	 * */
	public void accept(){
		isAcceptMsg=true;
		accepted=true;
	}
	
	/**
	 * Sets the message to become a decline challenge message
	 * */
	public void decline(){
		isAcceptMsg=true;
		accepted=false;
	}
}
