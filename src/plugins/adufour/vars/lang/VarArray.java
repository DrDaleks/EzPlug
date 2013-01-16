package plugins.adufour.vars.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Convenience class defining a generic array of elements based on the specified inner type.<br/>
 * The type of the underlying array cannot be changed. To creates arrays of changeable types, use
 * {@link VarMutableArray} instead.<br/>
 * NOTE: this class provides convenience methods to add elements (similarly to the {@link ArrayList}
 * class, however performance on large arrays is not optimal. For performance-critical applications,
 * use the {@link VarArray} instead
 * 
 * @author Alexandre Dufour
 * 
 * @param <T>
 *            the inner type of the array
 */
public class VarArray<T> extends VarGenericArray<T[]> implements Iterable<T>
{
    /**
     * Creates a new array variable
     * 
     * @param name
     *            the variable name
     * @param type
     *            the data type of the array (including the <code>[]</code>)
     * @param defaultValue
     *            the initial array
     */
    public VarArray(String name, Class<T[]> type, T[] defaultValue)
    {
        super(name, type, defaultValue);
    }
    
    /**
     * Inserts the specified elements at the end of this array. This methods acts similarly to
     * {@link ArrayList#add(Object)}: a old array is replaced by a new array where the contents of
     * the old array is copied and the specified element is added last
     * 
     * @param elements
     *            the elements to add
     */
    public void add(T... elements)
    {
        T[] oldArray = getValue();
        ArrayList<T> newArray = new ArrayList<T>(size() + elements.length);
        
        for (T oldElem : oldArray)
            newArray.add(oldElem);
        newArray.addAll(Arrays.asList(elements));
        
        setValue(newArray.toArray(oldArray));
    }

    @Override
    public Iterator<T> iterator()
    {
        return Arrays.asList(getValue()).iterator();
    }
}
