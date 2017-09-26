package plugins.adufour.vars.util;

/**
 * Enumeration of possible referencing policies of a Variable
 * 
 * @author Alexandre Dufour
 *
 */
public enum VarReferencingPolicy
{
    /**
     * The variable may neither reference nor be referenced by another variable
     */
    NONE,
    /**
     * The variable may reference but not be referenced by another variable
     */
    IN,
    /**
     * The variable may not reference but may be referenced by another variable
     */
    OUT,
    /**
     * The variable may reference or be referenced by another variable (default)
     */
    BOTH
}
