package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.TextField;

/**
 * Class defining a variable with a native array of type <code>double[]</code>. This class should be
 * preferred to {@link VarDoubleArray} for optimized performances on large arrays
 * 
 * @author Alexandre Dufour
 * 
 */
public class VarDoubleArrayNative extends VarGenericArray<double[]>
{
    public VarDoubleArrayNative(String name, double[] defaultValue)
    {
        super(name, double[].class, defaultValue);
    }

    @Override
    public VarEditor<double[]> createVarEditor()
    {
        return new TextField<double[]>(this);
    }
    
    @Override
    public Object parseComponent(String s)
    {
        return Double.parseDouble(s);
    }
    
    @Override
    public double[] getValue(boolean forbidNull)
    {
        // handle the case where the reference is not an array
        
        @SuppressWarnings("rawtypes")
        Var reference = getReference();
        
        if (reference == null) return super.getValue(forbidNull);
        
        Object value = reference.getValue();
        
        if (value == null) return super.getValue(forbidNull);
        
        if (value instanceof Number) return new double[] { ((Number) value).doubleValue() };
        
        return (double[]) value;
    }
}
