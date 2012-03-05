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
}
