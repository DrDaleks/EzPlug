package plugins.adufour.vars.lang;

public class VarDouble extends VarNumber<Double>
{
    /**
     * @deprecated use {@link #VarDouble(String, double)} instead
     * @param name
     * @param defaultValue
     */
    public VarDouble(String name, Double defaultValue)
    {
        this(name, defaultValue == null ? 0.0 : defaultValue.doubleValue());
    }
    
    public VarDouble(String name, double defaultValue)
    {
        super(name, Double.TYPE, defaultValue);
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
        Number number = (Number) super.getValue();
        
        return number == null ? null : number.doubleValue();
    }
}
