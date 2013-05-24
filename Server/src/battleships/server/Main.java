/*
 * Main.java
 * Version 1.0 (2013-05-20)
 */

package battleships.server;

/**
 * Initializes the server with an appropriate port.
 * 
 * @author Christopher Nilsson
 */
public class Main
{	
	/**
	 * Port used when listening for new client connections.
	 */
	private static final int SERVER_PORT = 5000;
	
	/**
	 * Starting point of the program.
	 * 
	 * @param args	Application arguments.
	 */
	public static void main(String[] args)
	{
		Server server = new Server();
		server.run(SERVER_PORT);
	}
}
