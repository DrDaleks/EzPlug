package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.ChannelSelector;

public class VarChannel extends Var<Integer>
{
    private boolean       allowAllChannels;
    
    private Var<Sequence> sequence;
    
    public VarChannel(String name, Var<Sequence> sequence, boolean allowAllChannels)
    {
        super(name, allowAllChannels ? -1 : 0);
        
        this.sequence = sequence;
        this.allowAllChannels = allowAllChannels;
    }
    
    @Override
    public VarEditor<Integer> createVarEditor()
    {
        return new ChannelSelector(this, sequence, allowAllChannels);
    }
}
