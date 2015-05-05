package plugins.adufour.ezplug;

import plugins.adufour.vars.gui.model.PasswordModel;
import plugins.adufour.vars.lang.VarString;

/**
 * A variable holding a password
 * 
 * @author Alexandre Dufour
 */
public class EzVarPassword extends EzVar<String>
{
    public EzVarPassword(String varName)
    {
        super(new VarString(varName, ""), new PasswordModel());
    }
}
