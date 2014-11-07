package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

public class VarInteger extends VarNumber<Integer>
{
    /**
     * @deprecated use {@link #VarInteger(String, int)} instead
     * @param name
     * @param defaultValue
     */
    public VarInteger(String name, Integer defaultValue)
    {
        this(name, defaultValue == null ? 0 : defaultValue.intValue());
    }
    
    /**
     * @param name
     * @param defaultValue
     */
    public VarInteger(String name, int defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarInteger(String name, int defaultValue, VarListener<Integer> defaultListener)
    {
        super(name, Integer.TYPE, defaultValue, defaultListener);
    }
    
    @Override
    public Integer parse(String s)
    {
        return Integer.parseInt(s);
    }
    
    @Override
    public int compareTo(Integer integer)
    {
        return getValue().compareTo(integer);
    }
    
    /**
     * Returns a Float representing the variable value.<br>
     * NOTE: if the current variable references a variable of different (wider) type, truncation
     * will occur
     */
    public Integer getValue()
    {
        Number number = (Number) super.getValue();
        
        return number == null ? null : number.intValue();
    }
}
