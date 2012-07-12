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
}
