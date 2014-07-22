package plugins.adufour.vars.gui.swing;

import icy.sequence.Sequence;

import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.util.VarListener;

public class ChannelSelector extends Spinner<Integer>
{
    private Var<Sequence>               sequence;
    
    private boolean                     allowAllChannels;
    
    private final VarListener<Sequence> sequenceListener = new VarListener<Sequence>()
                                                         {
                                                             @Override
                                                             public void valueChanged(Var<Sequence> source, Sequence oldValue, Sequence newValue)
                                                             {
                                                                 updateInterfaceValue();
                                                             }
                                                             
                                                             @Override
                                                             public void referenceChanged(Var<Sequence> source, Var<? extends Sequence> oldReference,
                                                                     Var<? extends Sequence> newReference)
                                                             {
                                                                 updateInterfaceValue();
                                                             }
                                                         };
    
    public ChannelSelector(Var<Integer> variable, Var<Sequence> sequence, boolean allowAllChannels)
    {
        super(variable);
        this.sequence = sequence;
        this.allowAllChannels = allowAllChannels;
    }
    
    /**
     * Creates a graphical component able to receive user input to change the variable value.<br>
     * Note that listeners are <b>NOT</b> registered here. This operation should be done when (and
     * if) the component is to be used (to avoid memory leaks due to improper cleaning)
     */
    @Override
    public JComponent createEditorComponent()
    {
        @SuppressWarnings("serial")
        final SpinnerModel spinnerModel = new SpinnerNumberModel(0, allowAllChannels ? -1 : 0, 65535, 1)
        {
            @Override
            public Object getNextValue()
            {
                if (sequence == null || sequence.getValue() == null) return super.getNextValue();
                
                return Math.min(sequence.getValue().getSizeC() - 1, variable.getValue() + 1);
            }
            
            @Override
            public void setValue(Object newValue)
            {
                super.setValue(newValue);
                if (variable.getReference() == null) variable.setValue((Integer) newValue);
            }
        };
        
        JSpinner jSpinnerChannels = new JSpinner(spinnerModel);
        
        JFormattedTextField ftf = ((JSpinner.NumberEditor) jSpinnerChannels.getEditor()).getTextField();
        
        ftf.setFormatterFactory(new AbstractFormatterFactory()
        {
            @SuppressWarnings("serial")
            @Override
            public AbstractFormatter getFormatter(JFormattedTextField arg0)
            {
                return new AbstractFormatter()
                {
                    @Override
                    public String valueToString(Object channelValue) throws ParseException
                    {
                        if (channelValue == null) return "";
                        
                        if (sequence == null || sequence.getValue() == null) return channelValue.toString();
                        
                        return sequence.getValue().getChannelName((Integer) channelValue);
                    }
                    
                    @Override
                    public Object stringToValue(String channelName) throws ParseException
                    {
                        if (sequence == null || sequence.getValue() == null) return Integer.parseInt(channelName);
                        
                        Sequence s = sequence.getValue();
                        
                        for (int c = 0; c < s.getSizeC(); c++)
                            if (s.getChannelName(c).equalsIgnoreCase(channelName)) return c;
                        
                        throw new ParseException("Channel " + channelName + " does not exist", 0);
                    }
                };
            }
        });
        
        return jSpinnerChannels;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        super.updateInterfaceValue();
        
        JFormattedTextField ftf = ((JSpinner.NumberEditor) getEditorComponent().getEditor()).getTextField();
        
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
