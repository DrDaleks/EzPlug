package plugins.adufour.vars.lang;

/**
 * Class defining a variable with a native array of type <code>float[]</code>. This class should be
 * preferred to {@link VarFloatArray} for optimized performances on large arrays
 * 
 * @author Alexandre Dufour
 */
public class VarFloatArrayNative extends VarGenericArray<float[]>
{
    public VarFloatArrayNative(String name, float[] defaultValue)
    {
        super(name, float[].class, defaultValue);
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
