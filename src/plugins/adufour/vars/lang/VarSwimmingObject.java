package plugins.adufour.vars.lang;

import icy.swimmingPool.SwimmingObject;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.SwimmingObjectChooser;

public class VarSwimmingObject extends Var<SwimmingObject>
{
    public VarSwimmingObject(String name, SwimmingObject defaultValue)
    {
        super(name, SwimmingObject.class, defaultValue);
    }
    
    @Override
    public VarEditor<SwimmingObject> createVarEditor()
    {
        return new SwimmingObjectChooser(this);
    }
    
    @Override
    public String getValueAsString()
    {
        SwimmingObject obj = getValue(false);
        
        if (obj == null) return "";
        
        return obj.getName() + " (type: " + obj.getObjectSimpleClassName() + ")";
    }
}
