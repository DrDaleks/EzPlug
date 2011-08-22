package plugins.adufour.ezplug;

import javax.swing.SpinnerNumberModel;

/**
 * Specialized implementation of {@link plugins.adufour.ezplug.EzVarNumeric} for variables of type double
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarDouble extends EzVarNumeric<Double>
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Creates a new integer variable with default minimum and maximum values, and a default step
	 * size of 0.01
	 */
	public EzVarDouble(String varName)
	{
		this(varName, 0, 0, Double.MAX_VALUE, 0.01);
	}
	
	/**
	 * Creates a new integer variable with specified minimum and maximum values
	 * 
	 * @param varName
	 *            the name of the variable (as it will appear on the interface)
	 * @param min
	 *            the minimum allowed value
	 * @param max
	 *            the maximum allowed value
	 * @param step
	 *            the step between consecutive values
	 */
	public EzVarDouble(String varName, double min, double max, double step)
	{
		this(varName, min, min, max, step);
	}
	
	/**
	 * Creates a new integer variable with specified default, minimum and maximum values
	 * 
	 * @param varName
	 *            the name of the variable (as it will appear on the interface)
	 * @param value
	 *            the default value
	 * @param min
	 *            the minimum allowed value
	 * @param max
	 *            the maximum allowed value
	 * @param step
	 *            the step between consecutive values
	 */
	public EzVarDouble(String varName, double value, double min, double max, double step)
	{
		super(varName, new SpinnerNumberModel(value, min, max, step));
	}
	
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
	public EzVarDouble(String varName, Double[] defaultValues, boolean allowUserInput) throws NullPointerException
	{
		this(varName, defaultValues, -1, allowUserInput);
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
	public EzVarDouble(String varName, Double[] defaultValues, int defaultValueIndex, boolean allowUserInput) throws NullPointerException
	{
		super(varName, defaultValues, defaultValueIndex, allowUserInput);
	}

	@Override
	public Double parseInput(String s)
	{
		return Double.parseDouble(s);
	}
}
