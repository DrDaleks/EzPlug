package plugins.adufour.vars.gui.model;

/**
 * Generic model used to design graphical editors for numeric variables
 * 
 * @author Alexandre Dufour
 * 
 * @param <N>
 * @see IntegerRangeModel
 * @see DoubleRangeModel
 * @see FloatRangeModel
 */
public abstract class RangeModel<N extends Number> implements VarEditorModel<N>
{
    protected N defaultValue, step;
    protected Comparable<N> min, max;

    public RangeModel(N defaultValue, Comparable<N> min, Comparable<N> max, N step)
    {
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public N getDefaultValue()
    {
        return defaultValue;
    }

    public Comparable<N> getMaximum()
    {
        return max;
    }

    public Comparable<N> getMinimum()
    {
        return min;
    }

    public N getStepSize()
    {
        return step;
    }
}