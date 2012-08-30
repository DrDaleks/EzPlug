package plugins.adufour.ezplug;

import icy.sequence.DimensionId;
import icy.sequence.Sequence;

public class EzVarDimensionPicker extends EzVarInteger implements EzVarListener<Sequence>
{
    final EzVarSequence s;
    final DimensionId   dim;

    public EzVarDimensionPicker(String varName, DimensionId dim, EzVarSequence sequence)
    {
        super(varName, 0, 0, 1);
        s = sequence;
        this.dim = dim;
        s.addVarChangeListener(this);
    }

    @Override
    public void variableChanged(EzVar<Sequence> source, Sequence newValue)
    {
        setValue(0);

        if (newValue == null)
        {
            setVisible(false);
        }
        else
        {
            int size = getSize(newValue, dim);
            setVisible(size > 1);
            setMaxValue(size - 1);
        }
    }

    private int getSize(Sequence s, DimensionId dim)
    {
        switch (dim)
        {
        case X:
            return s.getSizeX();
        case Y:
            return s.getSizeY();
        case Z:
            return s.getSizeZ();
        case T:
            return s.getSizeT();
        case C:
            return s.getSizeC();
        default:
            throw new IllegalArgumentException("Unknown sequence dimension: " + dim);
        }
    }

    @Override
    protected void dispose()
    {
        s.removeVarChangeListener(this);
        super.dispose();
    }
}
