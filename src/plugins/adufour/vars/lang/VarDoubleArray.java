package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;

/**
 * Class defining a variable holding an array of type {@link Double}.<br>
 * 
 * @deprecated For optimized performances on large arrays, consider using the
 *             {@link VarDoubleArrayNative} instead
 * @author Alexandre Dufour
 */
@Deprecated
public class VarDoubleArray extends VarArray<Double>
{
    public VarDoubleArray(String name, Double[] defaultValue)
    {
        super(name, Double[].class, defaultValue);
    }
    
    @Override
    public VarEditor<Double[]> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createTextField(this);
    }
    
    @Override
    public Object parseComponent(String s)
    {
        return Double.parseDouble(s);
    }
}
