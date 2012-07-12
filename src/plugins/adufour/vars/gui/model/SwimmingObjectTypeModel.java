package plugins.adufour.vars.gui.model;

import icy.swimmingPool.SwimmingObject;

public class SwimmingObjectTypeModel<T> implements VarEditorModel<SwimmingObject>
{
    @Override
    public boolean isValid(SwimmingObject value)
    {
        try
        {
            // if the value casts to T, the swimming object is valid
            @SuppressWarnings({ "unchecked", "unused" })
            T cast = (T) value.getObject();
            return true;
        }
        catch (ClassCastException ccE)
        {
            return false;
        }
    }

    @Override
    public SwimmingObject getDefaultValue()
    {
        return null;
    }
}