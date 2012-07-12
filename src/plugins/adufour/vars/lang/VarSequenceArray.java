package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.SequenceList;

public class VarSequenceArray extends VarGenericArray<Sequence[]>
{
    public VarSequenceArray(String name, Class<Sequence[]> type, Sequence[] defaultValue)
    {
        super(name, type, defaultValue);
    }
    
    @Override
    public VarEditor<Sequence[]> createVarEditor()
    {
        return new SequenceList(this);
    }
}
