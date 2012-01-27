package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarNumbers;

/**
 * Float arrays
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarFloatArray extends EzVar<Float[]>
{
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
    public EzVarFloatArray(String varName, Float[][] defaultValues, boolean allowUserInput) throws NullPointerException
    {
        this(varName, defaultValues, 0, allowUserInput);
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
    public EzVarFloatArray(String varName, Float[][] defaultValues, int defaultValueIndex, boolean allowUserInput) throws NullPointerException
    {
        super(new VarNumbers.Float(varName, null), defaultValues, defaultValueIndex, allowUserInput);
    }
}