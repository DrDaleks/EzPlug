package plugins.adufour.ezplug;

@SuppressWarnings("serial")
public class EzException extends RuntimeException
{
    /**
     * The plug-in where the exception occurred
     */
    public final EzPlug  source;
    
    public final boolean catchException;
    
    /**
     * Creates a new instance of EzException with the specified message and catching behavior.
     * 
     * @deprecated use {@link #EzException(EzPlug, String, boolean)} to specify the source of this
     *             exception
     * @param message
     *            the error message bound the current exception
     * @param catchException
     *            true if the exception should be caught within the EzPlug layer, false to let the
     *            exception pass to the global exception manager
     */
    public EzException(String message, boolean catchException)
    {
        this(null, message, catchException);
    }
    
    /**
     * Creates a new instance of EzException with the specified message and catching behavior.
     * 
     * @param source
     *            The plug-in where the exception occurred
     * @param message
     *            the error message bound the current exception
     * @param catchException
     *            true if the exception should be caught within the EzPlug layer, false to let the
     *            exception pass to the global exception manager
     */
    public EzException(EzPlug source, String message, boolean catchException)
    {
        super(message);
        this.catchException = catchException;
        this.source = source;
    }
}
