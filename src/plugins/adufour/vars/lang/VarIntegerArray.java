package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;

/**
 * Class defining a variable holding an array of type {@link Integer}.<br>
 * 
 * @deprecated For optimized performances on large arrays, consider using the
 *             {@link VarIntegerArrayNative} instead
 * @author Alexandre Dufour
 */
public class VarIntegerArray extends VarArray<Integer>
{
    public VarIntegerArray(String name, Integer[] defaultValues)
    {
        super(name, Integer[].class, defaultValues);
    }
    
    @Override
    public VarEditor<Integer[]> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createTextField(this);
    }
    
    @Override
    public Object parseComponent(String s)
    {
        return Integer.parseInt(s);
    }
}