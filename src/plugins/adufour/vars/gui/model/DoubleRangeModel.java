package plugins.adufour.vars.gui.model;

public class DoubleRangeModel extends RangeModel<Double>
{
	public DoubleRangeModel(Double defaultValue, Double min, Double max, Double step)
	{
		super(defaultValue, min, max, step);
	}
	
	@Override
	public boolean isValid(Double value)
	{
		return min.compareTo(value) <= 0 && max.compareTo(value) >= 0 && value % step == 0;
	}
}