package plugins.adufour.vars.gui.model;

public class IntegerRangeModel extends RangeModel<Integer>
{
    public IntegerRangeModel(Integer defaultValue, Integer min, Integer max, Integer step)
    {
        this(defaultValue, min, max, step, RangeEditorType.SPINNER);
    }
    
    public IntegerRangeModel(Integer defaultValue, Integer min, Integer max, Integer step, RangeEditorType editorType)
    {
        super(defaultValue, min, max, step, editorType, null);
    }
    
    @Override
    public boolean isValid(Integer value)
    {
        return min.compareTo(value) <= 0 && max.compareTo(value) >= 0 && (value % step == 0);
    }
    
    @Override
    public int getRangeSize()
    {
        return (((Integer) max) - ((Integer) min)) / step;
    }
    
    @Override
    public Integer getValueForIndex(int index)
    {
        return ((Integer) min) + index * step;
    }
    
    @Override
    public int indexOf(Integer value)
    {
        return Math.round((value - (Integer) min) / step);
    }
}