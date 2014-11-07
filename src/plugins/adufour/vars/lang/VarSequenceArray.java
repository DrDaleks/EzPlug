package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarSequenceArray extends VarGenericArray<Sequence[]>
{
    /**
     * @param name
     */
    public VarSequenceArray(String name)
    {
        this(name, (VarListener<Sequence[]>) null);
    }
    
    /**
     * @param name
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarSequenceArray(String name, VarListener<Sequence[]> defaultListener)
    {
        super(name, Sequence[].class, new Sequence[0], defaultListener);
    }
    
    public VarSequenceArray(String name, Sequence... sequences)
    {
        super(name, Sequence[].class, sequences != null ? sequences : new Sequence[0]);
    }
    
    @Override
    public VarEditor<Sequence[]> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createSequenceList(this);
    }
}
