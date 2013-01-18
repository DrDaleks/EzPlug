package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.CheckBox;

/**
 * Boolean variable
 * 
 * @author Alexandre Dufour
 * 
 */
public class VarBoolean extends Var<Boolean>
{
	public VarBoolean(String name, Boolean defaultValue)
	{
		super(name, Boolean.TYPE, defaultValue);
	}
	
	@Override
	public Boolean parse(String s)
	{
		return Boolean.parseBoolean(s);
	}
	
	/**
	 * Inverts the value of this variable.<br>
	 * Warning: this method does nothing if the current variable references another
	 * 
	 * @return the variable value after the change
	 */
	public Boolean toggleValue()
	{
		Boolean value = getValue();
		
		if (getReference() != null) return value;
		
		Boolean newValue = !value;
		
		setValue(newValue);
		
		return newValue;
	}

    @Override
    public VarEditor<Boolean> createVarEditor()
    {
        return new CheckBox(this);
    }
}
