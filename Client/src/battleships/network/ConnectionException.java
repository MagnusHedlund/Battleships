/*
 * ConnectionException.java	
 * Version 1.0 (2013-05-20)
 */

package battleships.network;

/**
 * Exception that is thrown whenever the establishment of communication
 * with sockets fail. This is also used when listening for new connections,
 * describing that no server socket could be established.
 * 
 * @author Christopher Nilsson
 */
@SuppressWarnings("serial")
public class ConnectionException extends Exception
{
    /**
     * Standard constructor.
     * 
     * @param message	Error message.
     */
    public ConnectionException(String message)
    {
        super(message);
    }

    /**
     * Constructor allowing for more detailed information.
     * 
     * @param message	Error message.
     * @param throwable	Specific data related to the exception.
     */
    public ConnectionException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}