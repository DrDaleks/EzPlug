package plugins.adufour.vars.gui.swing;

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

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
        @SuppressWarnings("serial")
        JSpinner jSpinner = new JSpinner(new SpinnerNumberModel()
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
    
    @SuppressWarnings("serial")
    protected AbstractFormatterFactory getFormatterFactory()
    {
        final DefaultFormatter editFormatter = new DefaultFormatter()
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
                
                try
                {
                    int value = toInteger(text);
                    if (value < -1) throw new ParseException(text, 0);
                    if (value == -1 && !allowAll) throw new ParseException(text, 0);
                    if (value >= sequence.getValue().getSize(dimension)) throw new ParseException(text, 0);
                    
                    return value;
                }
                catch (NumberFormatException e)
                {
                    throw new ParseException(text, 0);
                }
            }
        };
        
        return new DefaultFormatterFactory()
        {
            @Override
            public AbstractFormatter getDefaultFormatter()
            {
                return editFormatter;
            }
        };
    }
    
    @SuppressWarnings("static-method")
    protected String toUserFriendlyString(Integer value)
    {
        return value.toString();
    }
    
    @SuppressWarnings("static-method")
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
