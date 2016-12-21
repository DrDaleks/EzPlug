package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.model.ValueSelectionModel;
import plugins.adufour.vars.util.VarListener;

public class VarEnum<T extends Enum<T>> extends Var<T>
{
    /**
     * @param name
     * @param defaultValue
     * @throws NullPointerException
     *             if defaultValue is null
     */
    public VarEnum(String name, T defaultValue) throws NullPointerException
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     * @param defaultValue
     *            the default enumeration value (warning: cannot be <code>null</code> !!)
     * @throws NullPointerException
     *             if defaultValue is null
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarEnum(String name, T defaultValue, VarListener<T> defaultListener) throws NullPointerException
    {
        super(name, defaultValue.getDeclaringClass(), defaultValue, defaultListener);
        setDefaultEditorModel(new ValueSelectionModel<T>((T[]) defaultValue.getDeclaringClass().getEnumConstants(), defaultValue, false));
    }
    
    @Override
    public String getValueAsString()
    {
        return getValue().name();
    }
    
    /**
     * Parses the given string into an Enumeration value
     */
    @Override
    public T parse(String s)
    {
        return Enum.valueOf(getType(), s);
    }
}
