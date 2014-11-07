package plugins.adufour.vars.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import plugins.adufour.vars.lang.Var;

public class CheckBox extends SwingVarEditor<Boolean>
{
    private ActionListener actionListener;
    
    public CheckBox(Var<Boolean> variable)
    {
        super(variable);
    }
    
    @Override
    public JComponent createEditorComponent()
    {
        // Legacy note: by default, "nameVisible" is "true", but this component was not using it
        // until now, therefore set it to false here to preserve retro-compatibility
        setNameVisible(false);
        
        final JCheckBox jCheckBox = new JCheckBox(isNameVisible() ? variable.getName() : " ");
        
        actionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                getVariable().setValue(jCheckBox.isSelected());
            }
        };
        
        return jCheckBox;
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        Boolean b = variable.getValue();
        
        getEditorComponent().setSelected(b != null && b);
    }
    
    @Override
    public JCheckBox getEditorComponent()
    {
        return (JCheckBox) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        getEditorComponent().addActionListener(actionListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        getEditorComponent().removeActionListener(actionListener);
    }
}
