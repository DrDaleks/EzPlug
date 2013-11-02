package plugins.adufour.vars.gui.swing;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.lang.Var;

/**
 * Swing implementation of the VarEditor
 * 
 * @author Alexandre Dufour
 * @param <V>
 *            the type of variable to edit, see {@link Var}
 */
public abstract class SwingVarEditor<V> extends VarEditor<V>
{
    public SwingVarEditor(Var<V> variable)
    {
        super(variable);
    }
    
    @Override
    protected abstract JComponent createEditorComponent();
    
    @Override
    public JComponent getEditorComponent()
    {
        return (JComponent) super.getEditorComponent();
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return getEditorComponent().getPreferredSize();
    }
    
    @Override
    public boolean isComponentEnabled()
    {
        return getEditorComponent() instanceof JLabel || variable.getReference() == null;
    }
    
    @Override
    public boolean isComponentOpaque()
    {
        return getEditorComponent() instanceof JTextComponent;
    }
    
    @Override
    protected void setEditorEnabled(boolean enabled)
    {
        getEditorComponent().setEnabled(enabled);
    }
    
    @Override
    public void setComponentToolTipText(String s)
    {
        getEditorComponent().setToolTipText(s);
    }
}
