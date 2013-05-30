/*
 * Server.java	
 * Version 1.0 (2013-05-30)
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
		lobby = new Lobby();
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
		System.out.println("Attempting to start the server process...");
		try
		{
			listener.start(port);
			System.out.println("Server started sucessfully, listening at port #" + port + "!");
		}
		catch(Exception e)
		{
			System.out.println("Server could not be initialized properly!");
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
