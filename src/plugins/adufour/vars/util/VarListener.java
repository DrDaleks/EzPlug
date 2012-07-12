package plugins.adufour.vars.util;

import plugins.adufour.vars.lang.Var;

/**
 * Interface allowing to listen to value- and reference- change events
 * 
 * @author Alexandre Dufour
 * 
 * @param <T>
 */
public interface VarListener<T>
{
	/**
	 * Called when the value of the source variable changes
	 * 
	 * @param source
	 *            the variable firing the listener
	 * @param oldValue
	 *            the old variable value
	 * @param newValue
	 *            the new variable value
	 */
	void valueChanged(Var<T> source, T oldValue, T newValue);
	
	/**
	 * Called when the variable reference changes
	 * 
	 * @param source
	 *            the variable firing the listener
	 * @param oldReference
	 *            the old variable reference
	 * @param newReference
	 *            the new variable reference
	 */
	void referenceChanged(Var<T> source, Var<? extends T> oldReference, Var<? extends T> newReference);
}
