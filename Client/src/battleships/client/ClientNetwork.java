package battleships.client;
import battleships.message.Message;
import battleships.network.ConnectionException;
import battleships.network.Socket;

/**
 * Socket Wrapper for Client.
 * 
 * @author Fredrik Strömbergsson
 */
public class ClientNetwork {
	private Socket s = new Socket();
	
	/**
	 * Connect to the server
	 * 
	 * @param address	IP Address
	 * @param port		Port Number
	 */
	public boolean connect(String address, String port) {
		
		try 
		{	// Anslut till servern
			s.connect(address, Integer.parseInt(port));
		} 
		catch (NumberFormatException e) {
			return false;
		}
		catch (ConnectionException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Disconnect from server
	 */
	public void disconnect() {s.disconnect();}
	
	/**
	 * Send a message to the server
	 * @param msg	The Message
	 */	
	public void sendMessage(Message msg) {s.write(msg);}
	
	/**
	 * Get a message
	 * 
	 * @return	A message
	 */	
	public Message getMessage() {return s.read();}
	
}
