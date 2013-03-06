package plugins.adufour.vars.lang;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.Button;

/**
 * Variable providing a trigger (i.e. a button in the graphical interface) and the number of times
 * it was triggered
 * 
 * @author Alexandre Dufour
 * 
 */
public class VarTrigger extends VarInteger
{
    /**
     * Creates a new trigger with the given name
     * 
     * @param name
     *            the name of the trigger (will be the title of the button in graphical mode)
     */
    public VarTrigger(String name)
    {
        super(name, 0);
    }
    
    @Override
    public VarEditor<Integer> createVarEditor()
    {
        return new Button(this);
    }
    
    /**
     * Resets the trigger count (equivalent to setting the value of this variable to 0)
     */
    public void reset()
    {
        setValue(0);
    }
    
    /**
     * Triggers the variable (equivalent to incrementing the value of this variable)
     */
    public void trigger()
    {
        super.setValue(getValue() + 1);
    }
}
