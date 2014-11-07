package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

/**
 * Class defining a variable with a native array of type <code>float[]</code>. This class should be
 * preferred to {@link VarFloatArray} for optimized performances on large arrays
 * 
 * @author Alexandre Dufour
 */
public class VarFloatArrayNative extends VarGenericArray<float[]>
{
    /**
     * @param name
     * @param defaultValue
     */
    public VarFloatArrayNative(String name, float[] defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarFloatArrayNative(String name, float[] defaultValue, VarListener<float[]> defaultListener)
    {
        super(name, float[].class, defaultValue, defaultListener);
    }
    
    @Override
    public Object parseComponent(String s)
    {
        return Float.parseFloat(s);
    }
    
    @Override
    public float[] getValue(boolean forbidNull)
    {
        // handle the case where the reference is not an array
        
        @SuppressWarnings("rawtypes")
        Var reference = getReference();
        
        if (reference == null) return super.getValue(forbidNull);
        
        Object value = reference.getValue();
        
        if (value == null) return super.getValue(forbidNull);
        
        if (value instanceof Number) return new float[] { ((Number) value).floatValue() };
        
        return (float[]) value;
    }
}
