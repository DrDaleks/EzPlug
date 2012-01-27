package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarBoolean;

/**
 * Boolean variable for the ezPlug framework. Creates a JCheckBox component to select between true
 * and false
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarBoolean extends EzVar<Boolean>
{
	/**
	 * Constructs a new input variable holding a boolean value with a JCheckBox for graphical user
	 * interface
	 * 
	 * @param varName
	 *            the name of the variable
	 * @param defaultValue
	 *            the default state of the JCheckBox
	 */
	public EzVarBoolean(String varName, final boolean defaultValue)
	{
		super(new VarBoolean(varName, defaultValue), null);
	}
	
	/**
	 * Legacy method
	 */
	public void setValue(Boolean value)
	{
		super.setValue(value);
	}
}
