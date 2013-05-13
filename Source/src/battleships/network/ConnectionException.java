/*
 * ConnectionException.java	
 * Version 1.0 (2013-05-13)
 */

package battleships.network;

/**
 * Exception that is thrown whenever the establishment of communication
 * with sockets fail
 * 
 * @author Christopher Nilsson
 */
public class ConnectionException extends Exception
{
    /**
     * Standard constructor
     * 
     * @param message	Error message
     */
    public ConnectionException(String message)
    {
        super(message);
    }

    /**
     * Constructor allowing for more detailed information
     * 
     * @param message	Error message
     * @param throwable	Specific data related to the exception
     */
    public ConnectionException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}