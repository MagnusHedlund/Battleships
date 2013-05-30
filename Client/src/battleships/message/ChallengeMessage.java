package battleships.message;

public class ChallengeMessage extends Message {
	private static final String myType="ChallengeMessage";
	private boolean isAcceptMsg;  //is this an acceptMessage?
	private boolean accepted;  //was challenge accepted?
	private String opponentName;  
	private String opponentIP;  //could maybe be omitted?
	
	public ChallengeMessage(){
		super(myType);
		isAcceptMsg=false;
		accepted=false;
		opponentName="";
		opponentIP="";
	}
	
	public ChallengeMessage(String opponentName, String opponentIP){
		super(myType);
		isAcceptMsg=false;
		accepted=false;
		this.opponentName=opponentName;
		this.opponentIP=opponentIP;
	}
	
	public ChallengeMessage(String opponentName, String opponentIP, boolean isAcceptMsg, boolean accept){
		super(myType);
		this.isAcceptMsg=isAcceptMsg;
		this.accepted=accept;
		this.opponentName=opponentName;
		this.opponentIP=opponentIP;
	}
	
	/* Getters */
	public boolean GetAccept(){return accepted;}
	public String GetOpponentName(){return opponentName;}
	public String GetOpponentIP(){return opponentIP;}
	
	/* Setters */
	public void SetAccept(boolean accept){this.accepted=accept;}
	public void SetOpponentName(String opponentName){this.opponentName=opponentName;}
	public void SetOpponentIP(String opponentIP){this.opponentIP=opponentIP;}
	
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
