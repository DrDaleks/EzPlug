package plugins.adufour.ezplug;

/**
 * Class defining a enumeration type variable. <br>
 * The graphical component is a combo box containing some (or all) values of the enumeration
 * 
 * @author Alexandre Dufour
 * 
 * @param <E>
 *            The enumeration type
 */
public class EzVarEnum<E extends Enum<?>> extends EzVar<E> implements EzVar.Storable<Integer>
{
	private static final long	serialVersionUID	= 1L;

	private final Class<E>	clazz;
	
	/**
	 * Constructs a new input VarEnum variable
	 * 
	 * @param varName
	 *            the variable name
	 * @param values
	 *            the values to choose from. The full list of enumeration values can be obtained
	 *            with the E.values() method, where E is the enumeration type
	 */
	public EzVarEnum(String varName, E[] values)
	{
		this(varName, values, 0);
	}
	
	/**
	 * Constructs a new input VarEnum variable
	 * 
	 * @param varName
	 *            the variable name
	 * @param values
	 *            the values to choose from. The full list of enumeration values can be obtained
	 *            with the E.values() method, where E is the enumeration type
	 * @param defaultValueIndex
	 *            the zero-based index of the enumeration value to select by default
	 */
	@SuppressWarnings("unchecked")
	public EzVarEnum(String varName, E[] values, int defaultValueIndex)
	{
		super(varName, values, defaultValueIndex, false);
		clazz = (Class<E>) values[0].getClass();
	}
	
	/**
	 * Constructs a new input VarEnum variable
	 * 
	 * @param varName
	 *            the variable name
	 * @param values
	 *            the values to choose from. The full list of enumeration values can be obtained
	 *            with the E.values() method, where E is the enumeration type
	 * @param defaultValue
	 *            the enumeration value to select by default
	 */
	@SuppressWarnings("unchecked")
	public EzVarEnum(String varName, E[] values, E defaultValue)
	{
		super(varName, values, defaultValue.ordinal(), false);
		clazz = (Class<E>) values[0].getClass();
	}
	
	@Override
	public Integer getXMLValue()
	{
		return getValue().ordinal();
	}
	
	@Override
	public void setXMLValue(Integer value)
	{
		setValue(clazz.getEnumConstants()[value]);
	}
}
