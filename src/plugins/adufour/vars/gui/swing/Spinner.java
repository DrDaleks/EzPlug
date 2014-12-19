package plugins.adufour.vars.gui.swing;

import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

import plugins.adufour.vars.gui.model.RangeModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

public class Spinner<N extends Number> extends SwingVarEditor<N>
{
    private static final int         MAX_SPINNER_WIDTH  = 100;
    
    private final MouseWheelListener mouseWheelListener = new MouseWheelListener()
                                                        {
                                                            public void mouseWheelMoved(MouseWheelEvent e)
                                                            {
                                                                JSpinner spinner = getEditorComponent();
                                                                
                                                                if (!spinner.isEnabled()) return;
                                                                
                                                                int clicks = Math.abs(e.getWheelRotation());
                                                                
                                                                boolean up = (e.getWheelRotation() < 0);
                                                                Object newValue;
                                                                
                                                                for (int i = 0; i < clicks; i++)
                                                                {
                                                                    newValue = (up ? spinner.getNextValue() : spinner.getPreviousValue());
                                                                    
                                                                    if (newValue == null) break;
                                                                    
                                                                    spinner.setValue(newValue);
                                                                }
                                                            }
                                                        };
    
    private NumberFormatter          formatter;
    
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
        
        @SuppressWarnings("serial")
        final JSpinner jSpinner = new JSpinner(new SpinnerNumberModel(constraint.getDefaultValue(), constraint.getMinimum(), constraint.getMaximum(), constraint.getStepSize())
        {
            @SuppressWarnings("unchecked")
            @Override
            public void setValue(Object value)
            {
                super.setValue(value);
                if (variable.getReference() == null) variable.setValue((N) value);
            }
        });
        
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) jSpinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) editor.getComponent(0);
        formatter = (NumberFormatter) field.getFormatter();
        
        return jSpinner;
    }
    
    public void setMaximum(Comparable<N> maxValue)
    {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setMaximum(maxValue);
        ((RangeModel<N>) variable.getDefaultEditorModel()).setMaximum(maxValue);
    }
    
    public void setMinimum(Comparable<N> minValue)
    {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setMinimum(minValue);
        ((RangeModel<N>) variable.getDefaultEditorModel()).setMinimum(minValue);
    }
    
    public void setStepSize(N stepSize)
    {
        ((SpinnerNumberModel) getEditorComponent().getModel()).setStepSize(stepSize);
        ((RangeModel<N>) variable.getDefaultEditorModel()).setStepSize(stepSize);
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        N value = variable.getValue();
        
        // adjust the text format (if not in scientific notation)
        if (!value.toString().contains("E"))
        {
            if (formatter != null && formatter.getFormat() != null)
            {
                ((DecimalFormat) formatter.getFormat()).applyPattern(value.toString().replace("-", "").replaceAll("[0-9]", "#"));
            }
        }
        
        getEditorComponent().setValue(value);
    }
    
    @Override
    public JSpinner getEditorComponent()
    {
        return (JSpinner) super.getEditorComponent();
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        Dimension dim = super.getPreferredSize();
        dim.width = Math.min(MAX_SPINNER_WIDTH, dim.width);
        return dim;
    }
    
    @Override
    protected void activateListeners()
    {
        JSpinner jSpinner = getEditorComponent();
        jSpinner.addMouseWheelListener(mouseWheelListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        JSpinner jSpinner = getEditorComponent();
        jSpinner.removeMouseWheelListener(mouseWheelListener);
    }
}
