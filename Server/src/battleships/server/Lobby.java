/*
 * Lobby.java	
 * Version 0.2 (2013-05-28)
 */

package battleships.server;

/**
 * Listens for messages and either acts upon them when it's related to
 * the lobby, or redirects them to the appropriate Session thread. One
 * responsibility is to generate a new thread when a game between two
 * clients is desired.
 * 
 * @author Magnus Hedlund & Christopher Nilsson
 */
public class Lobby
{
	/**
	 * Performs updates related to players and sessions depending on
	 * messages received from the clients.
	 */
	public void update()
	{
		/*
		 * Read messages from clients and act upon them (some should invoke
		 * an update method in the appropriate Session thread? This makes
		 * the developer of game logic not have to bother with such details)
		 */
		
		// TO-DO
		
		// WORK IN PROGRESS
	}
	
	/**
	 * Takes a new player and adds him or her to the lobby.
	 * 
	 * @param newPlayer	A player that just connected to the server.
	 */
	public void addPlayer(Player newPlayer)
	{
		// TO-DO
	}
}
