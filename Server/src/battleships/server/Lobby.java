/*
 * Lobby.java	
 * Version 1.0 (2013-05-29)
 */

package battleships.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import battleships.game.Session;
import battleships.message.ActivePlayersMessage;
import battleships.message.ChallengeMessage;
import battleships.message.Message;

/**
 * Listens for messages and acts upon them when it's related to
 * the lobby. One responsibility is to generate a new thread when 
 * a game between two clients is desired.
 * 
 * @author Christopher Nilsson
 */
public class Lobby
{
	/**
	 * List of players that are in the lobby.
	 */
	Map<Integer, Player> players;

	/**
	 * Constructor.
	 */
	public Lobby()
	{
		players = new HashMap<Integer, Player>();
	}
	
	/**
	 * Performs updates related to players and sessions depending on
	 * messages received from the clients.
	 */
	public void update()
	{
		// Read messages coming from clients
		for(Entry<Integer, Player> entry : players.entrySet())
		{
			// Make sure the player is valid
			Player player = entry.getValue();
			if(!player.isValid())
			{
				players.remove(entry.getKey());
			}
			
			// Prepare message retrieval if the player isn't in a game
			else if(player.getIdle())
			{
				// Read a message
				Message message = player.readMessage();
				
				// Make sure anything was read
				if(message != null)
				{
					// Get the message type
					String type = message.getType();
					
					// Interpret the message and act accordingly
					switch(type)
					{
						case "ActivePlayersMessage": sendPlayerList(player); break;	
						case "ChallengeMessage": handleChallenge(player, (ChallengeMessage)message); break;
					}
				}
			}
			
		}
	}
	
	/**
	 * Takes a new player and adds him or her to the lobby.
	 * 
	 * @param newPlayer	A player that just connected to the server.
	 */
	public void addPlayer(Player newPlayer)
	{
		players.put(newPlayer.getID(), newPlayer);
	}
	
	/**
	 * 
	 * 
	 * @param player
	 */
	private void sendPlayerList(Player player)
	{
		// Create a message to deliver
		ActivePlayersMessage message = new ActivePlayersMessage();
		
		// List with idle player information
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for(Player value : players.values())
		{
			if(value.getIdle())
			{
				list.put(value.getName(), value.getID());
			}
		}
		
		// Apply the list to the message and send it
		message.setContenders(list);
		player.sendMessage(message);
	}
	
	/**
	 * 
	 * 
	 * @param player
	 * @param message
	 */
	private void handleChallenge(Player player, ChallengeMessage message)
	{
		// REMARKS:
		// How do I know whether the message is a Request or a Response? Det finns flagga för detta i meddelandet .//M
		// The IP (String) attribute should be changed to ID (Integer)? Fixar det.//M
		Player other = null;

		
		createGame(player, other);
	}
	
	/**
	 * 
	 * 
	 * @param first
	 * @param second
	 */
	private void createGame(Player first, Player second)
	{
		// REMARKS:
		// You probably need the Player instances. Define a constructor? Jag fixar.//M
		(new Thread(new Session())).start();
	}
}
