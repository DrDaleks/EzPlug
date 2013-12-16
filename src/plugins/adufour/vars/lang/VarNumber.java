package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.gui.model.RangeModel;
import plugins.adufour.vars.gui.model.RangeModel.RangeEditorType;
import plugins.adufour.vars.gui.model.VarEditorModel;

/**
 * Class bringing support for variables handling comparable types
 * 
 * @author Alexandre Dufour
 */
public abstract class VarNumber<N extends Number> extends Var<N> implements Comparable<N>
{
    public VarNumber(String name, Class<N> type, N defaultValue)
    {
        super(name, type, defaultValue);
    }
    
    public VarNumber(String name, VarEditorModel<N> model)
    {
        super(name, model);
    }
    
    @Override
    public boolean isAssignableFrom(Var<?> source)
    {
        if (source.getType() == null) return false;
        
        Class<?> sourceType = source.getType();
        
        return sourceType == Double.TYPE || sourceType == Float.TYPE || sourceType == Integer.TYPE || Number.class.isAssignableFrom(source.getType());
    }
    
    @Override
    public VarEditor<N> createVarEditor()
    {
        VarEditorFactory factory = VarEditorFactory.getDefaultFactory();
        
        if (getDefaultEditorModel() instanceof RangeModel)
        {
            RangeEditorType editorType = ((RangeModel<N>) getDefaultEditorModel()).getEditorType();
            switch (editorType)
            {
            case SLIDER:
                return factory.createSlider(this);
            case SPINNER:
                return factory.createSpinner(this);
            default:
                throw new UnsupportedOperationException("Editor not yet implemented: " + editorType);
            }
        }
        
        if (getDefaultEditorModel() == null) return factory.createTextField(this);
        
        return super.createVarEditor();
    }
}
