package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarException;
import plugins.adufour.vars.util.VarListener;

public class VarDouble extends VarNumber<Double>
{
    /**
     * @deprecated use {@link #VarDouble(String, double)} instead
     * @param name
     * @param defaultValue
     */
    @Deprecated
    public VarDouble(String name, Double defaultValue)
    {
        this(name, defaultValue == null ? 0.0 : defaultValue.doubleValue());
    }
    
    /**
     * @param name
     * @param defaultValue
     */
    public VarDouble(String name, double defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarDouble(String name, double defaultValue, VarListener<Double> defaultListener)
    {
        super(name, Double.TYPE, defaultValue, defaultListener);
    }
    
    @Override
    public Double parse(String s)
    {
        return Double.parseDouble(s);
    }
    
    @Override
    public int compareTo(Double d)
    {
        return getValue().compareTo(d);
    }
    
    @Override
    public Double getValue()
    {
        return getValue(false);
    }
    
    @Override
    public Double getValue(boolean forbidNull) throws VarException
    {
        Number number = super.getValue(forbidNull);
        
        return number == null ? null : number.doubleValue();
    }
}
