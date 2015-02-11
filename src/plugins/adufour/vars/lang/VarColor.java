package plugins.adufour.vars.lang;

import java.awt.Color;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarColor extends Var<Color>
{
    /**
     * Creates a new variable holding a OME-compatible {@link Color} object
     * 
     * @param name
     * @param defaultColor
     */
    public VarColor(String name, Color defaultColor)
    {
        this(name, defaultColor, null);
    }
            
    /**
     * Creates a new variable holding {@link Color} object
     * 
     * @param name
     * @param defaultColor
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarColor(String name, Color defaultColor, VarListener<Color> defaultListener)
    {
        super(name, defaultColor, defaultListener);
    }
        
    @Override
    public VarEditor<Color> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createColorChooser(this);
    }
}
