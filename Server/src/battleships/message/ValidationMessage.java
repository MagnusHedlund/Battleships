package battleships.message;

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
	
	public boolean getMessage(){return valid;}
	
	public void setMessage(boolean val){valid=val;}
}
