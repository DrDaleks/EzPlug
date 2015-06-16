package plugins.adufour.vars.gui.swing;

import icy.sequence.Sequence;

import java.text.ParseException;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JSpinner;

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
        JSpinner jSpinnerChannels = new JSpinner(new AbstractSpinnerModel()
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
                int minValue = allowAllChannels ? -1 : 0;
                
                return Math.max(minValue, variable.getValue() - 1);
            }
            
            @Override
            public Object getNextValue()
            {
                int maxValue = 65535;
                
                if (sequence != null && sequence.getValue() != null) maxValue = sequence.getValue().getSizeC() - 1;
                
                return Math.min(maxValue, variable.getValue() + 1);
            }
        });
        
        JFormattedTextField ftf = ((JSpinner.DefaultEditor) jSpinnerChannels.getEditor()).getTextField();
        
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
                        
                        if (channelValue.equals(-1)) return "ALL";
                        
                        if (sequence == null || sequence.getValue() == null) return channelValue.toString();
                        
                        return channelValue.toString() + " (" + sequence.getValue().getChannelName((Integer) channelValue) + ")";
                    }
                    
                    @Override
                    public Object stringToValue(String channelName) throws ParseException
                    {
                        if (channelName.equalsIgnoreCase("ALL")) return -1;
                        
                        if (sequence == null || sequence.getValue() == null) return Integer.parseInt(channelName);
                        
                        Sequence s = sequence.getValue();
                        
                        channelName = channelName.substring(channelName.indexOf("(") + 1);
                        channelName = channelName.substring(0, channelName.length() - 1);
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
