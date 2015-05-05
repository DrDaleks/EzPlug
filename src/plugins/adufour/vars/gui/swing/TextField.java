package plugins.adufour.vars.gui.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import plugins.adufour.vars.lang.Var;

public class TextField<T> extends SwingVarEditor<T>
{
    protected ActionListener actionListener;
    
    protected FocusListener  focusListener;
    
    protected KeyListener    keyListener;
    
    public TextField(Var<T> variable)
    {
        super(variable);
    }
    
    @Override
    public JComponent createEditorComponent()
    {
        final JTextField jTextField = new JTextField();
        
        initializeComponent(jTextField);
        
        return jTextField;
    }
    
    protected void initializeComponent(final JTextField jTextField)
    {
        jTextField.setText(getVariable().getValueAsString());
        actionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setVariableValue(jTextField);
            }
        };
        
        focusListener = new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                setVariableValue(jTextField);
            }
        };
        
        keyListener = new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
            }
            
            @Override
            public void keyReleased(KeyEvent e)
            {
                try
                {
                    variable.parse(jTextField.getText());
                    jTextField.setForeground(Color.black);
                }
                catch (Exception ex)
                {
                    jTextField.setForeground(Color.red);
                }
            }
            
            @Override
            public void keyPressed(KeyEvent e)
            {
            }
        };
    }
    
    protected void setVariableValue(JTextField jTextField)
    {
        try
        {
            if (variable.getReference() == null) variable.setValue(variable.parse(jTextField.getText()));
            jTextField.setForeground(Color.black);
            jTextField.setToolTipText(null);
        }
        catch (RuntimeException e)
        {
            jTextField.setForeground(Color.red);
            jTextField.setToolTipText("Cannot convert input into a " + getVariable().getTypeAsString());
        }
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        getEditorComponent().setText(variable.getValueAsString(true));
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
    }
    
    @Override
    public JTextField getEditorComponent()
    {
        return (JTextField) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        getEditorComponent().addActionListener(actionListener);
        getEditorComponent().addFocusListener(focusListener);
        getEditorComponent().addKeyListener(keyListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        getEditorComponent().removeActionListener(actionListener);
        getEditorComponent().removeFocusListener(focusListener);
        getEditorComponent().removeKeyListener(keyListener);
    }
}
