package plugins.adufour.vars.gui.model;

import java.util.HashMap;

public class DoubleRangeModel extends RangeModel<Double>
{
    public DoubleRangeModel(Double defaultValue, Double min, Double max, Double step)
    {
        this(defaultValue, min, max, step, RangeEditorType.SPINNER, null);
    }
    
    public DoubleRangeModel(Double defaultValue, Double min, Double max, Double step, RangeEditorType editorType, HashMap<Double, String> labels)
    {
        super(defaultValue, min, max, step, editorType, labels);
    }
    
    @Override
    public boolean isValid(Double value)
    {
        return min.compareTo(value) <= 0 && max.compareTo(value) >= 0 && value % step == 0;
    }
    
    @Override
    public int getRangeSize()
    {
        return (int) Math.round((((Double) max) - ((Double) min)) / step);
    }
    
    @Override
    public Double getValueForIndex(int index)
    {
        return ((Double) min) + index * step;
    }
    
    @Override
    public int indexOf(Double value)
    {
        return (int) Math.round((value - (Double) min) / step);
    }
}