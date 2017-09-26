package plugins.adufour.vars.util;

import plugins.adufour.vars.lang.VarMutable;
import plugins.adufour.vars.lang.VarMutableArray;

/**
 * Interface allowing to listen to value change events (used for mutable variables)
 * 
 * @author Alexandre Dufour
 * @see VarMutable
 * @see VarMutableArray
 */
public interface TypeChangeListener
{
    void typeChanged(Object source, Class<?> oldType, Class<?> newType);
}
