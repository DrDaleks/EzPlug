package plugins.adufour.vars.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import plugins.adufour.vars.lang.VarTrigger;

public class Button extends SwingVarEditor<Integer>
{
    private ActionListener listener;
    
    public Button(VarTrigger variable)
    {
        super(variable);
        
        setNameVisible(false);
    }
    
    @Override
    protected JButton createEditorComponent()
    {
        if (getEditorComponent() != null) deactivateListeners();
        
        listener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                variable.setValue(variable.getValue() + 1);
            }
        };
        
        return new JButton(variable.getName());
    }
    
    @Override
    public JButton getEditorComponent()
    {
        return (JButton) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        getEditorComponent().addActionListener(listener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        getEditorComponent().removeActionListener(listener);
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        // nothing to do (it's just a button with a name)
    }
    
}
