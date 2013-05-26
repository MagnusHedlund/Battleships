package battleships.message;

public class ValidationMessage {
	private boolean valid;
	
	public ValidationMessage(){
		valid=false;
	}
	
	public ValidationMessage(boolean val){valid=val;}
	
	private boolean getMessage(){return valid;}
	
	private void setMessage(boolean val){valid=val;}
}
