package plugins.adufour.ezplug;


/**
 * Interface used to fire events occurring on variables
 * 
 * @author Alexandre Dufour
 * 
 * @param <T>
 */
public interface EzVarListener<T>
{
    /**
     * @param source
     * @param newValue
     */
    void variableChanged(EzVar<T> source, T newValue);
}
