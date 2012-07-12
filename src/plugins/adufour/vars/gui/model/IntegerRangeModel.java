package plugins.adufour.vars.gui.model;

public class IntegerRangeModel extends RangeModel<Integer>
{
	public IntegerRangeModel(Integer defaultValue, Integer min, Integer max, Integer step)
	{
		super(defaultValue, min, max, step);
	}
	
	@Override
	public boolean isValid(Integer value)
	{
		return min.compareTo(value) <= 0 && max.compareTo(value) >= 0 && (value % step == 0);
	}
}