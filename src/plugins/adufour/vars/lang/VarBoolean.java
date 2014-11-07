package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

/**
 * Boolean variable
 * 
 * @author Alexandre Dufour
 */
public class VarBoolean extends Var<Boolean>
{
    /**
     * @param name
     *            the variable name
     * @param defaultValue
     *            the default value
     */
    public VarBoolean(String name, Boolean defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     *            the variable name
     * @param defaultValue
     *            the default value
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarBoolean(String name, Boolean defaultValue, VarListener<Boolean> defaultListener)
    {
        super(name, Boolean.TYPE, defaultValue, defaultListener);
    }
    
    @Override
    public Boolean parse(String s)
    {
        return Boolean.parseBoolean(s);
    }
    
    /**
     * Inverts the value of this variable.<br>
     * Warning: this method does nothing if the current variable references another
     * 
     * @return the variable value after the change
     */
    public Boolean toggleValue()
    {
        Boolean value = getValue();
        
        if (getReference() != null) return value;
        
        Boolean newValue = !value;
        
        setValue(newValue);
        
        return newValue;
    }
    
    @Override
    public VarEditor<Boolean> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createCheckBox(this);
    }
}
