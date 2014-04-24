package plugins.adufour.vars.gui.swing;

import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plugins.adufour.vars.gui.model.RangeModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

public class Spinner<N extends Number> extends SwingVarEditor<N>
{
    private static final int   MAX_SPINNER_WIDTH = 200;
    
    private MouseWheelListener mouseWheelListener;
    
    private ChangeListener     changeListener;
    
    public Spinner(Var<N> variable)
    {
        super(variable);
    }
    
    @Override
    public JComponent createEditorComponent()
    {
        VarEditorModel<N> model = variable.getDefaultEditorModel();
        
        if (model == null || !(model instanceof RangeModel))
        {
            throw new UnsupportedOperationException("Incorrect model for variable " + variable.getName() + ": " + model);
        }
        
        RangeModel<N> constraint = (RangeModel<N>) model;
        
        final JSpinner jSpinner = new JSpinner(new SpinnerNumberModel(constraint.getDefaultValue(), constraint.getMinimum(), constraint.getMaximum(), constraint.getStepSize()));
        
        jSpinner.setValue(constraint.getDefaultValue());
        
        changeListener = new ChangeListener()
        {
            @SuppressWarnings("unchecked")
            public void stateChanged(ChangeEvent e)
            {
                if (variable.getReference() == null) variable.setValue((N) jSpinner.getValue());
            }
        };
        
        mouseWheelListener = new MouseWheelListener()
        {
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                if (!jSpinner.isEnabled()) return;
                
                int clicks = Math.abs(e.getWheelRotation());
                
                boolean up = (e.getWheelRotation() < 0);
                Object newValue;
                
                for (int i = 0; i < clicks; i++)
                {
                    newValue = (up ? jSpinner.getNextValue() : jSpinner.getPreviousValue());
                    
                    if (newValue == null) break;
                    
                    jSpinner.setValue(newValue);
                }
            }
        };
        
        // Assign a maximum size to the spinner to avoid huge-ass interfaces
        Dimension dim = jSpinner.getPreferredSize();
        dim.setSize(Math.min(dim.width, MAX_SPINNER_WIDTH), dim.height);
        jSpinner.setPreferredSize(dim);
        
        return jSpinner;
    }
    
    public void setMaximum(Comparable<N> maxValue)
    {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setMaximum(maxValue);
        ((RangeModel<N>)variable.getDefaultEditorModel()).setMaximum(maxValue);
    }
    
    public void setMinimum(Comparable<N> minValue)
    {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setMinimum(minValue);
        ((RangeModel<N>)variable.getDefaultEditorModel()).setMinimum(minValue);
    }
    
    public void setStepSize(N stepSize)
    {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setStepSize(stepSize);
        ((RangeModel<N>)variable.getDefaultEditorModel()).setStepSize(stepSize);
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        N value = variable.getValue();
        getEditorComponent().setValue(value != null ? value : variable.getDefaultValue());
    }
    
    @Override
    public JSpinner getEditorComponent()
    {
        return (JSpinner) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        JSpinner jSpinner = getEditorComponent();
        jSpinner.addMouseWheelListener(mouseWheelListener);
        jSpinner.addChangeListener(changeListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        JSpinner jSpinner = getEditorComponent();
        jSpinner.removeMouseWheelListener(mouseWheelListener);
        jSpinner.removeChangeListener(changeListener);
    }
}
