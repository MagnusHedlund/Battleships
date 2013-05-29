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
	public boolean GetAccept(){return accepted;}
	public String GetOpponentName(){return opponentName;}
	public int GetOpponentIP(){return opponentID;}
	
	/* Setters */
	public void SetAccept(boolean accept){this.accepted=accept;}
	public void SetOpponentName(String opponentName){this.opponentName=opponentName;}
	public void SetOpponentIP(int opponentIP){this.opponentID=opponentID;}
	
	/**
	 * Sets the message to become a accept challenge message
	 * */
	public void Accept(){
		isAcceptMsg=true;
		accepted=true;
	}
	
	/**
	 * Sets the message to become a decline challenge message
	 * */
	public void Decline(){
		isAcceptMsg=true;
		accepted=false;
	}
}
