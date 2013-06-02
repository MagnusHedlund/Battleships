package battleships.message;

/**
 * A message for sending a name
 * @author Magnus Hedlund
 *
 */
public class NameMessage extends Message {
	/**
	 * String telling type of message, Accessed through inherited method
	 */
	private static final String myType="NameMessage";
	
	/**
	 * The name String
	 */
	private String name;
	
	/**
	 * Constructor
	 */
	public NameMessage() {
		super(myType);
		name="";
	}
	
	/**
	 * Initializing constructor
	 * @param name
	 */
	public NameMessage(String name) {
		super(myType);
		this.name=name;
	}
	
	/**
	 * Sets name
	 * @param name
	 */
	public void setName(String name){
		this.name=name;
	}
	
	/**
	 * Returns name
	 * @return name
	 */
	public String getName(){return name;}

}
