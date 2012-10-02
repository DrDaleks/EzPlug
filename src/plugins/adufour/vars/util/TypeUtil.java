package plugins.adufour.vars.util;

public class TypeUtil
{
    /**
     * @param primitiveType
     * @return the Boxed type corresponding to the specified primitive type (or the original type if
     *         not a primitive)
     */
    public static Class<?> toBoxedType(Class<?> primitiveType)
    {
        if (!primitiveType.isPrimitive()) return primitiveType;
        
        if (primitiveType == void.class) return Void.class;
        if (primitiveType == char.class) return Character.class;
        if (primitiveType == byte.class) return Byte.class;
        if (primitiveType == short.class) return Short.class;
        if (primitiveType == int.class) return Integer.class;
        if (primitiveType == long.class) return Long.class;
        if (primitiveType == float.class) return Float.class;
        if (primitiveType == double.class) return Double.class;
        
        throw new IllegalArgumentException(primitiveType.getName());
    }
    
    /**
     * This method is intended to help non-programming users in differentiating and understanding
     * the various Java types by giving them more explicit names
     * 
     * @param type
     * @return a human-friendly name designating the specified type.
     */
    public static String toFriendlyString(Class<?> type)
    {
        if (type == null) return "no type";
        
        if (type.isPrimitive()) return toFriendlyString(toBoxedType(type));
        
        if (type.isArray()) return "List of " + toFriendlyString(type.getComponentType()).toLowerCase() + "s";
        
        if (type == Double.class) return "Decimal";
        
        if (type == String.class) return "Text";
        
        return type.getSimpleName();
    }
}
