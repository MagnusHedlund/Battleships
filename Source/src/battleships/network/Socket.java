/*
 * Socket.java
 * Version 0.1 (2013-05-13)
 */

/*
 * TO-DO (requires functioning Message and DataParser classes)
 * - public void write(Message message) throws ConnectionException
 * - public Message read() throws ConnectionException
 */

package battleships.network;

/**
 * Handles the communication between two entities over the net
 * 
 * @author Christopher Nilsson
 */
public class Socket
{
	/**
	 * Is implemented by using the Socket class in the Net library
	 */
	private java.net.Socket internalSocket;
	
	/**
	 * Creates a connection to another entity over the net
	 * 
	 * @param address				The IP address of the entity to connect to
	 * @param port					Port number to send through
	 * @throws ConnectionException	Is thrown when a connection couldn't be established
	 */
	public void connect(String address, Integer port) throws ConnectionException
	{
		// Attempt to connect by using a specific address
		try
		{
			internalSocket = new java.net.Socket(address, port);
		} 
		
		// The establishment of a connection may fail
		catch (java.io.IOException e)
		{
			internalSocket = null;
			throw new ConnectionException("A connection could not be established to " + 
										  address + ":" + port + ".");
		}
	}
	
	/**
	 * Removes any connection this socket currently is responsible for
	 */
	public void disconnect()
	{
		// Disconnecting when not already connected makes no sense
		if(isConnected())
		{
			// Disable the connection
			try
			{
				internalSocket.close();
			} 
			
			// This exception does not really matter since the socket should be aborted anyway
			catch (java.io.IOException e)
			{
				// Do nothing
			}
			
			// Let the socket handle new connections later on
			internalSocket = null;
		}
	}
	
	/**
	 * Specifies the current state of the socket and its corresponding network connection
	 * 
	 * @return	Whether a connection is active
	 */
	public boolean isConnected()
	{
		return (internalSocket != null && internalSocket.isConnected() && 
				!internalSocket.isClosed());
	}
}
