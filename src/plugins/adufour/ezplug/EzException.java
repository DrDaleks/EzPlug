package plugins.adufour.ezplug;

/**
 * Convenience class for the EzPlug framework that allows to raise exceptions occurring within the
 * EzPlug layer to optimize bug finding / reporting / fixing.
 * 
 * @author Alexandre Dufour
 */
public class EzException extends RuntimeException
{
	private static final long	serialVersionUID	= 666L;
	
	final boolean				catchException;
	
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
