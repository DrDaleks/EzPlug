package plugins.adufour.vars.lang;

import java.lang.reflect.Array;
import java.util.ArrayList;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.TableEditor;
import plugins.adufour.vars.util.ArrayType;

/**
 * Variable representing a two-dimensional array
 * 
 * @author Alexandre Dufour
 * 
 * @param <T>
 *            the table type (e.g. <code>int[][]</code>, not <code>int</code>
 */
public class VarTable<T> extends Var<T> implements ArrayType
{
    public static interface VarTableListener<T>
    {
        void tableChanged(VarTable<T> source, int row, int column, T oldValue, T newValue);
    }
    
    @SuppressWarnings("serial")
    public static class InvalidCellException extends RuntimeException
    {
        private final int         row, column;
        private final VarTable<?> source;
        
        public InvalidCellException(VarTable<?> table, int row, int column)
        {
            super("Invalid cell coordinate: " + row + "," + column);
            this.row = row;
            this.column = column;
            this.source = table;
        }
        
        public VarTable<?> getSource()
        {
            return source;
        }
        
        public int getRow()
        {
            return row;
        }
        
        public int getColumn()
        {
            return column;
        }
    }
    
    private final ArrayList<VarTableListener<T>> listeners = new ArrayList<VarTable.VarTableListener<T>>();
    
    @SuppressWarnings("unchecked")
    public VarTable(String name, Class<T> type, int rows, int columns)
    {
        super(name, type, (T) Array.newInstance(type.getComponentType().getComponentType(), rows, columns));
    }
    
    public void addTableListener(VarTableListener<T> listener)
    {
        listeners.add(listener);
    }
    
    public void removeTableListener(VarTableListener<T> listener)
    {
        listeners.remove(listener);
    }
    
    @Override
    public VarEditor<T> createVarEditor()
    {
        return new TableEditor<T>(this);
    }
    
    public void setValue(T newValue) throws IllegalAccessError
    {
        super.setValue(newValue);
        
        
    }
    
    public void setValue(int row, int column, Object value)
    {
        if (!getInnerType().equals(String.class) && value instanceof String)
        {
            value = parseComponent((String) value);
        }
        
        T oldValue = getValue();
        
        // dimension check
        if (Array.getLength(oldValue) <= row) throw new InvalidCellException(this, row, column);
        if (Array.get(oldValue, row) == null) throw new InvalidCellException(this, row, column);
        if (Array.getLength(Array.get(oldValue, row)) <= column) throw new InvalidCellException(this, row, column);
        
        T newValue = clone(oldValue);
        
        Array.set(Array.get(newValue, row), column, value);
        
        setValue(newValue);
    }
    
    @SuppressWarnings("unchecked")
    private T clone(T value)
    {
        int rows = Array.getLength(value);
        
        // retrieve the column size and inner type from cell [0,0]
        Object row0 = Array.get(value, 0);
        
        int cols = Array.getLength(row0);
        
        Class<?> elemType = row0.getClass().getComponentType();
        
        Object clone = Array.newInstance(elemType, rows, cols);
        
        for (int i = 0; i < rows; i++)
            System.arraycopy(Array.get(value, i), 0, Array.get(clone, i), 0, cols);
        
        return (T) clone;
    }
    
    public Class<?> getInnerType()
    {
        return getType() == null ? null : getType().getComponentType().getComponentType();
    }
    
    public int getColumns()
    {
        Object table = getValue();
        
        if (table == null) return -1;
        
        return Array.getLength(Array.get(table, 0));
    }
    
    public int getRows()
    {
        Object table = getValue();
        
        if (table == null) return -1;
        
        return Array.getLength(table);
    }
    
    public Object parseComponent(String s) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Parsing not implemented for type " + getClass().getSimpleName());
    }
    
    @Override
    public int getDimensions()
    {
        return 2;
    }
    
    @Override
    public String getSeparator(int dimension) throws ArrayIndexOutOfBoundsException
    {
        if (dimension == 0) return " ";
        if (dimension == 1) return "\n";

        throw new ArrayIndexOutOfBoundsException(dimension);    }
    
    @Override
    public int size(int dimension) throws ArrayIndexOutOfBoundsException
    {
        if (dimension == 0) return getRows();
        
        if (dimension == 1) return getColumns();
        
        throw new ArrayIndexOutOfBoundsException(dimension);
    }
}
