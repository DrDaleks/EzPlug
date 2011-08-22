package plugins.adufour.ezplug;

/**
 * Specialized implementation of {@link plugins.adufour.ezplug.EzVarNumericArray} for integer arrays
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarIntegerArray extends EzVarNumericArray<Integer>
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Creates a new integer variable with a given array of possible values
	 * 
	 * @param varName
	 *            the name of the variable (as it will appear on the interface)
	 * @param defaultValues
	 *            the list of possible values the user may choose from
	 * @param allowUserInput
	 *            set to true to allow the user to input its own value manually, false otherwise
	 * @throws NullPointerException
	 *             if the defaultValues parameter is null
	 */
	public EzVarIntegerArray(String varName, Integer[][] defaultValues, boolean allowUserInput) throws NullPointerException
	{
		super(varName, defaultValues, -1, allowUserInput);
	}
	
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
	 * @throws NullPointerException
	 *             if the defaultValues parameter is null
	 */
	public EzVarIntegerArray(String varName, Integer[][] defaultValues, int defaultValueIndex, boolean allowUserInput) throws NullPointerException
	{
		super(varName, defaultValues, defaultValueIndex, allowUserInput);
	}
	
	public Integer[] parseInput(String input)
	{
		String[] inputs = input.trim().split(" ");
		Integer[] values = new Integer[inputs.length];
		
		for (int i = 0; i < inputs.length; i++)
			values[i] = Integer.parseInt(inputs[i]);
		
		return values;
	}
}