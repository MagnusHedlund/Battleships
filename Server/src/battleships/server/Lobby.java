/*
 * Lobby.java	
 * Version 1.1 (2013-06-01)
 */

package battleships.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import battleships.message.ActivePlayersMessage;
import battleships.message.ChallengeMessage;
import battleships.message.Message;
import battleships.message.NotificationMessage;

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
				System.out.println(player.getName() + " [" + player.getID() + "] left.");
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
						// Player list request
						case "RefreshMessage": 
							sendPlayerList(player);
							break;
							
						// Messages related to challenging others
						case "ChallengeMessage": 
							handleChallenge(player, (ChallengeMessage)message);
							break;
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
		System.out.println(newPlayer.getName() + " [" + newPlayer.getID() + "] joined.");
	}
	
	/**
	 * Sends a list of idle players to a client.
	 * 
	 * @param player	Player that requested the list.
	 */
	private void sendPlayerList(Player player)
	{
		// Create a message to deliver
		ActivePlayersMessage message = new ActivePlayersMessage();
		
		// List with player information
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for(Player value : players.values())
		{
			// Only idle players (and ignore the requesting player)
			if(value.getIdle() && player.getID() != value.getID())
			{
				list.put(value.getName(), value.getID());
			}
		}
		
		// Apply the list to the message and send it
		message.setContenders(list);
		player.sendMessage(message);
	}
	
	/**
	 * Interpret and respond to a challenge request or answer.
	 * 
	 * @param player	Player client that sent the message.
	 * @param message	The message itself.
	 */
	private void handleChallenge(Player player, ChallengeMessage message)
	{
		// The player may challenge the Server AI
		if(message.getOpponentID() == 0)
		{
			// Accept the request right away
			player.sendMessage(new ChallengeMessage("Server AI", 0, true, true));
			createGame(player);
			return;
		}
		
		// Find the other player, since it turned out not to be the Server AI
		Player other = players.get(message.getOpponentID());
		
		// Must be a valid one
		if(other == null)
		{
			NotificationMessage error = new NotificationMessage("The player you wish to challenge has disconnected.");
			player.sendMessage(error);
		}
		
		// The player can't already be in a game
		else if(!other.getIdle())
		{
			NotificationMessage error = new NotificationMessage("'" + other.getName() + "' is already in a game.");
			player.sendMessage(error);
		}
		
		// Challenging oneself makes no sense
		else if(other.getID() == player.getID())
		{
			NotificationMessage error = new NotificationMessage("You can't challenge yourself!");
			player.sendMessage(error);
		}
		
		// Sending the challenge request
		else if(!message.isAcceptMessage())
		{
			other.sendMessage(new ChallengeMessage(player.getName(), player.getID()));
		}
		
		// Response from the challenged player
		else
		{
			// Get stance
			boolean response = message.getAccept();
			
			// Send the result to the challenger
			other.sendMessage(new ChallengeMessage(player.getName(), player.getID(), true, response));
			
			// Create a new game if the challenge was accepted
			if(response)
			{
				createGame(player, other);
			}
			
			// The challenge was declined
			else
			{
				NotificationMessage error = new NotificationMessage("'" + player.getName() + "' declined your request.");
				other.sendMessage(error);
			}
		}
	}
	
	/**
	 * Creates a new game session for a single player and the Server AI.
	 * 
	 * @param player	The challenger.
	 */
	private void createGame(Player player)
	{
		// The player isn't idle anymore
		player.setIdle(false);
		
		// Sessions are handled in separate threads
		(new Thread(new Session(player))).start();
	}
	
	/**
	 * Creates a new game session for two specific players.
	 * 
	 * @param first		Player #1.
	 * @param second	Player #2.
	 */
	private void createGame(Player first, Player second)
	{
		// The players aren't idle anymore
		first.setIdle(false);
		second.setIdle(false);
		
		// Sessions are handled in separate threads
		(new Thread(new Session(first, second))).start();
	}
}
