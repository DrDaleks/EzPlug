package plugins.adufour.vars.lang;

import java.lang.reflect.Array;

import plugins.adufour.vars.util.VarException;
import plugins.adufour.vars.util.VarListener;

/**
 * Variable holding an array of mutable type
 * 
 * @author Alexandre Dufour
 */
public class VarMutableArray extends VarMutable
{
    /**
     * @param name
     * @param initialType
     */
    public VarMutableArray(String name, Class<?> initialType)
    {
        this(name, initialType, null);
    }
    
    /**
     * @param name
     * @param initialType
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarMutableArray(String name, Class<?> initialType, VarListener<?> defaultListener)
    {
        super(name, initialType, defaultListener);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean isAssignableFrom(Var source)
    {
        return super.isAssignableFrom(source);// && source.getType().isArray();
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
    
    @Override
    public Object getValue(boolean forbidNull) throws VarException
    {
        // handle the case where the reference is not an array
        
        @SuppressWarnings("rawtypes")
        Var reference = getReference();
        
        if (reference == null) return super.getValue(forbidNull);
        
        Object value = reference.getValue();
        
        if (value == null) return super.getValue(forbidNull);
        
        if (value.getClass().isArray()) return value;
        
        Object array = Array.newInstance(value.getClass(), 1);
        Array.set(array, 0, value);
        return array;
    }
}
