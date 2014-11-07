package plugins.adufour.vars.lang;

import icy.swimmingPool.SwimmingObject;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarSwimmingObject extends Var<SwimmingObject>
{
    /**
     * @param name
     * @param defaultValue
     */
    public VarSwimmingObject(String name, SwimmingObject defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarSwimmingObject(String name, SwimmingObject defaultValue, VarListener<SwimmingObject> defaultListener)
    {
        super(name, SwimmingObject.class, defaultValue, defaultListener);
    }
    
    @Override
    public VarEditor<SwimmingObject> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createSwimmingObjectChooser(this);
    }
    
    @Override
    public String getValueAsString()
    {
        SwimmingObject obj = getValue(false);
        
        if (obj == null) return "";
        
        return obj.getName() + " (type: " + obj.getObjectSimpleClassName() + ")";
    }
}
