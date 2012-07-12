package plugins.adufour.vars.util;

public interface ArrayType
{
    /**
     * @return the number of dimensions in this array
     */
    int getDimensions();
    
    /**
     * @return the type of each element of this array
     */
    Class<?> getInnerType();
    
    /**
     * @return The character used to separate array values when displaying the value as a String
     * @param dimension
     *            the dimension for which the separator is wanted
     */
    String getSeparator(int dimension) throws ArrayIndexOutOfBoundsException;
    
    /**
     * Parse a string representing an element of the underlying array. Note that this method throws
     * an <code>UnsupportedOperationException</code> by default, therefore the implementation must
     * be provided by overriding classes
     * 
     * @param s
     *            the string to parse
     * @return a primitive type representing the given string
     */
    Object parseComponent(String s) throws UnsupportedOperationException;
    
    /**
     * @param dimension
     *            the dimension along which the size is wanted
     * @return the size of the array in the specified dimension,or -1 if the array is
     *         <code>null</code>
     * @throws ArrayIndexOutOfBoundsException
     *             if the dimension does not exist
     */
    int size(int dimension) throws ArrayIndexOutOfBoundsException;
}
