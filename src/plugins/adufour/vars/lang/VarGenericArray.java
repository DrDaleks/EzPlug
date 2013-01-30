package plugins.adufour.vars.lang;

import java.lang.reflect.Array;

import plugins.adufour.vars.util.ArrayType;

/**
 * Class defining a variable holding an array of arbitrary type (including Java primitives). This
 * class should be preferred to {@link VarArray} when dealing with large arrays of primitives in
 * order to optimize performances.
 * 
 * @author Alexandre Dufour
 * 
 * @param <A>
 *            the underlying array type (*not* the inner component type, e.g., <code>int[]</code>
 *            and not <code>int</code>)
 */
public class VarGenericArray<A> extends Var<A> implements ArrayType
{
    @SuppressWarnings("unchecked")
    public VarGenericArray(String name, Class<A> type, A defaultValue)
    {
        super(name, type, (defaultValue != null ? defaultValue : (A) Array.newInstance(type.getComponentType(), 0)));
        //if (type != null && !type.isArray()) throw new IllegalArgumentException("Cannot create variable " + name + ": " + type.getSimpleName() + " is not an array type");
    }
    
    @Override
    public int getDimensions()
    {
        return 1;
    }
    
    public Class<?> getInnerType()
    {
        return getType() == null ? null : getType().getComponentType();
    }
    
    @Override
    public String getSeparator(int dimension)
    {
        if (dimension == 0) return " ";
        
        throw new ArrayIndexOutOfBoundsException(dimension);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public A getValue()
    {
        Object o = super.getValue();
        if (o == null) return (A) Array.newInstance(getInnerType(), 0);
        
        if (type == null) return null;
        
        // convert single elements to singled-value arrays
        if (getInnerType().isAssignableFrom(o.getClass()))
        {
            Object array = Array.newInstance(o.getClass(), 1);
            Array.set(array, 0, o);
            return (A) array;
        }
        
        return (A) o;
    }
    
    @Override
    public boolean isAssignableFrom(Var<?> source)
    {
        Class<?> componentType = getInnerType();
        
        if (componentType == null) return false;
        
        if (source instanceof VarGenericArray)
        {
            // check if the inner type is compatible
            VarGenericArray<?> varNativeArray = (VarGenericArray<?>) source;
            Class<?> sourceComponentType = varNativeArray.getInnerType();
            if (sourceComponentType == null) return false;
            return componentType.isAssignableFrom(sourceComponentType);
        }
        else
            if (componentType.isAssignableFrom(source.getType()))
        {
            return true;
        }
        else return super.isAssignableFrom(source);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public A parse(String input)
    {
        Class<?> componentType = getInnerType();
        
        input = input.trim();
        
        if (input.isEmpty()) return (A) Array.newInstance(componentType, 0);
        
        String[] inputs = input.trim().split(getSeparator(0));
        A array = (A) Array.newInstance(componentType, inputs.length);
        
        for (int i = 0; i < inputs.length; i++)
            Array.set(array, i, parseComponent(inputs[i]));
        
        return array;
    }
    
    /**
     * Parse a string representing an element of the underlying array. Note that this method throws
     * an <code>UnsupportedOperationException</code> by default, therefore the implementation must
     * be provided by overriding classes
     * 
     * @param s
     *            the string to parse
     * @return a primitive type representing the given string
     */
    public Object parseComponent(String s) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Parsing not implemented for type " + getClass().getSimpleName());
    }
    
    /**
     * @return the size of this array, or -1 if the array is <code>null</code>
     */
    public int size()
    {
        return size(0);
    }
    
    @Override
    public int size(int dimension) throws ArrayIndexOutOfBoundsException
    {
        if (dimension > 0) throw new ArrayIndexOutOfBoundsException(dimension);
        
        Object array = getValue();
        
        return array == null ? -1 : Array.getLength(array);
    }
}
