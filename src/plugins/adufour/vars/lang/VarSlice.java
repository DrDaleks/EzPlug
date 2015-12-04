package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

/**
 * Special variable storing an integer representing a slice number in a sequence 
 * 
 * @author Alexandre Dufour
 *
 */
public class VarSlice extends VarInteger
{
    private boolean       allowAllSlices;
    
    private Var<Sequence> sequence;
    
    /**
     * @param name
     * @param sequence
     * @param allowAllChannels
     */
    public VarSlice(String name, Var<Sequence> sequence, boolean allowAllSlices)
    {
        this(name, sequence, allowAllSlices, null);
    }
    
    /**
     * @param name
     * @param sequence
     * @param allowAllSlices
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarSlice(String name, Var<Sequence> sequence, boolean allowAllSlices, VarListener<Integer> defaultListener)
    {
        super(name, allowAllSlices ? -1 : 0, defaultListener);
        
        this.sequence = sequence;
        this.allowAllSlices = allowAllSlices;
    }
    
    @Override
    public VarEditor<Integer> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createSliceSelector(this, sequence, allowAllSlices);
    }
}
