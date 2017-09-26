package plugins.adufour.vars.gui.swing;

import javax.swing.JComponent;
import javax.swing.JPasswordField;

import plugins.adufour.vars.lang.Var;

public class PasswordField extends TextField<String>
{
    public PasswordField(Var<String> variable)
    {
        super(variable);
    }
    
    @Override
    public JComponent createEditorComponent()
    {
        final JPasswordField jPassField = new JPasswordField();
        
        super.initializeComponent(jPassField);
        
        return jPassField;
    }
}