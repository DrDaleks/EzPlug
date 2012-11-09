package plugins.adufour.vars.gui.swing;

import icy.gui.component.sequence.SequencePreviewPanel;
import icy.main.Icy;
import icy.sequence.Sequence;
import icy.sequence.SequenceEvent;
import icy.sequence.SequenceListener;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarMutable;

public class SequenceViewer extends SwingVarEditor<Sequence> implements SequenceListener
{
    public SequenceViewer(Var<Sequence> variable)
    {
        super(variable);
    }
    
    /**
     * Constructs a new SequenceViewer using a mutable variable of type sequence.
     * 
     * @param variable
     *            the mutable variable containing the {@link Sequence} to display
     * @throws ClassCastException
     *             if the given variable is not of type {@link Sequence}
     * @see Var#getType()
     */
    @SuppressWarnings("unchecked")
    public SequenceViewer(final VarMutable variable)
    {
        super(variable);
        if (!variable.getType().isAssignableFrom(Sequence.class)) throw new ClassCastException("Variable " + variable.getName() + " is not a Sequence");
    }
    
    @Override
    protected JComponent createEditorComponent()
    {
        SequencePreviewPanel editor = new SequencePreviewPanel(true);
        
        // make every component opaque
        for (JPanel panel : new JPanel[] { editor.getMainPanel(), editor.getTPanel(), editor.getZPanel() })
        {
            editor.setOpaque(false);
            panel.setOpaque(false);
            for (Component c : panel.getComponents())
                if (c instanceof JComponent) ((JComponent) c).setOpaque(false);
        }
        
        return editor;
    }
    
    private final MouseAdapter mouseAdapter = new MouseAdapter()
                                            {
                                                @Override
                                                public void mouseClicked(MouseEvent e)
                                                {
                                                    if (e.getClickCount() == 2)
                                                    {
                                                        Sequence s = getVariable().getValue();
                                                        if (s != null) Icy.getMainInterface().addSequence(s);
                                                    }
                                                }
                                            };
    
    @Override
    protected void activateListeners()
    {
        getEditorComponent().addMouseListener(mouseAdapter);
    }
    
    @Override
    protected void deactivateListeners()
    {
        getEditorComponent().removeMouseListener(mouseAdapter);
    }
    
    @Override
    public SequencePreviewPanel getEditorComponent()
    {
        return (SequencePreviewPanel) super.getEditorComponent();
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        Sequence s = variable.getValue();
        getEditorComponent().setModel(s);
    }
    
    @Override
    public boolean isComponentResizeable()
    {
        return true;
    }
    
    @Override
    public double getComponentVerticalResizeFactor()
    {
        return 1.0;
    }
    
    @Override
    public void valueChanged(Var<Sequence> source, Sequence oldValue, Sequence newValue)
    {
        if (oldValue != null) oldValue.removeListener(this);
        if (newValue != null) newValue.addListener(this);
        super.valueChanged(source, oldValue, newValue);
    }
    
    @Override
    public void referenceChanged(Var<Sequence> source, Var<? extends Sequence> oldReference, Var<? extends Sequence> newReference)
    {
        if (oldReference != null && oldReference.getValue() != null) oldReference.getValue().removeListener(this);
        super.referenceChanged(source, oldReference, newReference);
    }
    
    @Override
    public void sequenceClosed(Sequence sequence)
    {
    }
    
    @Override
    public void sequenceChanged(SequenceEvent sequenceEvent)
    {
        getEditorComponent().dimensionChanged();
        getEditorComponent().imageChanged();
    }
}
