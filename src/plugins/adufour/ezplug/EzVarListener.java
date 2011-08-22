package plugins.adufour.ezplug;

/**
 * Interface used to fire events occurring on variables
 * 
 * @author Alexandre Dufour
 *@param <T>
 *            The type of variable to listen to
 */
public interface EzVarListener<T>
{
	void variableChanged(EzVar<T> source, T newValue);
}
