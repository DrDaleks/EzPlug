package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.model.RangeModel;
import plugins.adufour.vars.gui.swing.Spinner;
import plugins.adufour.vars.gui.swing.TextField;

/**
 * Class bringing support for variables handling comparable types
 * 
 * @author Alexandre Dufour
 * 
 */
public abstract class VarNumber<T extends Number> extends Var<T> implements Comparable<T>
{
    public VarNumber(String name, Class<T> type, T defaultValue)
    {
        super(name, type, defaultValue);
    }

    @Override
    public boolean isAssignableFrom(Var<?> source)
    {
        if (source.getType() == null) return false;
        
        Class<?> sourceType = source.getType();
        
        return sourceType == Double.TYPE || sourceType == Float.TYPE || sourceType == Integer.TYPE || Number.class.isAssignableFrom(source.getType());
    }

    @Override
    public VarEditor<T> createVarEditor()
    {
        if (getDefaultEditorModel() instanceof RangeModel) return new Spinner<T>(this);

        if (getDefaultEditorModel() == null) return new TextField<T>(this);

        return super.createVarEditor();
    }
}
