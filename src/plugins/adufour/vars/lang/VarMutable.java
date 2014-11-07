package plugins.adufour.vars.lang;

import icy.sequence.Sequence;

import java.lang.reflect.Array;
import java.util.ArrayList;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.gui.model.TypeSelectionModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.util.MutableType;
import plugins.adufour.vars.util.TypeChangeListener;
import plugins.adufour.vars.util.VarListener;

/**
 * Variable holding a value with mutable type, i.e. its type may be changed at runtime
 * 
 * @author Alexandre Dufour
 */
@SuppressWarnings("rawtypes")
public class VarMutable extends Var implements MutableType
{
    private final ArrayList<TypeChangeListener> listeners = new ArrayList<TypeChangeListener>();
    
    /**
     * Constructs a new mutable variable with specified name and type. The default value for mutable
     * variables is always null, in order to be reset properly
     * 
     * @param name
     * @param initialType
     *            the type of variable to handle (this can be changed via the
     *            {@link #setType(Class)} method
     */
    public VarMutable(String name, Class<?> initialType)
    {
        this(name, initialType, null);
    }
    
    /**
     * Constructs a new mutable variable with specified name and type. The default value for mutable
     * variables is always null, in order to be reset properly
     * 
     * @param name
     * @param initialType
     *            the type of variable to handle (this can be changed via the
     *            {@link #setType(Class)} method
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    @SuppressWarnings("unchecked")
    public VarMutable(String name, Class<?> initialType, VarListener<?> defaultListener)
    {
        super(name, initialType != null ? initialType : Object.class, null, defaultListener);
    }
    
    @Override
    public VarEditor createVarEditor()
    {
        if (getDefaultEditorModel() instanceof TypeSelectionModel)
        {
            return VarEditorFactory.getDefaultFactory().createTypeChooser(this);
        }
        else
        {
            return VarEditorFactory.getDefaultFactory().createMutableVarEditor(this);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public VarEditor createVarViewer()
    {
        if (Sequence.class.equals(type))
        {
            return VarEditorFactory.getDefaultFactory().createSequenceViewer(this);
        }
        else
        {
            return super.createVarViewer();
        }
    }
    
    public void addTypeChangeListener(TypeChangeListener listener)
    {
        listeners.add(listener);
    }
    
    public void removeTypeChangeListener(TypeChangeListener listener)
    {
        listeners.remove(listener);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean isAssignableFrom(Var source)
    {
        Class<?> sourceType = source.getType();
        
        // cannot point to null
        if (sourceType == null) return false;
        
        // null can point to anything (type will change after linking)
        if (type == null) return true;
        
        // special case for primitive numbers
        if (Number.class.isAssignableFrom(type) && sourceType.isPrimitive()) return true;
        // and the other way round
        if (Number.class.isAssignableFrom(sourceType) && type.isPrimitive()) return true;
        
        // last (default) case
        return type.isAssignableFrom(sourceType) || (type.isArray() && type.getComponentType().isAssignableFrom(sourceType));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setDefaultEditorModel(VarEditorModel model)
    {
        if (model instanceof TypeSelectionModel)
        {
            defaultEditorModel = model;
            setType(((TypeSelectionModel) model).getDefaultValue());
        }
        else super.setDefaultEditorModel(model);
    }
    
    @SuppressWarnings("unchecked")
    public void setType(Class<?> newType)
    {
        Class<?> oldType = this.getType();
        
        if (oldType == newType) return;
        
        if (isReferenced()) throw new IllegalAccessError("Cannot change the type of a linked mutable variable");
        
        setValue(null);
        
        this.type = newType;
        
        for (TypeChangeListener listener : listeners)
            listener.typeChanged(this, oldType, newType);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Object newValue)
    {
        // Since this method is loosely typed, we need to make the difference between single objects
        // and arrays of objects to prevent potential (erroneous) ClassCastException(s)
        
        if (newValue != null && getType().isArray())
        {
            // We are expecting an array of "getType().getComponentType()" objects
            Class<?> componentType = getType().getComponentType();
            
            if (newValue.getClass().isArray())
            {
                // the array could be Object[] with valid items inside (cast will fail)
                // => copy its elements "manually" into a valid array
                int nbValues = Array.getLength(newValue);
                Object array = Array.newInstance(componentType, nbValues);
                System.arraycopy(newValue, 0, array, 0, nbValues);
                super.setValue(array);
            }
            else if (componentType.isAssignableFrom(newValue.getClass()))
            {
                // newValue is not an array but a single object
                // => place it into a valid array
                Object array = Array.newInstance(componentType, 1);
                Array.set(array, 0, newValue);
                super.setValue(array);
            }
            else
            {
                // There clearly is a (real) problem...
                throw new ClassCastException("Cannot interpret " + newValue + " as an object of type " + getType());
            }
        }
        else
        {
            // The default method is good enough
            super.setValue(newValue);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setReference(Var variable) throws ClassCastException
    {
        // change the type just in time before setting the reference
        if (variable != null) setType(variable.getType());
        super.setReference(variable);
    }
}
