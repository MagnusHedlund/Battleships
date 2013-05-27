package battleships.message;

public class NameMessage extends Message {

	private static final String myType="NameMessage";
	private String name;
	
	public NameMessage() {
		super(myType);
		name="";
	}
	
	public NameMessage(String name) {
		super(myType);
		this.name=name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){return name;}

}
