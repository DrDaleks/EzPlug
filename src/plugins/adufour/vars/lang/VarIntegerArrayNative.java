package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

/**
 * Class defining a variable with a native array of type <code>int[]</code>. This class should be
 * preferred to {@link VarIntegerArray} for optimized performances on large arrays
 * 
 * @author Alexandre Dufour
 */
public class VarIntegerArrayNative extends VarGenericArray<int[]>
{
    /**
     * @param name
     * @param defaultValue
     */
    public VarIntegerArrayNative(String name, int[] defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarIntegerArrayNative(String name, int[] defaultValue, VarListener<int[]> defaultListener)
    {
        super(name, int[].class, defaultValue, defaultListener);
    }
    
    @Override
    public Object parseComponent(String s)
    {
        return Integer.parseInt(s);
    }
    
    @Override
    public int[] getValue(boolean forbidNull)
    {
        // handle the case where the reference is not an array
        
        @SuppressWarnings("rawtypes")
        Var reference = getReference();
        
        if (reference == null) return super.getValue(forbidNull);
        
        Object value = reference.getValue();
        
        if (value == null) return super.getValue(forbidNull);
        
        if (value instanceof Number) return new int[] { ((Number) value).intValue() };
        
        return (int[]) value;
    }
}
