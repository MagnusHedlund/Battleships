/*
 * Socket.java
 * Version 1.1 (2013-06-01)
 */

package battleships.network;

import battleships.message.Message;

/**
 * Handles the communication between two entities over a network.
 * 
 * @author Christopher Nilsson
 */
public class Socket
{
	/**
	 * Is implemented by using the Socket class in the Net library.
	 */
	private java.net.Socket internalSocket;
	
	/**
	 * Writes to the socket.
	 */
	private java.io.PrintWriter out;
	
	/**
	 * Reads from the socket.
	 */
	private java.io.BufferedReader in;

	/**
	 * Creates a connection to another entity over the net.
	 * 
	 * @param address				The IP address of the entity to connect to.
	 * @param port					Port number to send messages through.
	 * @throws ConnectionException	Is thrown when a connection couldn't be established.
	 */
	public void connect(String address, Integer port) throws ConnectionException
	{
		// Attempt to connect by using a specific address
		try
		{
			// Socket management
			internalSocket = new java.net.Socket(address, port);
			internalSocket.setSoTimeout(1);
			
			// Streams for reading and writing through this socket
			out = new java.io.PrintWriter(internalSocket.getOutputStream(), true);
			in = new java.io.BufferedReader(new java.io.InputStreamReader(internalSocket.getInputStream()));
		} 
		
		// The establishment of a connection may fail
		catch(Exception e)
		{
			internalSocket = null;
			throw new ConnectionException("A connection could not be established to " + 
										  address + ":" + port + ".");
		}
	}
	
	/**
	 * Attaches a raw socket that already has established a connection.
	 * 
	 * @param socket				Prepared raw socket.
	 * @throws ConnectionException	Is thrown if the raw socket is invalid.
	 */
	public void connect(java.net.Socket rawSocket) throws ConnectionException
	{
		// Validate the socket
		internalSocket = rawSocket;
		if(!isConnected())
		{
			throw new ConnectionException("Invalid raw socket");			
		}
		
		// Streams for reading and writing through this socket
		try
		{
			out = new java.io.PrintWriter(internalSocket.getOutputStream(), true);
			in = new java.io.BufferedReader(new java.io.InputStreamReader(internalSocket.getInputStream()));
		}
		catch (Exception e)
		{
			throw new ConnectionException("Stream failure!");		
		}
	}
	
	/**
	 * Removes any connection this socket currently is responsible for.
	 */
	public void disconnect()
	{
		// Disconnecting when not already connected makes no sense
		if(isConnected())
		{
			// Disable the connection
			try
			{
				out.close();
				in.close();
				internalSocket.close();
			} 
			
			// This exception does not really matter since the socket should be aborted anyway
			catch (Exception e)
			{
				// Do nothing
			}
			
			// Let the socket handle new connections later on
			out = null;
			in = null;
			internalSocket = null;
		}
	}
	
	/**
	 * Specifies the current state of the socket and its corresponding network connection.
	 * 
	 * @return	Whether a connection is active
	 */
	public boolean isConnected()
	{
		return (internalSocket != null && internalSocket.isConnected() && !internalSocket.isClosed());
	}
	
	/**
	 * Writes a message that will be sent through the socket.
	 * 
	 * @param message					Message to be delivered.
	 */
	public void write(Message message)
	{
		out.println(message.toXML());			
	}
	
	/**
	 * Reads a message coming through the socket.
	 * 
	 * @return	Message coming through the socket. Is set to <i>null</i> if the sender
	 * 			hasn't given any messages that have not been read previously.
	 */
	public Message read()
	{
		// Reading
		String data = null;
		try
		{
			// Retrieve information if there's any to fetch
			if(internalSocket.getInputStream().available() > 0)
			{
				data = in.readLine();
			}
		} 
		catch (Exception e)
		{
			return null;
		}
		
		// Returning a message
		if(data != null && data.length() > 0)
		{
			return Message.toMessage(data);
		}
		return null;
	}
}
