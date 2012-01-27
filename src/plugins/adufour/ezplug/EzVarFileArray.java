package plugins.adufour.ezplug;

import java.io.File;

import plugins.adufour.vars.gui.FileMode;
import plugins.adufour.vars.lang.VarFiles;

/**
 * Class defining a variable of type String, embarking a button triggering a file dialog as graphical component
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarFileArray extends EzVar<File[]>
{
    /**
     * Constructs a new input variable with given name and default file dialog path
     * 
     * @param varName
     *            the name of the variable (as it will appear in the interface)
     * @param path
     *            the default path to show in the file dialog
     */
    public EzVarFileArray(String varName, final String path)
    {
        super(new VarFiles(varName, null), new VarFiles.VarConstraintByFileType(path, FileMode.FILES, null, false));
    }
}
