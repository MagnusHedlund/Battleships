/*
 * Player.java	
 * Version 1.0 (2013-05-24)
 */

package battleships.server;

import battleships.message.InvalidMessageException;
import battleships.message.Message;
import battleships.network.Socket;

/**
 * Represents a player. Embeds functionality to send and
 * receive messages to and from the player's client.
 * 
 * @author Christopher Nilsson
 */
public class Player
{	
	/**
	 * Socket for communicating with the player over a network.
	 */
	private Socket socket;
	
	/**
	 * The name of the player.
	 */
	private String name;
	
	/**
	 * Player ID.
	 */
	private Integer id;
	
	/**
	 * Constructor where a connected socket and the player's 
	 * name has to be given.
	 * 
	 * @param name	Name of the player.
	 */
	public Player(Socket socket, Integer id, String name)
	{
		this.socket = socket;
		this.name = name;
		this.id = id;
	}
	
	/**
	 * Retrieves the name of the player.
	 * 
	 * @return	Player name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the unique ID of the player.
	 * 
	 * @return	Player ID.
	 */
	public Integer getID()
	{
		return id;
	}
	
	/**
	 * Sends a message to the player's client.
	 * 
	 * @param message					Message to deliver.
	 * @throws InvalidMessageException 	Messages must be formatted properly.
	 */
	public void sendMessage(Message message) throws InvalidMessageException
	{
		if(message.isValid())
		{
			socket.write(message);
		}
	}
	
	/**
	 * Reads a message that has been sent from the player's client.
	 * 
	 * @return	A message received from the corresponding client. If there are
	 * 			no unread messages, <i>null</i> is returned.
	 */
	public Message readMessage()
	{
		return socket.read();
	}
}
