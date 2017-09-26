package plugins.adufour.vars.gui.model;

public class FloatRangeModel extends RangeModel<Float>
{
    public FloatRangeModel(Float defaultValue, Float min, Float max, Float step)
    {
        this(defaultValue, min, max, step, RangeEditorType.SPINNER);
    }
    
    public FloatRangeModel(Float defaultValue, Float min, Float max, Float step, RangeEditorType editorType)
    {
        super(defaultValue, min, max, step, editorType, null);
    }
    
    @Override
    public boolean isValid(Float value)
    {
        return min.compareTo(value) <= 0 && max.compareTo(value) >= 0 && value % step == 0;
    }
    
    @Override
    public int getRangeSize()
    {
        return Math.round((((Float) max) - ((Float) min)) / step);
    }
    
    @Override
    public Float getValueForIndex(int index)
    {
        return ((Float) min) + index * step;
    }
    
    @Override
    public int indexOf(Float value)
    {
        return Math.round((value - (Float) min) / step);
    }
}