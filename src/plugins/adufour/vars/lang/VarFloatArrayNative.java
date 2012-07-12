package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.TextField;

/**
 * Class defining a variable with a native array of type <code>float[]</code>. This class should be
 * preferred to {@link VarFloatArray} for optimized performances on large arrays
 * 
 * @author Alexandre Dufour
 * 
 */
public class VarFloatArrayNative extends VarGenericArray<float[]>
{
    public VarFloatArrayNative(String name, float[] defaultValue)
    {
        super(name, float[].class, defaultValue);
    }

    @Override
    public VarEditor<float[]> createVarEditor()
    {
        return new TextField<float[]>(this);
    }
    
    @Override
    public Object parseComponent(String s)
    {
        return Float.parseFloat(s);
    }
}
