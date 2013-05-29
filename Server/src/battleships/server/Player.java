/*
 * Player.java	
 * Version 1.0 (2013-05-29)
 */

package battleships.server;

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
	 * Status.
	 */
	private boolean idle;
	
	/**
	 * Constructor where a connected socket and the player's 
	 * name has to be given.
	 * 
	 * @param name	Name of the player.
	 */
	public Player(Socket socket, Integer id, String name)
	{
		// Attributes
		this.socket = socket;
		this.name = name;
		this.id = id;
		
		// Status
		idle = true;
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
	 * Indicates whether the player isn't in a game.
	 * 
	 * @return	Idle status.
	 */
	public boolean getIdle()
	{
		return idle;
	}
	
	/**
	 * Defines the status of the player (false means that he or she
	 * is currently in a game).
	 * 
	 * @param idle	Status.
	 */
	public void setIdle(boolean idle)
	{
		this.idle = idle;
	}
	
	/**
	 * A player is not valid if there's no connection to him or her
	 * 
	 * @return	Connection state
	 */
	public boolean isValid()
	{
		return socket.isConnected();
	}
	
	/**
	 * Sends a message to the player's client.
	 * 
	 * @param message					Message to deliver.
	 */
	public void sendMessage(Message message)
	{
		socket.write(message);
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
