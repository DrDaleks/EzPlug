package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

public class VarObject extends Var<Object>
{
    /**
     * @param name
     * @param defaultValue
     */
    public VarObject(String name, Object defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarObject(String name, Object defaultValue, VarListener<Object> defaultListener)
    {
        super(name, Object.class, defaultValue, defaultListener);
    }
    
    @Override
    public boolean isAssignableFrom(Var<?> source)
    {
        // an Object type variable may always point to any DEFINED type
        
        return getType() != null;
    }
}
