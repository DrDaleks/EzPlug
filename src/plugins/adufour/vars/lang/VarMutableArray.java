package plugins.adufour.vars.lang;

import java.lang.reflect.Array;

/**
 * Variable holding an array of mutable type
 * 
 * @author Alexandre Dufour
 * 
 */
public class VarMutableArray extends VarMutable
{
    public VarMutableArray(String name, Class<?> initialType)
    {
        super(name, initialType);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean isAssignableFrom(Var source)
    {
        return super.isAssignableFrom(source) && source.getType().isArray();
    }
    
    /**
     * @param index
     *            the index to retrieve
     * @return the array element at the specified index
     * @throws NullPointerException
     *             if the variable value is null
     * @throws ClassCastException
     *             if the inferred type is incompatible with the array element
     */
    @SuppressWarnings("unchecked")
    public <T> T getElementAt(int index) throws NullPointerException, ClassCastException
    {
        return (T) Array.get(getValue(), index);
    }
    
    /**
     * @return The size of this array, or -1 if the array is <code>null</code>
     */
    public int size()
    {
        return getValue() == null ? -1 : Array.getLength(getValue());
    }
}
