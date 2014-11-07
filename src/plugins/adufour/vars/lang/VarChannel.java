package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarChannel extends VarInteger
{
    private boolean       allowAllChannels;
    
    private Var<Sequence> sequence;
    
    /**
     * @param name
     * @param sequence
     * @param allowAllChannels
     */
    public VarChannel(String name, Var<Sequence> sequence, boolean allowAllChannels)
    {
        this(name, sequence, allowAllChannels, null);
    }
    
    /**
     * @param name
     * @param sequence
     * @param allowAllChannels
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarChannel(String name, Var<Sequence> sequence, boolean allowAllChannels, VarListener<Integer> defaultListener)
    {
        super(name, allowAllChannels ? -1 : 0, defaultListener);
        
        this.sequence = sequence;
        this.allowAllChannels = allowAllChannels;
    }
    
    @Override
    public VarEditor<Integer> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createChannelSelector(this, sequence, allowAllChannels);
    }
}
