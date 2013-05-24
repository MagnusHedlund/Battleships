package battleships.message;

import battleships.game.Coordinate;
import battleships.game.Rotation;

public class Message
{
	/**
	 * 
	 */
	private MessageType type;
	
	/**
	 * 
	 */
	private String stringData;
	
	/**
	 * 
	 */
	private Integer integerData;
	
	/**
	 * 
	 */
	private Coordinate coordinateData;
	
	/**
	 * 
	 */
	private Rotation rotationData;
	
	/**
	 * Creating a new message with no data set to it
	 * 
	 * @param type	Type of message
	 */
	public Message(MessageType type)
	{
		// Type of message
		this.type = type;
		
		// Empty data is represented by null
		stringData = null;
		integerData = null;
		coordinateData = null;
		rotationData = null;
	}
	
	/**
	 * Retrieves the type of the message
	 * 
	 * @return	Message type
	 */
	public MessageType getType()
	{
		return type;
	}
	
	/**
	 * Reading text data bound to the message.
	 * 
	 * @return
	 */
	public String getText()
	{
		return stringData;
	}
	
	/**
	 * Reading numeric data bound to the message.
	 * 
	 * @return
	 */
	public Integer getInteger()
	{
		return integerData;
	}
	
	/**
	 * Reading coordinate data bound to the message.
	 * 
	 * @return
	 */
	public Coordinate getCoordinate()
	{
		return coordinateData;
	}
	
	/**
	 * Reading rotation data bound to the message.
	 * 
	 * @return
	 */
	public Rotation getRotation()
	{
		return rotationData;
	}
	
	/**
	 * Defining text data that will be sent along with tme message.
	 * 
	 * @param data
	 */
	public void setText(String data)
	{
		stringData = data;
	}
	
	/**
	 * Defining numeric data that will be sent along with tme message.
	 * 
	 * @param data
	 */
	public void setInteger(Integer data)
	{
		integerData = data;
	}
	
	/**
	 * Defining coordinate data that will be sent along with tme message.
	 * 
	 * @param data
	 */
	public void setCoordinate(Coordinate data)
	{
		coordinateData = data;
	}
	
	/**
	 * Defining rotation data that will be sent along with tme message.
	 * 
	 * @param data
	 */
	public void setRotation(Rotation data)
	{
		rotationData = data;
	}
	
	/**
	 * Some messages must contain specific data
	 * 
	 * @return	True if the required data has been set
	 */
	public boolean isValid()
	{
		// Check message type
		boolean valid;
		switch(getType())
		{
			// Text
			case CONNECT: 
				valid = getText() != null; 
				break;
			
			// Integer
			case CHALLENGE: case ACCEPT: case DENY: case TURN:
				valid = getInteger() != null; 
				break;
			
			// Text and Integer
			case UPDATE:
				valid = getText() != null && getInteger() != null; 
				break;
				
			// Coordinate
			case FIRE: case HIT: case MISS:
				valid = getCoordinate() != null;
				break;
				
			// Integer, Coordinate and Rotation 
			case PLACE: 
				valid = getInteger() != null && getCoordinate() != null && getRotation() != null;
				break;
			
			// All other message types require no data
			default:
				valid = true;
				break;
		}
		
		// Describe the validity of the message
		return valid;
	}
}
