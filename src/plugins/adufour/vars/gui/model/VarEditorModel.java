package plugins.adufour.vars.gui.model;

/**
 * Variable constraint allowing to narrow the range of accepted values for input variables
 * 
 * @author Alexandre Dufour
 * 
 * @param <T>
 * @see SwimmingObjectTypeModel
 * @see ValueSelectionModel
 * @see RangeModel
 */
public interface VarEditorModel<T>
{
	boolean isValid(T value);
	
	T getDefaultValue();
}
