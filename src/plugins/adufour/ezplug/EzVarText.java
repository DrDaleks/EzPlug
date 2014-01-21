package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarString;

/**
 * Class defining a variable of type String, embarking a textfield as graphical component
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarText extends EzVar<String>
{
    /**
     * Creates a new Text variable
     * 
     * @param varName
     *            the name of the variable (as it will appear on the interface)
     */
    public EzVarText(String varName)
    {
        this(varName, 1);
    }
    
    /**
     * Creates a new Text variable
     * 
     * @param varName
     *            the name of the variable (as it will appear on the interface)
     * @param nbLines
     *            the number of lines to display on the interface
     */
    public EzVarText(String varName, int nbLines)
    {
        this(varName, "", nbLines);
    }
    
    /**
     * Creates a new Text variable
     * 
     * @param varName
     *            the name of the variable (as it will appear on the interface)
     * @param defaultText
     *            the default text to enter (the number of lines will be derived from this text)
     */
    public EzVarText(String varName, String defaultText)
    {
        this(varName, defaultText, defaultText.split("\n").length);
    }
    
    /**
     * Creates a new Text variable
     * 
     * @param varName
     *            the name of the variable (as it will appear on the interface)
     * @param defaultText
     *            the default text to enter
     * @param nbLines
     *            the number of lines to display on the interface
     */
    public EzVarText(String varName, String defaultText, int nbLines)
    {
        super(new VarString(varName, defaultText, nbLines), null);
    }
    
    /**
     * Creates a new Text input variable with a list of default values
     * 
     * @param varName
     *            the variable name
     * @param defaultValues
     *            the list of default values the user may choose from
     * @param allowUserInput
     *            set to true if the user may input text manually instead of selecting a default
     *            value, or false to forbid user input
     */
    public EzVarText(String varName, String[] defaultValues, Boolean allowUserInput)
    {
        this(varName, defaultValues, 0, allowUserInput);
    }
    
    /**
     * Creates a new Text input variable with a list of default values and a default selection
     * 
     * @param varName
     *            the variable name
     * @param defaultValues
     *            the list of default values the user may choose from
     * @param defaultValueIndex
     *            the index of the default selected value
     * @param allowUserInput
     *            set to true if the user may input text manually instead of selecting a default
     *            value, or false to forbid user input
     */
    public EzVarText(String varName, String[] defaultValues, int defaultValueIndex, Boolean allowUserInput)
    {
        super(new VarString(varName, null), defaultValues, defaultValueIndex, allowUserInput);
    }
}