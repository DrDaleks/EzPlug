package plugins.adufour.ezplug;

import icy.swimmingPool.SwimmingObject;
import plugins.adufour.vars.lang.ConstraintByType;
import plugins.adufour.vars.lang.VarSwimmingObject;

public class EzVarSwimmingObject<T> extends EzVar<SwimmingObject>
{
	public EzVarSwimmingObject(String varName)
	{
		super(new VarSwimmingObject(varName, null), new ConstraintByType<T>());
	}
		
	/**
	 * Legacy method
	 */
	@Override
	public SwimmingObject getValue()
	{
		return getValue(true);
	}
	
	/**
	 * Returns the currently selected sequence
	 * 
	 * @param throwExceptionIfNull
	 *            throws a EzException if the currently selected sequence is null
	 * @throws EzException
	 *             if the currently selection is null and throwExceptionIfNull is true.
	 */
	public SwimmingObject getValue(boolean throwExceptionIfNull) throws EzException
	{
		SwimmingObject value = getValue();
		
		if (value == null && throwExceptionIfNull) throw new EzException("Variable \"" + name + "\": No selection", true);

		return value;
	}
}
