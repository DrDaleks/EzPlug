package plugins.adufour.vars.gui.swing;

import java.text.ParseException;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;

import icy.sequence.DimensionId;
import icy.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.util.VarListener;

public abstract class SequenceDimensionSelector extends Spinner<Integer>
{
    protected Var<Sequence> sequence;
    
    protected DimensionId dimension;
    
    protected boolean allowAll;
    
    private final VarListener<Sequence> sequenceListener = new VarListener<Sequence>()
    {
        @Override
        public void valueChanged(Var<Sequence> source, Sequence oldValue, Sequence newValue)
        {
            updateInterfaceValue();
        }
        
        @Override
        public void referenceChanged(Var<Sequence> source, Var<? extends Sequence> oldReference, Var<? extends Sequence> newReference)
        {
            updateInterfaceValue();
        }
    };
    
    public SequenceDimensionSelector(Var<Integer> variable, Var<Sequence> sequence, DimensionId dim, boolean allowAll)
    {
        super(variable);
        this.sequence = sequence;
        this.dimension = dim;
        this.allowAll = allowAll;
    }
    
    /**
     * Creates a graphical component able to receive user input to change the variable value.<br>
     * Note that listeners are <b>NOT</b> registered here. This operation should be done when (and
     * if) the component is to be used (to avoid memory leaks due to improper cleaning)
     */
    @Override
    public JSpinner createEditorComponent()
    {
        JSpinner jSpinner = new JSpinner(new AbstractSpinnerModel()
        {
            @Override
            public void setValue(Object channel)
            {
                variable.setValue((Integer) channel);
            }
            
            @Override
            public Integer getValue()
            {
                return variable.getValue();
            }
            
            @Override
            public Integer getPreviousValue()
            {
                int minValue = allowAll ? -1 : 0;
                
                return Math.max(minValue, variable.getValue() - 1);
            }
            
            @Override
            public Object getNextValue()
            {
                int maxValue = 65535;
                
                if (sequence != null && sequence.getValue() != null) maxValue = sequence.getValue().getSize(dimension) - 1;
                
                return Math.min(maxValue, variable.getValue() + 1);
            }
        });
        
        JFormattedTextField ftf = ((JSpinner.DefaultEditor) jSpinner.getEditor()).getTextField();
        
        ftf.setFormatterFactory(getFormatterFactory());
        
        return jSpinner;
    }
    
    protected AbstractFormatterFactory getFormatterFactory()
    {
        return new AbstractFormatterFactory()
        {
            @Override
            public AbstractFormatter getFormatter(JFormattedTextField arg0)
            {
                return SequenceDimensionSelector.this.getFormatter();
            }
        };
    }
    
    @SuppressWarnings("serial")
    protected AbstractFormatter getFormatter()
    {
        return new AbstractFormatter()
        {
            @Override
            public String valueToString(Object value) throws ParseException
            {
                if (value == null) return "";
                
                if (value.equals(-1)) return "ALL";
                
                if (sequence == null || sequence.getValue() == null) return value.toString();
                
                return toUserFriendlyString((Integer) value);
            }
            
            @Override
            public Object stringToValue(String text) throws ParseException
            {
                if (text.equalsIgnoreCase("ALL")) return -1;
                
                if (sequence == null || sequence.getValue() == null) return Integer.parseInt(text);
                
                return toInteger(text);
            }
        };
    }
    
    protected String toUserFriendlyString(Integer value)
    {
        return value.toString();
    }
    
    protected Integer toInteger(String text) throws ParseException
    {
        return Integer.parseInt(text);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        JFormattedTextField ftf = ((JSpinner.DefaultEditor) getEditorComponent().getEditor()).getTextField();
        
        ftf.setValue(variable.getValue());
        ftf.setEditable(sequence == null || sequence.getValue() == null);
    }
    
    @Override
    protected void activateListeners()
    {
        super.activateListeners();
        sequence.addListener(sequenceListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        sequence.removeListener(sequenceListener);
        super.deactivateListeners();
    }
}
