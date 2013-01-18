package plugins.adufour.vars.lang;

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
    
    public VarInteger(String name, int defaultValue)
    {
        super(name, Integer.TYPE, defaultValue);
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
