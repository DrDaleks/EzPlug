package plugins.adufour.vars.lang;

/**
 * Convenience class defining a generic array of elements based on the specified inner type.<br/>
 * The type of the underlying array cannot be changed. To creates arrays of changeable types, use
 * {@link VarMutableArray} instead.
 * 
 * @deprecated use {@link VarGenericArray} instead
 * @author Alexandre Dufour
 * 
 * @param <T>
 *            the inner type of the array
 */
public class VarArray<T> extends VarGenericArray<T[]>
{
    public VarArray(String name, Class<T[]> type, T[] defaultValue)
    {
        super(name, type, defaultValue);
    }
}
