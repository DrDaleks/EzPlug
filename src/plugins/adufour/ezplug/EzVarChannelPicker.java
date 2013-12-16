package plugins.adufour.ezplug;

import icy.sequence.DimensionId;
import icy.sequence.Sequence;
import plugins.adufour.vars.lang.Var;

public class EzVarChannelPicker extends EzVarDimensionPicker
{
    public EzVarChannelPicker(String name, Var<Sequence> sequence, boolean allowAllChannels)
    {
        super(name, DimensionId.C, sequence, allowAllChannels);
    }
    
    
}
