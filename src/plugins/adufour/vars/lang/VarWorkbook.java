package plugins.adufour.vars.lang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.processing.FilerException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.WorkbookEditor;
import plugins.adufour.vars.util.VarException;

public class VarWorkbook extends Var<Workbook>
{
    /**
     * Creates a new variable with a new empty workbook
     * 
     * @param name
     *            the name of the workbook
     */
    public VarWorkbook(String name)
    {
        this(name, name, false);
    }
    
    public VarWorkbook(String name, File file) throws InvalidFormatException, IOException
    {
        super(name, Workbook.class, WorkbookFactory.create(file));
    }
    
    /**
     * Creates a new variable with a new empty workbook
     * 
     * @param name
     *            the name of the workbook
     * @param firstSheetName
     *            the name of the first sheet
     * @param legacy
     *            <code>true</code> is the workbook format should use the old Excel 2003 format
     */
    public VarWorkbook(String name, String firstSheetName, boolean legacy)
    {
        super(name, Workbook.class, legacy ? new HSSFWorkbook() : new XSSFWorkbook());
        
        getValue().createSheet(firstSheetName);
    }
    
    @Override
    public VarEditor<Workbook> createVarEditor()
    {
        return new WorkbookEditor(this);
    }
    
    /**
     * Save the workbook to disk
     * 
     * @param folder
     *            the folder on disk where the workbook should be saved (must exist)
     * @param workbookName
     *            the name of the workbook (without extension)
     * @throws VarException
     *             if the workbook is <code>null</code>
     * @throws IOException
     *             if the file cannot be accessed
     */
    public void saveToDisk(File folder, String workbookName) throws VarException, IOException
    {
        if (!folder.isDirectory()) throw new FilerException(folder + "is not a valid folder");
        
        Workbook wb = getValue(true);
        
        String filename = folder.getPath() + File.separator + workbookName + ".xls";
        
        if (wb instanceof XSSFWorkbook) filename = filename + "x";
        
        FileOutputStream out = new FileOutputStream(filename);
        wb.write(out);
        out.close();
    }
}
