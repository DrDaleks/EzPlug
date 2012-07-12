package plugins.adufour.vars.gui.model;

public class FloatRangeModel extends RangeModel<Float>
{
	public FloatRangeModel(Float defaultValue, Float min, Float max, Float step)
	{
		super(defaultValue, min, max, step);
	}
	
	@Override
	public boolean isValid(Float value)
	{
		return min.compareTo(value) <= 0 && max.compareTo(value) >= 0 && value % step == 0;
	}
}