package client;
import message.Message;
import network.ConnectionException;
import network.Socket;

public class ClientNetwork {
	private Socket s;
	
	//-----------------------------------------
	// Ansluter till servern
	//-----------------------------------------
	public boolean connect(String address, String port) {
		
		s = new Socket();
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
	
	//-----------------------------------------
	// Disconnect
	//-----------------------------------------	
	public void disconnect() {s.disconnect();}
	
	//-----------------------------------------
	// Skicka meddelande
	//-----------------------------------------		
	public void sendMessage(Message msg) {s.write(msg);}
	
	//-----------------------------------------
	// Ta emot meddelande
	//-----------------------------------------		
	public Message getMessage() {return s.read();}
	
}
