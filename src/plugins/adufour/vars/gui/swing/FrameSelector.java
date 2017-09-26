package plugins.adufour.vars.gui.swing;

import icy.sequence.DimensionId;
import icy.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarFrame;

public class FrameSelector extends SequenceDimensionSelector
{
    public FrameSelector(VarFrame variable, Var<Sequence> sequence, boolean allowAllChannels)
    {
        super(variable, sequence, DimensionId.T, allowAllChannels);
    }
}
