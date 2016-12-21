package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;

public class VarFloat extends VarNumber<Float>
{
    /**
     * @deprecated use {@link #VarFloat(String, float)} instead
     * @param name
     * @param defaultValue
     */
    @Deprecated
    public VarFloat(String name, Float defaultValue)
    {
        this(name, defaultValue == null ? 0f : defaultValue.floatValue());
    }
    
    /**
     * 
     * @param name
     * @param defaultValue
     */
    public VarFloat(String name, float defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * 
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarFloat(String name, float defaultValue, VarListener<Float> defaultListener)
    {
        super(name, Float.TYPE, defaultValue, defaultListener);
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
	
	@Override
	public Float getValue()
	{
	    return getValue(false);
	}
	
	/**
	 * Returns a Float representing the variable value.<br>
	 * NOTE: if the current variable references a variable of different (wider) type, truncation
	 * will occur
	 */
	public Float getValue(boolean forbidNull)
	{
		Number number = super.getValue(forbidNull);
    	
    	return number == null ? null : number.floatValue();
	}
}
