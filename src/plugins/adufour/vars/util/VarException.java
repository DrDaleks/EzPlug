package plugins.adufour.vars.util;

/**
 * Utility class used to notify users that a variable value is invalid
 * 
 * @author Alexandre Dufour
 * 
 */
@SuppressWarnings("serial")
public class VarException extends IllegalArgumentException
{
    /**
     * Creates a new exception with the specified message
     * 
     * @param message
     *            the error message behind this exception
     */
    public VarException(String message)
    {
        super(message);
    }
}
