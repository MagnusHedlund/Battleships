package battleships.message;

@SuppressWarnings("serial")
public class InvalidMessageException extends Exception
{
    /**
     * Standard constructor.
     * 
     * @param message	Error message.
     */
    public InvalidMessageException(String message)
    {
        super(message);
    }

    /**
     * Constructor allowing for more detailed information.
     * 
     * @param message	Error message.
     * @param throwable	Specific data related to the exception.
     */
    public InvalidMessageException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
