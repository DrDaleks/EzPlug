package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.TextField;

/**
 * Class defining a variable holding an array of type {@link Float}.<br>
 * 
 * @deprecated For optimized performances on large arrays, consider using the
 *             {@link VarFloatArrayNative} instead
 * @author Alexandre Dufour
 * 
 */
@Deprecated
public class VarFloatArray extends VarArray<Float>
{
    public VarFloatArray(String name, Float[] defaultValues)
    {
        super(name, Float[].class, defaultValues);
    }
    
    @Override
    public VarEditor<Float[]> createVarEditor()
    {
        return new TextField<Float[]>(this);
    }
    
    @Override
    public Object parseComponent(String s)
    {
        return Float.parseFloat(s);
    }
}
