package plugins.adufour.vars.gui.swing;

import java.awt.Dimension;
import java.lang.reflect.Array;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jdesktop.swingx.JXTable;

import plugins.adufour.vars.lang.VarTable;

public class TableEditor<T> extends SwingVarEditor<T>
{
    public TableEditor(VarTable<T> variable)
    {
        super(variable);
    }
    
    private TableModelListener tableListener;
    
    private boolean            tableUpdating = false;
    
    @Override
    protected JComponent createEditorComponent()
    {
        final VarTable<T> varTable = (VarTable<T>) variable;
        
        final JXTable editor = new JXTable(varTable.getRows(), varTable.getColumns());
        
        try
        {
            varTable.parseComponent("");
        }
        catch (UnsupportedOperationException e)
        {
            editor.setEnabled(false);
        }
        
        editor.setColumnControlVisible(true);
        
        tableListener = new TableModelListener()
        {
            @Override
            public void tableChanged(TableModelEvent e)
            {
                switch (e.getType())
                {
                    case TableModelEvent.INSERT:
                        System.out.print("inserted");
                    break;
                    case TableModelEvent.UPDATE:
                    {
                        if (tableUpdating) return;
                        
                        if (e.getFirstRow() == e.getLastRow())
                        {
                            int row = e.getFirstRow();
                            int col = e.getColumn();
                            varTable.setValue(row, col, editor.getValueAt(row, col));
                        }
                        System.out.print("updated");
                    }
                    break;
                    case TableModelEvent.DELETE:
                        System.out.print("deleted");
                    break;
                    
                    default:
                    break;
                }
                
                System.out.println(" col. " + e.getColumn() + ", rows " + e.getFirstRow() + "-" + e.getLastRow());
            }
        };
        
        // A scroll pane *must* be created to make the headers visible
        JScrollPane scrollPane = new JScrollPane(editor);
        
        return scrollPane;
    }
    
    @Override
    public JScrollPane getEditorComponent()
    {
        return (JScrollPane) super.getEditorComponent();
    }
    
    public JXTable getTable()
    {
        return (JXTable) getEditorComponent().getViewport().getView();
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(250, 80);
    }
    
    @Override
    protected void activateListeners()
    {
        if (getEditorComponent() != null) getTable().getModel().addTableModelListener(tableListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        if (getEditorComponent() != null) getTable().getModel().removeTableModelListener(tableListener);
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        tableUpdating = true;
        
        JXTable editor = getTable();
        
        T table = variable.getValue();
        
        if (table == null) return;
        
        int rows = Array.getLength(table);
        int columns = Array.getLength(Array.get(table, 0));
        
        for (int i = 0; i < rows; i++)
        {
            Object row = Array.get(table, i);
            
            for (int j = 0; j < columns; j++)
            {
                Object value = Array.get(row, j);
                editor.setValueAt(value, i, j);
            }
        }
        
        tableUpdating = false;
    }
    
    @Override
    public boolean isComponentResizeable()
    {
        return true;
    }
    
    @Override
    public double getComponentVerticalResizeFactor()
    {
        return 1.0;
    }
}
