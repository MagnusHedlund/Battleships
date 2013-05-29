/*
 * Server.java	
 * Version 0.2 (2013-05-26)
 */

package battleships.server;


/**
 * Manages the creation of connections and players. Redirects the rest
 * to an instance of Lobby that handles other communication messages.
 * 
 * @author Christopher Nilsson
 */
public class Server
{	
	/**
	 * Manages players and sessions.
	 */
	private Lobby lobby;
	
	/**
	 * Listens for new connections, generating new players for the lobby.
	 */
	private Listener listener;
	
	/**
	 * Constructor.
	 */
	public Server()
	{
		listener = new Listener();
	}
	
	/**
	 * Activates the server.
	 * 
	 * @param port	At which port to listen for messages at.
	 */
	public void run(int port)
	{
		// State of execution
		boolean serverActive = true;
		
		// Begin listening for connections
		try
		{
			listener.start(port);
		}
		catch(Exception e)
		{
			System.out.println("Couldn't start server with port " + port + "! Aborting...");
			serverActive = false;
		}
		
		// Server logic
		while(serverActive)
		{
			// Continuously register new players as they connect
			Player newPlayer = listener.listen();
			if(newPlayer != null)
			{
				lobby.addPlayer(newPlayer);
			}
			
			// Let the lobby update itself
			lobby.update();
		}
		
		// Don't listen for new connections anymore
		listener.stop();
	}
}
