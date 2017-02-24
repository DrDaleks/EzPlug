package plugins.adufour.ezplug;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import icy.util.StringUtil;
import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.gui.model.FileTypeModel;
import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.adufour.vars.lang.VarFile;

/**
 * Class defining a variable of type String, embarking a button triggering a file dialog as
 * graphical component
 * 
 * @author Alexandre Dufour
 */
public class EzVarFile extends EzVar<File>
{
    /**
     * Constructs a new input variable with given name and default file dialog path
     * 
     * @param varName
     *            the name of the variable (as it will appear in the interface)
     * @param path
     *            the default path to show in the file dialog
     */
    public EzVarFile(String varName, String path)
    {
        this(varName, path, (FileFilter) null);
    }
    
    /**
     * Constructs a new input variable with given name, file path and filter
     * 
     * @param varName
     *            the name of the variable (as it will appear in the interface)
     * @param path
     *            the default path to show in the file dialog
     * @param wildcard
     *            a wild-card filter (e.g. "*.tif"). Warning: this parameter is *not* a regular
     *            expression. To use regular expressions, see
     *            {@link #EzVarFile(String, String, Pattern)}
     */
    public EzVarFile(String varName, String path, final String wildcard)
    {
        this(varName, null, Pattern.compile(StringUtil.wildcardToRegex(wildcard)));
    }
    
    /**
     * Constructs a new input variable with given name, file path and filter
     * 
     * @param varName
     *            the name of the variable (as it will appear in the interface)
     * @param path
     *            the default path to show in the file dialog
     * @param pattern
     *            a regular expression pattern to filter the file list
     */
    public EzVarFile(String varName, String path, final Pattern pattern)
    {
        this(varName, null, new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isDirectory() || Pattern.matches(pattern.pattern(), pathname.getPath());
            }
        });
    }
    
    /**
     * Constructs a new input variable with given name, file path and filter
     * 
     * @param varName
     *            the name of the variable (as it will appear in the interface)
     * @param path
     *            the default path to show in the file dialog
     * @param filter
     *            the file filter
     */
    public EzVarFile(String varName, String path, FileFilter filter)
    {
        super(new VarFile(varName, null), new FileTypeModel(path, FileMode.FILES, filter, false));
    }
    
    /**
     * Replaces the button text by the given string
     * 
     * @param text
     */
    public void setButtonText(String text)
    {
        ((FileChooser) getVarEditor()).setButtonText(text);
    }
}
