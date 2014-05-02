package plugins.adufour.ezplug;

import icy.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarChannel;

public class EzVarChannel extends EzVar<Integer>
{
    public EzVarChannel(String name, Var<Sequence> sequence, boolean allowAllChannels)
    {
        super(new VarChannel(name, sequence, allowAllChannels), null);
    }
}
