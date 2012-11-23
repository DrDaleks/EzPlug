package plugins.adufour.vars.gui.swing;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdesktop.swingx.JXTable;

import plugins.adufour.vars.lang.Var;

public class WorkbookEditor extends SwingVarEditor<Workbook>
{
    public WorkbookEditor(Var<Workbook> variable)
    {
        super(variable);
    }
    
    @Override
    protected JComponent createEditorComponent()
    {
        Workbook book = variable.getValue();
        
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
        
        int nSheets = book.getNumberOfSheets();
        
        for (int i = 0; i < nSheets; i++)
        {
            final Sheet sheet = book.getSheetAt(i);
            
            SheetModel model = new SheetModel(sheet);
            
            JXTable table = new JXTable(model);
            table.setColumnControlVisible(true);
            JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            tabs.addTab(sheet.getSheetName(), scrollPane);
        }
        return tabs;
    }
    
    @Override
    protected void activateListeners()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void deactivateListeners()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(0,0);
    }
    
    @Override
    public double getComponentVerticalResizeFactor()
    {
        return 1.0;
    }
    
    @Override
    public boolean isComponentResizeable()
    {
        return true;
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        
    }
    
    private class SheetModel implements TableModel
    {
        private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
        
        private final Sheet                   sheet;
        
        private int                           nbRows    = 0;
        
        private int                           nbCols    = 0;
        
        public SheetModel(Sheet sheet)
        {
            this.sheet = sheet;
            this.nbRows = sheet.getLastRowNum() + 1;
            this.nbCols = sheet.getRow(0).getLastCellNum() + 1;
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            Row row = sheet.getRow(rowIndex);
            if (row == null)
            {
                // create all rows in-between
                for (int i = 0; i < rowIndex; i++)
                    if (sheet.getRow(i) == null) sheet.createRow(i);
                row = sheet.createRow(rowIndex);
                
                if (rowIndex >= nbRows) nbRows = rowIndex + 1;
            }
            
            Cell cell = row.getCell(columnIndex, Row.CREATE_NULL_AS_BLANK);
            if (columnIndex >= nbCols) nbCols = columnIndex + 1;
            
            if (aValue instanceof Double)
            {
                cell.setCellValue(((Double) aValue).doubleValue());
            }
            else if (aValue instanceof Boolean)
            {
                cell.setCellValue(((Boolean) aValue).booleanValue());
            }
            else
            {
                cell.setCellValue(aValue.toString());
            }
        }
        
        @Override
        public void removeTableModelListener(TableModelListener l)
        {
            listeners.remove(l);
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return true;
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            Row row = sheet.getRow(rowIndex);
            
            if (row == null) return null;
            
            Cell cell = row.getCell(columnIndex, Row.RETURN_NULL_AND_BLANK);
            
            if (cell == null) return null;
            
            switch(cell.getCellType())
            {
                case Cell.CELL_TYPE_BLANK: return null;
                case Cell.CELL_TYPE_BOOLEAN: return cell.getBooleanCellValue();
                case Cell.CELL_TYPE_NUMERIC: return cell.getNumericCellValue();
                case Cell.CELL_TYPE_STRING: return cell.getStringCellValue();
                case Cell.CELL_TYPE_FORMULA: return cell.getCellFormula();
                default: return "?????";
            }
        }
        
        @Override
        public int getRowCount()
        {
            return nbRows;
        }
        
        @Override
        public String getColumnName(int columnIndex)
        {
            return "Col. " + columnIndex;
        }
        
        @Override
        public int getColumnCount()
        {
            return nbCols;
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex)
        {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public void addTableModelListener(TableModelListener l)
        {
            listeners.add(l);
        }
    }
}
