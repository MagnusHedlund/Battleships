/*
 * Player.java	
 * Version 0.1 (2013-05-20)
 */

package battleships.server;

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
	 * Constructor where a connected socket and the player's 
	 * name has to be given.
	 * 
	 * @param name	Name of the player.
	 */
	public Player(Socket socket, String name)
	{
		this.socket = socket;
		this.name = name;
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
	 * Sends a message to the player's client.
	 * 
	 * @param message	Message to deliver.
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
