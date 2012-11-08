package plugins.adufour.vars.gui.swing;

import icy.sequence.Sequence;

import java.awt.Dimension;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JLabel;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.lang.VarMutable;

/**
 * Special editor that changes type according to the underlying variable
 * 
 * @author Alexandre Dufour
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MutableVarEditor extends SwingVarEditor<Object>
{
    public MutableVarEditor(VarMutable variable)
    {
        super(variable);
    }
    
    /**
     * Abstract pointer to the active editor
     */
    private VarEditor      varEditor;
    
    /**
     * Editor used if the inner type of the variable is compatible with {@link Sequence}
     */
    private SequenceViewer sequenceViewer;
    
    /**
     * Editor used for any type that misses a dedicted editor
     */
    private Label          label;
    
    @Override
    protected JComponent createEditorComponent()
    {
        return getEditorComponent();
    }
    
    public JComponent getEditorComponent()
    {
        // deactivate the current editor (if any)
        if (varEditor != null) varEditor.setEnabled(false);
        
        
        if (variable.getType() != null && variable.getType().isAssignableFrom(Sequence.class))
        {
            if (sequenceViewer == null)
            {
                sequenceViewer = new SequenceViewer((VarMutable) variable);
            }
            varEditor = sequenceViewer;
        }
        else
        {
            if (label == null)
            {
                label = new Label(variable);
                label.getEditorComponent().setHorizontalAlignment(JLabel.CENTER);
            }
            varEditor = label;
        }
        
        // activate the new listener
        varEditor.setEnabled(true);
        
        return (JComponent) varEditor.getEditorComponent();
    };
    
    @Override
    public Dimension getPreferredSize()
    {
        return varEditor.getPreferredSize();
    }
    
    @Override
    public void setComponentToolTipText(String s)
    {
        varEditor.setComponentToolTipText(s);
    }
    
    public void setEnabled(boolean enabled)
    {
        // handle this ourselves
    }
    
    @Override
    protected void activateListeners()
    {
        
    }
    
    @Override
    protected void deactivateListeners()
    {
        
    }
    
    public boolean isComponentResizeable()
    {
        return true;
    }
    
    public double getComponentVerticalResizeFactor()
    {
        return 0.5;
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        try
        {
            Method m = varEditor.getClass().getMethod("updateInterfaceValue");
            m.setAccessible(true);
            m.invoke(varEditor);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
