package plugins.adufour.vars.util;

import plugins.adufour.ezplug.EzVar;
import plugins.adufour.vars.lang.Var;

/**
 * Utility class used to notify users that a variable value is invalid
 * 
 * @author Alexandre Dufour
 */
@SuppressWarnings("serial")
public class VarException extends IllegalArgumentException
{
    /**
     * The variable causing the exception
     */
    public final Var<?> source;
    
    /**
     * Creates a new exception with the specified message
     * 
     * @deprecated use {@link #VarException(Var, String)} to specify the source of this exception
     * @param message
     *            the error message behind this exception
     */
    public VarException(String message)
    {
        this(null, message);
    }
    
    /**
     * Creates a new exception with the specified message
     * 
     * @param source
     *            the variable causing the exception (will be used to provide better user experience
     *            when displaying error messages). If the source of the exception is an
     *            {@link EzVar} object, indicate here its underlying variable, accessible via
     *            {@link EzVar#getVariable()}
     * @param message
     *            the error message behind this exception
     */
    public VarException(Var<?> source, String message)
    {
        super(message);
        this.source = source;
    }
}
