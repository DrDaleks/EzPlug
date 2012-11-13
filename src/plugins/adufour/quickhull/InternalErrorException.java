package plugins.adufour.quickhull;

/**
 * Exception thrown when QuickHull3D encounters an internal error.
 */
public class InternalErrorException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InternalErrorException (String msg)
	 { super (msg);
	 }
}
