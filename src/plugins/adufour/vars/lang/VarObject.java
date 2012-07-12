package plugins.adufour.vars.lang;

public class VarObject extends Var<Object>
{
    public VarObject(String name, Object defaultValue)
    {
        super(name, Object.class, defaultValue);
    }

    @Override
    public boolean isAssignableFrom(Var<?> source)
    {
        // an Object type variable may always point to any DEFINED type

        return getType() != null;
    }
}
