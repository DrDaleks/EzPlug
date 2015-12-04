package plugins.adufour.vars.gui.swing;

import java.text.ParseException;

import icy.sequence.DimensionId;
import icy.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarChannel;

public class ChannelSelector extends SequenceDimensionSelector
{
    public ChannelSelector(VarChannel variable, Var<Sequence> sequence, boolean allowAllChannels)
    {
        super(variable, sequence, DimensionId.C, allowAllChannels);
    }
    
    @Override
    protected String toUserFriendlyString(Integer value)
    {
        return value.toString() + " (" + sequence.getValue().getChannelName(value) + ")";
    }
    
    @Override
    protected Integer toInteger(String text) throws ParseException
    {
        Sequence s = sequence.getValue();
        
        text = text.substring(text.indexOf("(") + 1);
        text = text.substring(0, text.length() - 1);
        for (int c = 0; c < s.getSizeC(); c++)
            if (s.getChannelName(c).equalsIgnoreCase(text)) return c;
            
        throw new ParseException("Channel " + text + " does not exist", 0);
    }
    
}
