package plugins.adufour.vars.util;

public interface MutableType
{
    void addTypeChangeListener(TypeChangeListener listener);
    
    void removeTypeChangeListener(TypeChangeListener listener);
    
    void setType(Class<?> type);
}
