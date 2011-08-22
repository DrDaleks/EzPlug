package plugins.adufour.ezplug;

import javax.swing.JComboBox;

/**
 * Abstract class defining a "numeric array"-type variable, representing arrays of numeric values
 * that the user may choose from or input its own using the variable's parsing abilities
 * 
 * @author Alexandre Dufour
 * 
 * @param <N>
 *            the type of numeric values used in the array
 */
public abstract class EzVarNumericArray<N extends Number> extends EzVar<N[]> implements EzTextParser<N[]>
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Creates a new integer variable with a given array of possible values
	 * 
	 * @param varName
	 *            the name of the variable (as it will appear on the interface)
	 * @param defaultValues
	 *            the list of possible values the user may choose from
	 * @param defaultValueIndex
	 *            the index of the default selected value
	 * @param allowUserInput
	 *            set to true to allow the user to input its own value manually, false otherwise
	 */
	protected EzVarNumericArray(String varName, N[][] defaultValues, int defaultValueIndex, boolean allowUserInput)
	{
		super(varName, defaultValues, defaultValueIndex, allowUserInput);
	}
	
	@Override
	public N[] getValue()
	{
		return getValue(true);
	}

	@SuppressWarnings("unchecked")
	public N[] getValue(boolean throwExceptionIfNull)
	{
		Object o = ((JComboBox) getComponent()).getSelectedItem();
		
		if (o == null)
		{
			if (throwExceptionIfNull) throw new EzException(name + " is empty", true);
			return null;
		}
		
		if (o instanceof String)
			return parseInput((String) o);
		
		if (o.getClass().isArray())
			return (N[]) o;
		
		return super.getValue();
	}
	
	public abstract N[] parseInput(String input) throws NumberFormatException;
}
