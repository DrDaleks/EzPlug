package plugins.adufour.vars.gui.model;

import java.util.HashMap;

/**
 * Generic model used to design graphical editors for numeric variables
 * 
 * @author Alexandre Dufour
 * @param <N>
 * @see IntegerRangeModel
 * @see DoubleRangeModel
 * @see FloatRangeModel
 */
public abstract class RangeModel<N extends Number> implements VarEditorModel<N>
{
    /**
     * default editor is a spinner, but can be changed (see {@link RangeEditorType})
     */
    private RangeEditorType editorType = RangeEditorType.SPINNER;
    
    public enum RangeEditorType
    {
        /**
         * A spinner that allows manual value editing, and browsing through the values with small
         * arrows on the side or with the mouse wheel. This is the default component
         */
        SPINNER,
        /**
         * A slider with tick marks and a label on each side
         */
        SLIDER
    }
    
    protected N                  defaultValue, step;
    protected Comparable<N>      min, max;
    
    protected HashMap<N, String> labels;
    
    public RangeModel(N defaultValue, Comparable<N> min, Comparable<N> max, N step)
    {
        this(defaultValue, min, max, step, RangeEditorType.SPINNER, null);
    }
    
    public RangeModel(N defaultValue, Comparable<N> min, Comparable<N> max, N step, RangeEditorType editorType, HashMap<N, String> labels)
    {
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.step = step;
        setEditorType(editorType);
        this.labels = labels;
    }
    
    @Override
    public N getDefaultValue()
    {
        return defaultValue;
    }
    
    public RangeEditorType getEditorType()
    {
        return editorType;
    }
    
    public HashMap<N, String> getLabels()
    {
        return labels;
    }
    
    /**
     * @return the number of values covered by this range, i.e., <code>(max - min) / step</code>
     */
    public abstract int getRangeSize();
    
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
    
    /**
     * @param index
     * @return the value corresponding to the specified zero-based index, i.e.,
     *         <code>min + index * step</code>
     */
    public abstract N getValueForIndex(int index);
    
    /**
     * Calculates the index of the specified value within the range, according to the current
     * minimum value and step
     * 
     * @param value
     * @return
     */
    public abstract int indexOf(N value);
    
    public void setMaximum(Comparable<N> maximum)
    {
        this.max = maximum;
    }
    
    public void setEditorType(RangeEditorType editorType)
    {
        this.editorType = editorType;
    }
    
    public void setMinimum(Comparable<N> minimum)
    {
        this.min = minimum;
    }
    
    public void setStepSize(N step)
    {
        this.step = step;
    }
}