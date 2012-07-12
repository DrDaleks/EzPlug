package plugins.adufour.vars.lang;

public class VarFloat extends VarNumber<Float>
{
    public VarFloat(String name, Float defaultValue)
    {
        this(name, defaultValue == null ? 0f : defaultValue.floatValue());
    }
    
    public VarFloat(String name, float defaultValue)
    {
        super(name, Float.class, defaultValue);
    }
    
	@Override
	public Float parse(String s)
	{
		return Float.parseFloat(s);
	}
	
	@Override
	public int compareTo(Float f)
	{
		return getValue().compareTo(f);
	}
	
	/**
	 * Returns a Float representing the variable value.<br>
	 * NOTE: if the current variable references a variable of different (wider) type, truncation
	 * will occur
	 */
	public Float getValue()
	{
		Number number = (Number) super.getValue();
    	
    	return number == null ? null : number.floatValue();
	}
}
