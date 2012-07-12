package plugins.adufour.vars.gui.swing;

import java.awt.Dimension;

import javax.swing.JComponent;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.lang.Var;

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
