package message;

public class ValidationMessage extends Message {
	private static final String myType="ValidationMessage";
	private boolean valid;
	
	public ValidationMessage(){
		super(myType);
		valid=false;
	}
	
	public ValidationMessage(boolean val){
		super(myType);
		valid=val;
	}
	
	private boolean getMessage(){return valid;}
	
	private void setMessage(boolean val){valid=val;}
}
