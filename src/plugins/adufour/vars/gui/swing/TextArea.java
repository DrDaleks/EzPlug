package plugins.adufour.vars.gui.swing;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTextArea;

import plugins.adufour.vars.lang.Var;

public class TextArea<T> extends SwingVarEditor<T>
{
    private FocusListener focusListener;
    
    private KeyListener   keyListener;
    
    public TextArea(Var<T> variable, int rows)
    {
        super(variable);
        getTextArea().setRows(rows);
    }
    
    @Override
    public JComponent createEditorComponent()
    {
        final JXTextArea jTextArea = new JXTextArea();
        jTextArea.setText(getVariable().getValueAsString());
        
        focusListener = new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                setVariableValue(jTextArea);
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
                    variable.parse(jTextArea.getText());
                    jTextArea.setForeground(Color.black);
                }
                catch (Exception ex)
                {
                    jTextArea.setForeground(Color.red);
                }
            }
            
            @Override
            public void keyPressed(KeyEvent e)
            {
            }
        };
        
        JScrollPane scroll = new JScrollPane(jTextArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setAutoscrolls(true);
        
        // return jTextArea;
        return scroll;
    }
    
    private void setVariableValue(JXTextArea jTextArea)
    {
        try
        {
            if (variable.getReference() == null) variable.setValue(variable.parse(jTextArea.getText()));
            jTextArea.setForeground(Color.black);
            jTextArea.setToolTipText(null);
        }
        catch (RuntimeException e)
        {
            jTextArea.setForeground(Color.red);
            jTextArea.setToolTipText("Cannot convert input into a " + getVariable().getTypeAsString());
        }
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        getTextArea().setText(variable.getValueAsString(true));
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
    }
    
    @Override
    public JScrollPane getEditorComponent()
    {
        return (JScrollPane) super.getEditorComponent();
    }
    
    public JXTextArea getTextArea()
    {
        return (JXTextArea) getEditorComponent().getViewport().getView();
    }
    
    @Override
    protected void activateListeners()
    {
        getTextArea().addFocusListener(focusListener);
        getTextArea().addKeyListener(keyListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        getTextArea().removeFocusListener(focusListener);
        getTextArea().removeKeyListener(keyListener);
    }
}
