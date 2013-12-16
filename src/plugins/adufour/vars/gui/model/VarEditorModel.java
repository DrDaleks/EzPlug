package plugins.adufour.vars.gui.model;

import plugins.adufour.vars.lang.Var;

/**
 * Model usable by graphical user interfaces to build custom components for {@link Var} objects.
 * This can be used for instance to restrict the bounds of valid values. <br/>
 * <br/>
 * For more information and model templates, see the other classes of this package.
 * 
 * @author Alexandre Dufour
 * @param <T>
 * @see SwimmingObjectTypeModel
 * @see ValueSelectionModel
 * @see RangeModel
 * @see FileTypeModel
 * @see FileTypeListModel
 */
public interface VarEditorModel<T>
{
    boolean isValid(T value);
    
    T getDefaultValue();
}
