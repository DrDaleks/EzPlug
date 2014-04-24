package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;

public class VarSequenceArray extends VarGenericArray<Sequence[]>
{
    public VarSequenceArray(String name)
    {
        super(name, Sequence[].class, new Sequence[] {});
    }
    
    public VarSequenceArray(String name, Sequence... sequences)
    {
        super(name, Sequence[].class, sequences != null ? sequences : new Sequence[] {});
    }
    
    /**
     * @deprecated use other constructors instead
     * @param name
     * @param type
     * @param defaultValue
     */
    public VarSequenceArray(String name, Class<Sequence[]> type, Sequence[] defaultValue)
    {
        this(name, defaultValue);
    }
    
    @Override
    public VarEditor<Sequence[]> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createSequenceList(this);
    }
}
