package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.TextField;

/**
 * Class defining a variable with a native array of type <code>int[]</code>. This class should be
 * preferred to {@link VarIntegerArray} for optimized performances on large arrays
 * 
 * @author Alexandre Dufour
 * 
 */
public class VarIntegerArrayNative extends VarGenericArray<int[]>
{
    public VarIntegerArrayNative(String name, int[] defaultValue)
    {
        super(name, int[].class, defaultValue);
    }

    @Override
    public VarEditor<int[]> createVarEditor()
    {
        return new TextField<int[]>(this);
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
