package plugins.adufour.ezplug;

import icy.sequence.Sequence;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarFrame;
import plugins.adufour.vars.util.VarListener;

/**
 * Graphical component letting the user select a frame number (T dimension) from a sequence
 * 
 * @author Alexandre Dufour
 */
public class EzVarFrame extends EzVar<Integer>
{
    private final VarListener<Sequence> listener = new VarListener<Sequence>()
    {
        @Override
        public void valueChanged(Var<Sequence> source, Sequence oldValue, Sequence newValue)
        {
            if (newValue == null)
            {
                setVisible(false);
            }
            else
            {
                int sizeT = newValue.getSizeT();
                if (sizeT == 1)
                {
                    setVisible(false);
                    if (variable.getReference() == null) setValue(0);
                }
                else
                {
                    setVisible(true);
                }
            }
        }
        
        @Override
        public void referenceChanged(Var<Sequence> source, Var<? extends Sequence> oldReference, Var<? extends Sequence> newReference)
        {
            // shoudn't happen
        }
    };
    
    private final Var<Sequence> sequence;
    
    public EzVarFrame(String name, Var<Sequence> sequence, boolean allowAllFrames)
    {
        super(new VarFrame(name, sequence, allowAllFrames), null);
        
        this.sequence = sequence;
        sequence.addListener(listener);
        
        // trigger the listener once
        listener.valueChanged(sequence, sequence.getValue(), sequence.getValue());
    }
    
    @Override
    protected void dispose()
    {
        sequence.removeListener(listener);
        super.dispose();
    }
}
