package plugins.adufour.vars.gui.swing;

import icy.sequence.DimensionId;
import icy.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarSlice;

public class SliceSelector extends SequenceDimensionSelector
{
    public SliceSelector(VarSlice variable, Var<Sequence> sequence, boolean allowAllChannels)
    {
        super(variable, sequence, DimensionId.Z, allowAllChannels);
    }
}
