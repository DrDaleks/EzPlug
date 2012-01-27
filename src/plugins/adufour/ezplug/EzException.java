package plugins.adufour.ezplug;

public class EzException extends RuntimeException
{
	private static final long	serialVersionUID	= 666L;
	
	public final boolean		catchException;
	
	/**
	 * Creates a new instance of EzException with the specified message and catching behavior.
	 * 
	 * @param message
	 *            the error message bound the current exception
	 * @param catchException
	 *            true if the exception should be caught within the EzPlug layer, false to let the
	 *            exception pass to the global exception manager
	 */
	public EzException(String message, boolean catchException)
	{
		super(message);
		this.catchException = catchException;
	}
}
