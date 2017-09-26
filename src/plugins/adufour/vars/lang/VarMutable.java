package plugins.adufour.vars.lang;

import java.lang.reflect.Array;
import java.util.ArrayList;

import icy.sequence.Sequence;
import icy.type.DataType;
import icy.type.collection.array.Array1DUtil;
import icy.type.collection.array.ArrayUtil;
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
        VarEditorFactory factory = VarEditorFactory.getDefaultFactory();
        
        if (getDefaultEditorModel() instanceof TypeSelectionModel)
        {
            return factory.createTypeChooser(this);
        }
        
        // Revert to the default editor
        return factory.createMutableVarEditor(this);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public VarEditor createVarViewer()
    {
        if (Sequence.class.equals(type))
        {
            return VarEditorFactory.getDefaultFactory().createSequenceViewer(this);
        }
        
        // Revert to the default viewer
        return super.createVarViewer();
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
        
        if (isReferenced()) throw new IllegalAccessError("Cannot change the type of variable \"" + getName() + "\": it is being referenced by another variable");
        
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
        
        // the obvious ones first
        if (newValue == null || getType().isAssignableFrom(newValue.getClass()))
        {
            super.setValue(newValue);
        }
        // the easy ones next
        else if (newValue instanceof Number && Number.class.isAssignableFrom(getType()) || (getType().isPrimitive() && getType() != Boolean.TYPE))
        {
            // put the common ones first to optimize a bit...
            if (getType() == Integer.class || getType() == Integer.TYPE) super.setValue(((Number) newValue).intValue());
            else if (getType() == Double.class || getType() == Double.TYPE) super.setValue(((Number) newValue).doubleValue());
            else if (getType() == Byte.class || getType() == Byte.TYPE) super.setValue(((Number) newValue).byteValue());
            else if (getType() == Short.class || getType() == Short.TYPE) super.setValue(((Number) newValue).shortValue());
            else if (getType() == Float.class || getType() == Float.TYPE) super.setValue(((Number) newValue).floatValue());
            else super.setValue(((Number) newValue).longValue());
        }
        // and now for the hard one
        else if (getType().isArray() && newValue.getClass().isArray())
        {
            // the easy way (both arrays match) is already covered above
            // => we need to handle the tricky case (Javascript) where newValue is *always* Object[]
            // ==> copy its elements "manually" into a valid array
            
            try
            {
                // create a new valid array
                Class<?> localType = getType().getComponentType();
                int nbValues = Array.getLength(newValue);
                Object array = Array.newInstance(localType, nbValues);
                
                if (localType.isPrimitive())
                {
                    DataType dataType = ArrayUtil.getDataType(array);
                    for (int i = 0; i < nbValues; i++)
                    {
                        // assume newValue also has numbers inside...
                        Number n = (Number) Array.get(newValue, i);
                        // let Icy do the conversion
                        Array1DUtil.setValue(array, i, dataType, n.doubleValue());
                    }
                }
                else
                {
                    System.arraycopy(newValue, 0, array, 0, nbValues);
                }
                
                super.setValue(array);
            }
            catch (ArrayStoreException typeError)
            {
                String text = "[" + ArrayUtil.arrayToString(newValue).replace(":", ", ") + "]";
                throw new ClassCastException(text + " is not of type " + getType().getSimpleName());
            }
        }
        // let's be nice and box single values into arrays
        else if (getType().isArray() && getType().getComponentType().isAssignableFrom(newValue.getClass()))
        {
            Object array = Array.newInstance(getType().getComponentType(), 1);
            Array.set(array, 0, newValue);
            super.setValue(array);
        }
        // This has to fail then...
        else throw new ClassCastException(newValue + " is not of type " + getType().getSimpleName());
        
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
